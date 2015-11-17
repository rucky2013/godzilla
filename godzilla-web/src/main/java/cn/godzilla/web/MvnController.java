package cn.godzilla.web;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.godzilla.common.BusinessException;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.common.response.ResponseBodyJson;
import cn.godzilla.service.MvnService;
import cn.godzilla.service.OperateLogService;

@Controller
@RequestMapping("/mvn")
public class MvnController extends GodzillaApplication{
	
	@Autowired
	private MvnService mvnService;
	@Autowired
	private OperateLogService operateLogService;
	
	/**
	 * 部署(打包)
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/deploy", method=RequestMethod.POST)
	@ResponseBody
	public Object deploy(@PathVariable String sid,@PathVariable String projectCode,@PathVariable String profile,HttpServletRequest request, HttpServletResponse response) {

		String parentVersion = StringUtil.getReqPrameter(request, "parentVersion", "");
		String parentVersionSuffix = StringUtil.getReqPrameter(request, "parentVersionSuffix", "");
		
		final String pencentkey = this.getPencentKey(projectCode, profile);
		
		GodzillaApplication.processPercent.put(pencentkey, "0");
		
		ReturnCodeEnum deployReturn = this.doDeploy(projectCode, profile, parentVersion+parentVersionSuffix, pencentkey);
		
		if(deployReturn.equals(ReturnCodeEnum.getByReturnCode(OK_MVNDEPLOY))
				||deployReturn.equals(ReturnCodeEnum.getByReturnCode(NO_RESTARTTOMCAT))
				||deployReturn.equals(ReturnCodeEnum.getByReturnCode(NO_MVNBUILD))) {
			//rpc那边生成日志，为了添加部署日志跟jar包日志
			return ResponseBodyJson.custom().setAll(deployReturn, DEPLOY).build().updateLog();
		} 
		return ResponseBodyJson.custom().setAll(deployReturn, DEPLOY).build().log();
	}
	
	
	
	private ReturnCodeEnum doDeploy(String projectCode, String profile, String version, String pencentkey) {
		
		/*
		 * -2.限制并发　发布
		 * 日常环境　每个项目　只允许　一个人发布（如果互相依赖项目　并发发布，还是会出现问题）
		 * 准生产	　所有项目只允许一个人发布
		 * 生产　　　所有项目只允许一个人发布
		 */
		Lock lock = null;
		boolean hasAC = false;
		try {
			if(profile.equals(TEST_PROFILE)) {
				lock = GodzillaApplication.deploy_lock.get(projectCode);
				hasAC = lock.tryLock(1, TimeUnit.SECONDS);
				if(!hasAC) 
					return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
			} else {
				lock = GodzillaApplication.deploy_lock.get(profile);
				hasAC = lock.tryLock(1, TimeUnit.SECONDS);
				if(!hasAC) 
					return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
			}
			
			ReturnCodeEnum deployReturn = mvnService.doDeploy(projectCode, profile, version, pencentkey);
			
			if(deployReturn.equals(ReturnCodeEnum.OK_MVNDEPLOY)) {
				GodzillaApplication.processPercent.put(pencentkey, "100");
			} else {
				GodzillaApplication.processPercent.put(pencentkey, "0");
			}
			final String pencentkeyF = pencentkey;
			new Thread() {
				public void run() {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					GodzillaApplication.processPercent.put(pencentkeyF, "0");
				}
			}.start();
			
			return deployReturn;
		} catch(InterruptedException e) {
			e.printStackTrace();
			return ReturnCodeEnum.getByReturnCode(NO_INTERRUPTEDEX);
		} catch(BusinessException e){
			e.printStackTrace();
			return ReturnCodeEnum.getByReturnCode(NO_SYSTEMEX).setSystemEXMsg(e.getErrorMsg());
		} catch(Throwable e) {
			e.printStackTrace();
			return ReturnCodeEnum.getByReturnCode(NO_SYSTEMEX).setSystemEXMsg(e.getMessage());
		} finally {
			if(lock!=null) {
				try {
					lock.unlock();
				} /*catch(InvocationTargetException e2) {
					return ReturnCodeEnum.getByReturnCode(NO_HASKEYDEPLOY);
				} */
				catch(IllegalMonitorStateException e1) {
					return ReturnCodeEnum.getByReturnCode(NO_CONCURRENCEDEPLOY);
				} 
			}
		}
		
	}



	private String getPencentKey(String projectCode, String profile) {
		return projectCode + "-" + profile;
	}

	/**
	 * 查询部署进度
	 * 进度存在 ResponseBodyJson.data里
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/process", method=RequestMethod.POST)
	@ResponseBody
	public Object process(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, HttpServletRequest request, HttpServletResponse response) {
		
		ReturnCodeEnum returnenum = mvnService.getProcessPercent(projectCode, profile, this.getPencentKey(projectCode, profile));
		
		return ResponseBodyJson.custom().setAll(returnenum, "").build();
	}
	
}
