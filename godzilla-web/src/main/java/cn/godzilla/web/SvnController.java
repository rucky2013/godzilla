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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.response.ResponseBodyJson;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.Project;
import cn.godzilla.model.SvnBranchConfig;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.OperateLogService;
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
@RequestMapping(value="")
public class SvnController extends GodzillaApplication implements Constant{

	private final Logger logger = LogManager.getLogger(SvnController.class);
	
	@Autowired
	private ProjectService projectService ;
	@Autowired
	private SvnBranchConfigService svnBranchConfigService;
	@Autowired
	private ClientConfigService clientConfigService;
	@Autowired
	private SvnService svnService;
	@Autowired
	private OperateLogService operateLogService;
	@Autowired
	private BaseShellCommand command;
	/**
	 * 状态查看
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/svn/{sid}/{projectCode}/{profile}/status", method=RequestMethod.GET)
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
			branches = EMPTY_BRANCH;
		} else {
			branches = branches.substring(0, branches.length()-1);
		}
		String callbackUrl = "http://localhost:8080/process-callback.do";
		
		String operator = super.getUser().getUserName();
		String str = "";
		try {
			str ="sh /home/godzilla/gzl/shell/server/svn_server_wl.sh status "+trunkPath+" '"+branches+"' "+" "+callbackUrl+" "+projectCode+" "+ operator +" "+clientIp ;
			flag = command.execute(str, super.getUser().getUserName(), projectCode, project.getSvnUsername(), project.getSvnPassword());
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		//如果有回显的话  将后台信息输出到 mq
		if(flag){
			String username = super.getUser().getUserName();
			operateLogService.addSvnCommandLog(username, trunkPath, str, username);
			operateLogService.addOperateLog(username, super.getUser().getRealName(), projectCode, profile, SVNSTATUS, SUCCESS, "状态查看SUCCESS");
			logger.info("************状态查看End**************");
			String echoMessage = echoMessageThreadLocal.get();
			return ResponseBodyJson.custom().setAll(OK_AJAX, SUCCESS, echoMessage).build();
		}else{
			String username = super.getUser().getUserName();
			operateLogService.addSvnCommandLog(username, trunkPath, str, username);
			operateLogService.addOperateLog(username, super.getUser().getRealName(), projectCode, profile, SVNSTATUS, FAILURE, "状态查看FAILURE");
			logger.error("************状态查看Error**************");
			return ResponseBodyJson.custom().setAll(NO_AJAX, FAILURE, "").build();
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
	@RequestMapping(value="/svn/{sid}/{projectCode}/{profile}/merge", method=RequestMethod.GET)
	@ResponseBody
	public Object doMerge(@PathVariable String sid, @PathVariable String projectCode,@PathVariable String profile, HttpServletRequest request, HttpServletResponse response) {
		
		boolean flag = svnService.svnMerge(projectCode, profile);
		if(flag){
			logger.info("************代码合并End**************");
			return ResponseBodyJson.custom().setAll(OK_AJAX, SUCCESS, "").build();
		}else{
			logger.error("************代码合并Error**************");
			return ResponseBodyJson.custom().setAll(NO_AJAX, FAILURE, "").build();
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
	@RequestMapping(value="/svn/{sid}/{projectCode}/{profile}/commit", method=RequestMethod.GET)
	@ResponseBody
	public Object commit(@PathVariable String sid, @PathVariable String projectCode,@PathVariable String profile, HttpServletRequest request, HttpServletResponse response) {
		
		logger.info("************提交主干Begin***********");
		
		ReturnCodeEnum returnEnum = svnService.svnCommit(projectCode, profile);
		operateLogService.addOperateLog(super.getUser().getUserName(), super.getUser().getRealName(), projectCode, profile, SVNCOMMIT, returnEnum.getStatus(), returnEnum.getReturnMsg());
		logger.info("************提交主干End "+returnEnum.getStatus()+":"+returnEnum.getReturnMsg()+"**************");
		return ResponseBodyJson.custom().setAll(returnEnum).build();
	}
	
	/**
	 * 分支设置
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param branchUrl
	 * @param currentVersion
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/svnbranch/{sid}/{projectCode}/{profile}/add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, 
			@RequestParam("branchUrl") String branchUrl,
			HttpServletRequest request, HttpServletResponse response) {

		
		logger.info("**************添加分支设置***********projectCode:" + projectCode
				+ ",branchUrl:" + branchUrl);

		boolean flag = svnBranchConfigService.addNewBranch(projectCode, profile, branchUrl);
		
		if(flag){
			operateLogService.addOperateLog(super.getUser().getUserName(), super.getUser().getRealName(), projectCode, profile, BRANCHADD, SUCCESS, "添加分支设置SUCCESS");
			logger.info("************添加分支设置End**************");
			return SUCCESS;
		}else{
			operateLogService.addOperateLog(super.getUser().getUserName(), super.getUser().getRealName(), projectCode, profile, BRANCHADD, FAILURE, "添加分支设置FAILURE");
			logger.error("************添加分支设置Error**************");
			return FAILURE;
		}
	}
	
	/**
	 * 分支编辑 保存  
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param branchUrl
	 * @param currentVersion
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/svnbranch/{sid}/{projectCode}/{profile}/edit", method = RequestMethod.POST)
	@ResponseBody
	public Object edit(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, 
			@RequestParam("id") String id,
			@RequestParam("branchUrl") String branchUrl,
			HttpServletRequest request, HttpServletResponse response) {

		
		logger.info("**************分支编辑 保存***********projectCode:" + projectCode
				+ ",branchUrl:" + branchUrl);

		boolean flag = svnBranchConfigService.editBranch(projectCode, profile, id, branchUrl);
		
		if(flag){
			operateLogService.addOperateLog(super.getUser().getUserName(), super.getUser().getRealName(), projectCode, profile, BRANCHEDIT, SUCCESS, "分支编辑 保存SUCCESS");
			logger.info("************分支编辑 保存End**************");
			return ResponseBodyJson.custom().setAll(OK_AJAX, SUCCESS, "").build();
		}else{
			operateLogService.addOperateLog(super.getUser().getUserName(), super.getUser().getRealName(), projectCode, profile, BRANCHEDIT, FAILURE, "分支编辑 保存FAILURE");
			logger.error("************分支编辑 保存Error**************");
			return ResponseBodyJson.custom().setAll(NO_AJAX, FAILURE, "").build();
		}
	}
	
	@RequestMapping(value="/svnbranch/{sid}/{projectCode}/{profile}/delete", method=RequestMethod.GET) 
	@ResponseBody
	public Object delete(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, 
			@RequestParam("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		
		logger.info("************分支编辑　删除×××××××××××××××××××***" ) ;
		ReturnCodeEnum returnEnum = svnBranchConfigService.deleteBranch(projectCode, profile, id);
		return ResponseBodyJson.custom().setAll(returnEnum).build();
	}
}
