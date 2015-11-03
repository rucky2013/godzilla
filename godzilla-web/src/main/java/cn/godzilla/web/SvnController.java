package cn.godzilla.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class SvnController extends GodzillaApplication implements Constant{

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
		
		ReturnCodeEnum returnenum = svnService.getStatus(projectCode, profile);
		
		return ResponseBodyJson.custom().setAll(returnenum, echoMessageThreadLocal.get(), SVNSTATUS).build().log();
	}
	
	/**
	 * 代码合并
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/svn/{sid}/{projectCode}/{profile}/merge", method=RequestMethod.GET)
	@ResponseBody
	public Object doMerge(@PathVariable String sid, @PathVariable String projectCode,@PathVariable String profile, HttpServletRequest request, HttpServletResponse response) {
		
		ReturnCodeEnum returnEnum = svnService.svnMerge(projectCode, profile);
		return ResponseBodyJson.custom().setAll(returnEnum, SVNMERGE).build().log();
	}

	/**
	 * 提交主干
	 * 1.合并分支
	 * if branches
	 * 		检出主干-->合并分支-->删除分支-->提交主干
	 * else branches is null
	 * 		检出主干-->exit 5
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/svn/{sid}/{projectCode}/{profile}/commit", method=RequestMethod.GET)
	@ResponseBody
	public Object commit(@PathVariable String sid, @PathVariable String projectCode,@PathVariable String profile, HttpServletRequest request, HttpServletResponse response) {
		
		ReturnCodeEnum returnEnum = svnService.svnCommit(projectCode, profile);
		return ResponseBodyJson.custom().setAll(returnEnum, SVNCOMMIT).build().log();
	}
	
	/**
	 * 分支设置
	 * 注：20151103 清除project表 merge_status标记,初始化为0
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param branchUrl
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/svnbranch/{sid}/{projectCode}/{profile}/add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, 
			@RequestParam("branchUrl") String branchUrl,
			HttpServletRequest request, HttpServletResponse response) {

		ReturnCodeEnum returnEnum = svnBranchConfigService.addNewBranch(projectCode, profile, branchUrl);
		return ResponseBodyJson.custom().setAll(returnEnum, BRANCHADD).build().log();
	}
	
	/**
	 * 分支编辑 保存 
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param id
	 * @param branchUrl
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/svnbranch/{sid}/{projectCode}/{profile}/edit", method = RequestMethod.POST)
	@ResponseBody
	@Deprecated
	public Object edit(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, 
			@RequestParam("id") String id,
			@RequestParam("branchUrl") String branchUrl,
			HttpServletRequest request, HttpServletResponse response) {

		
		ReturnCodeEnum returnEnum = svnBranchConfigService.editBranch(projectCode, profile, id, branchUrl);
		return ResponseBodyJson.custom().setAll(returnEnum, BRANCHEDIT).build().log();
	}

	/**
	 * 分支编辑 删除
	 * 注：20151103 清除project表 merge_status标记,初始化为0
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/svnbranch/{sid}/{projectCode}/{profile}/delete", method=RequestMethod.GET) 
	@ResponseBody
	public Object delete(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, 
			@RequestParam("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		
		ReturnCodeEnum returnEnum = svnBranchConfigService.deletebranchesByProjectCode(projectCode, id);
		return ResponseBodyJson.custom().setAll(returnEnum, BRANCHDELETE).build().log();
	}
	/**
	 * 标记此项目 冲突分支 冲突已经解决
	 * 注：20151103 清除project表 merge_status标记,初始化为0
	 * @param sid
	 * @param projectCode
	 * @param profile
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/svnbranch/{sid}/{projectCode}/{profile}/resolved", method=RequestMethod.GET)
	@ResponseBody
	public Object resolved(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, HttpServletRequest request, HttpServletResponse response) {
		
		ReturnCodeEnum returnEnum = svnService.svnResolved(projectCode, profile);
		return ResponseBodyJson.custom().setAll(returnEnum, CONFLICTRESOLVED).build().log();
	}
}
