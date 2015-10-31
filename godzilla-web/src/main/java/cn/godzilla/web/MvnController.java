package cn.godzilla.web;


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
		
		ReturnCodeEnum deployReturn = mvnService.doDeploy(projectCode, profile, parentVersion+parentVersionSuffix, pencentkey);
		
		if(deployReturn.equals(ReturnCodeEnum.getByReturnCode(OK_MVNDEPLOY))
				||deployReturn.equals(ReturnCodeEnum.getByReturnCode(NO_RESTARTTOMCAT))
				||deployReturn.equals(ReturnCodeEnum.getByReturnCode(NO_MVNBUILD))) {
			//rpc那边生成日志，为了添加部署日志跟jar包日志
			return ResponseBodyJson.custom().setAll(deployReturn, DEPLOY).build().updateLog();
		} 
		return ResponseBodyJson.custom().setAll(deployReturn, DEPLOY).build().log();
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
		
		ReturnCodeEnum returnenum = mvnService.getProcessPercent(this.getPencentKey(projectCode, profile));
		
		return ResponseBodyJson.custom().setAll(returnenum, "").build();
	}
	
}
