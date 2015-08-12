package cn.godzilla.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.Project;
import cn.godzilla.model.SvnBranchConfig;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.SvnBranchConfigService;
import cn.godzilla.service.SvnService;
import cn.godzilla.svn.BaseShellCommand;

/**
 * 合并代码
 * @author ZhongweiLee
 *
 */
@Controller
@RequestMapping(value="/svn")
public class SvnController extends GodzillaApplication implements Constant{

	private final Logger logger = LogManager.getLogger(SvnController.class);
	
	@Autowired
	private ProjectService projectService ;
	@Autowired
	private SvnBranchConfigService svnBranchConfigService;
	@Autowired
	private ClientConfigService clientConfigService;
	@Autowired
	private SvnService SvnService;
	
	/**
	 * 状态查看
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/status", method=RequestMethod.GET)
	@ResponseBody
	public Object doStatus(@PathVariable String sid, @PathVariable String projectCode,@PathVariable String profile, HttpServletRequest request, HttpServletResponse response) {
		
		logger.info("************状态查看Begin***********");
		
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		String clientIp = clientConfig.getRemoteIp();
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode);
		Project project = projectService.qureyByProCode(projectCode);
		String trunkPath = project.getRepositoryUrl();
		String localPath=project.getCheckoutPath(); 
		
		boolean flag = false;
		
		String branches = "";
		for(SvnBranchConfig sbc: svnBranchConfigs) {
			branches = sbc.getBranchUrl() + ",";
		}
		branches = branches.substring(0, branches.length()-1);
		String callbackUrl = "http://localhost:8080/process-callback.do";
		
		String operator = super.getUser().getUserName();
		String str = "";
		try {
			BaseShellCommand command = new BaseShellCommand();
			str ="sh /home/godzilla/gzl/shell/server/svn_server_wl.sh status "+trunkPath+" '"+branches+"' "+" "+callbackUrl+" "+projectCode+" "+ operator +" "+clientIp ;
			flag = command.execute(str, super.getUser().getUserName());
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		//如果有回显的话  将后台信息输出到 mq
		if(!isEcho){
			String echoMessage = echoMessageThreadLocal.get();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("returncode", OK_AJAX);
			resultMap.put("returnmsg", SUCCESS);
			resultMap.put("echoMessage", echoMessage);		
			return resultMap;
		} else {
			if(flag){
				logger.info("************状态查看End**************");
				return SUCCESS;
			}else{
				logger.error("************状态查看Error**************");
				return FAILURE;
			}
		}
		
	}
	
	/**
	 * 代码合并
	 * @param branchPath
	 * 项目分支svn地址
	 * @param svnpwd
	 * svn密码
	 * @param trunkPath
	 * 项目主干svn地址
	 * @param projectName
	 * 项目code
	 * @param clientIp
	 * 客户端IP
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/merge", method=RequestMethod.GET)
	@ResponseBody
	public Object doMerge(@PathVariable String sid, @PathVariable String projectCode,@PathVariable String profile, HttpServletRequest request, HttpServletResponse response) {
		
		logger.info("************代码合并Begin***********");
		
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		String clientIp = clientConfig.getRemoteIp();
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode);
		Project project = projectService.qureyByProCode(projectCode);
		String trunkPath = project.getRepositoryUrl();
		String localPath=project.getCheckoutPath(); 
		
		boolean flag = false;
		
		String branches = "";
		for(SvnBranchConfig sbc: svnBranchConfigs) {
			branches = sbc.getBranchUrl() + ",";
		}
		branches = branches.substring(0, branches.length()-1);
		String callbackUrl = "http://localhost:8080/process-callback.do";
		
		String operator = super.getUser().getUserName();
		try {
			BaseShellCommand command = new BaseShellCommand();
			String str = "sh /home/godzilla/gzl/shell/server/svn_server_wl.sh merge "+trunkPath+" '"+branches+"' "+" "+callbackUrl+" "+projectCode+" "+ operator +" "+clientIp ;
			flag = command.execute(str, super.getUser().getUserName());
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		if(flag){
			logger.info("************代码合并End**************");
			return SUCCESS;
		}else{
			logger.error("************代码合并Error**************");
			return FAILURE;
		}
	}
	/**
	 * 提交主干
	 * 1.合并分支
	 * if branches
	 * 		检出主干-->合并分支-->删除分支-->提交主干
	 * else branches is null
	 * 		检出主干-->exit 5
	 * @param branchPath
	 * 项目分支svn地址
	 * @param svnpwd
	 * svn密码
	 * @param trunkPath
	 * 项目主干svn地址
	 * @param projectName
	 * 项目code
	 * @param clientIp
	 * 客户端IP
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/commit", method=RequestMethod.GET)
	@ResponseBody
	public Object commit(@PathVariable String sid, @PathVariable String projectCode,@PathVariable String profile, HttpServletRequest request, HttpServletResponse response) {
		
		logger.info("************提交主干Begin***********");
		
		ReturnCodeEnum returncode = SvnService.svnCommit(projectCode, profile);
		if(returncode==ReturnCodeEnum.OK_SVNCOMMIT){
			logger.info("************提交主干End success**************");
			return SUCCESS;
		} else if(returncode==ReturnCodeEnum.NO_SVNCOMMIT){
			logger.error("************提交主干Error: svn提交失败**************");
			return FAILURE;
		} else if(returncode==ReturnCodeEnum.NO_FOUNDCONFLICT){
			logger.error("************提交主干Erro  合并出现冲突，请先解决冲突**************");
			return FAILURE;
		} else if(returncode==ReturnCodeEnum.NO_CLIENTPARAM){
			logger.error("************提交主干Error  client.sh缺少参数**************");
			return FAILURE;
		} else if(returncode==ReturnCodeEnum.NO_SERVERPARAM){
			logger.error("************提交主干Error server.sh缺少参数**************");
			return FAILURE;
		} else if(returncode==ReturnCodeEnum.NO_CHANGECOMMIT){ //没有更改可以提交
			logger.error("************提交主干Error 没有更改可以提交**************");
			return FAILURE;
		} else if(returncode==ReturnCodeEnum.NO_JAVASHELLCALL){
			logger.error("************提交主干Error  java调用shell执行失败**************");
			return FAILURE;
		} else {
			logger.error("************提交主干Error **************");
			return FAILURE;
		}
	}
}
