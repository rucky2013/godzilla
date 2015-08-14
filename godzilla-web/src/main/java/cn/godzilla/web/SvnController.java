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
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.SvnBranchConfigService;
import cn.godzilla.service.SvnCmdLogService;
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
	@Autowired
	private OperateLogService operateLogService;
	@Autowired
	private SvnCmdLogService svnCmdLogService;
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
		if("".equals(branches)) {
			branches = "empty";
		} else {
			branches = branches.substring(0, branches.length()-1);
		}
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
		if(flag){
			String username = super.getUser().getUserName();
			svnCmdLogService.addSvnCommandLog(username, trunkPath, str, username);
			operateLogService.addOperateLog(username, projectCode, profile, SVNSTATUS, SUCCESS, "状态查看SUCCESS");
			logger.info("************状态查看End**************");
			String echoMessage = echoMessageThreadLocal.get();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("returncode", OK_AJAX);
			resultMap.put("returnmsg", SUCCESS);
			resultMap.put("echoMessage", echoMessage);		
			return resultMap;
		}else{
			String username = super.getUser().getUserName();
			svnCmdLogService.addSvnCommandLog(username, trunkPath, str, username);
			operateLogService.addOperateLog(username, projectCode, profile, SVNSTATUS, FAILURE, "状态查看FAILURE");
			logger.error("************状态查看Error**************");
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("returncode", NO_AJAX);
			resultMap.put("returnmsg", FAILURE);
			resultMap.put("echoMessage", FAILURE);		
			return resultMap;
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
		if("".equals(branches)) {
			branches = "empty";
		} else {
			branches = branches.substring(0, branches.length()-1);
		}
		
		String callbackUrl = "http://localhost:8080/process-callback.do";
		
		String operator = super.getUser().getUserName();
		String str= "";
		try {
			BaseShellCommand command = new BaseShellCommand();
			str = "sh /home/godzilla/gzl/shell/server/svn_server_wl.sh merge "+trunkPath+" '"+branches+"' "+" "+callbackUrl+" "+projectCode+" "+ operator +" "+clientIp ;
			flag = command.execute(str, super.getUser().getUserName());
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		if(flag){
			String username = super.getUser().getUserName();
			svnCmdLogService.addSvnCommandLog(username, trunkPath, str, username);
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, SVNMERGE, SUCCESS, "代码合并SUCCESS");
			logger.info("************代码合并End**************");
			return SUCCESS;
		}else{
			String username = super.getUser().getUserName();
			svnCmdLogService.addSvnCommandLog(username, trunkPath, str, username);
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, SVNMERGE, FAILURE, "代码合并FAILURE");
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
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, SVNCOMMIT, SUCCESS, ReturnCodeEnum.OK_SVNCOMMIT.getReturnMsg());
			logger.info("************提交主干End success**************");
			return SUCCESS;
		} else if(returncode==ReturnCodeEnum.NO_SVNCOMMIT){
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, SVNCOMMIT, FAILURE, ReturnCodeEnum.NO_SVNCOMMIT.getReturnMsg());
			logger.error("************提交主干Error: svn提交失败**************");
			return FAILURE;
		} else if(returncode==ReturnCodeEnum.NO_FOUNDCONFLICT){
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, SVNCOMMIT, FAILURE, ReturnCodeEnum.NO_FOUNDCONFLICT.getReturnMsg());
			logger.error("************提交主干Erro  合并出现冲突，请先解决冲突**************");
			return FAILURE;
		} else if(returncode==ReturnCodeEnum.NO_CLIENTPARAM){
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, SVNCOMMIT, FAILURE, ReturnCodeEnum.NO_CLIENTPARAM.getReturnMsg());
			logger.error("************提交主干Error  client.sh缺少参数**************");
			return FAILURE;
		} else if(returncode==ReturnCodeEnum.NO_SERVERPARAM){
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, SVNCOMMIT, FAILURE, ReturnCodeEnum.NO_SERVERPARAM.getReturnMsg());
			logger.error("************提交主干Error server.sh缺少参数**************");
			return FAILURE;
		} else if(returncode==ReturnCodeEnum.NO_CHANGECOMMIT){ //没有更改可以提交
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, SVNCOMMIT, FAILURE, ReturnCodeEnum.NO_CHANGECOMMIT.getReturnMsg());
			logger.error("************提交主干Error 没有更改可以提交**************");
			return FAILURE;
		} else if(returncode==ReturnCodeEnum.NO_JAVASHELLCALL){
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, SVNCOMMIT, FAILURE, ReturnCodeEnum.NO_JAVASHELLCALL.getReturnMsg());
			logger.error("************提交主干Error  java调用shell执行失败**************");
			return FAILURE;
		} else {
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, SVNCOMMIT, FAILURE, "提交主干FAILURE");
			logger.error("************提交主干Error **************");
			return FAILURE;
		}
	}
}
