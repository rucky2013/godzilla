package cn.godzilla.web;

import java.util.List;

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
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.Project;
import cn.godzilla.model.SvnBranchConfig;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.SvnBranchConfigService;
import cn.godzilla.svn.BaseShellCommand;

/**
 * 合并代码
 * @author ZhongweiLee
 *
 */
@Controller
@RequestMapping(value="/svn")
public class SvnController extends SuperController implements Constant{

	private final Logger logger = LogManager.getLogger(SvnController.class);
	
	@Autowired
	private ProjectService projectService ;
	@Autowired
	private SvnBranchConfigService svnBranchConfigService;
	@Autowired
	private ClientConfigService clientConfigService;
	
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
		try {
			BaseShellCommand command = new BaseShellCommand();
			String str = "sh /home/godzilla/gzl/shell/server/svn_server_wl.sh status "+trunkPath+" '"+branches+"' "+" "+callbackUrl+" "+projectCode+" "+ operator +" "+clientIp ;
			flag = command.execute(str);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		if(flag){
			logger.info("************状态查看End**************");
			return SUCCESS;
		}else{
			logger.error("************状态查看Error**************");
			return FAILURE;
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
			flag = command.execute(str);
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
		
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile) ;
		String clientIp = clientConfig.getRemoteIp();
		List<SvnBranchConfig> svnBranchConfigs = svnBranchConfigService.queryListByProjectCode(projectCode);
		Project project = projectService.qureyByProCode(projectCode);
		String trunkPath = project.getRepositoryUrl();
		
		boolean flag = false;
		
		String branches = "";
		for(SvnBranchConfig sbc: svnBranchConfigs) {
			branches = sbc.getBranchUrl() + ",";
		}
		branches = branches.substring(0, branches.length()-1);
		String callbackUrl = "http://localhost:8080/process-callback.do";
		
		String operator = super.getUser().getUserName();
		
		try {
			String str = "sh /home/godzilla/gzl/shell/server/svn_server_wl.sh commit "+trunkPath+" '"+branches+"' "+" "+callbackUrl+" "+projectCode+" "+ operator +" "+clientIp ;
			BaseShellCommand command = new BaseShellCommand();
			flag = command.execute(str);
			
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		if(flag){
			logger.info("************提交主干End**************");
			return SUCCESS;
		}else{
			logger.error("************提交主干Error**************");
			return FAILURE;
		}
	}
}
