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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjPrivateService;
import cn.godzilla.service.SvnBranchConfigService;

@Controller
@RequestMapping("svnbranch")
public class SvnBranchConfigController extends GodzillaApplication implements Constant{

	private final Logger logger = LogManager.getLogger(SvnBranchConfigController.class);

	@Autowired
	SvnBranchConfigService svnBranchConfigService;

	@Autowired
	ProjPrivateService projPrivateService;

	@Autowired
	OperateLogService operateLogService;
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
	@RequestMapping(value = "/{sid}/{projectCode}/{profile}/add", method = RequestMethod.GET)
	@ResponseBody
	public Object add(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, 
			@RequestParam("branchUrl") String branchUrl,
			HttpServletRequest request, HttpServletResponse response) {

		
		logger.info("**************添加分支设置***********projectCode:" + projectCode
				+ ",branchUrl:" + branchUrl);

		boolean flag = svnBranchConfigService.addNewBranch(projectCode, profile, branchUrl);
		
		if(flag){
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, BRANCHADD, SUCCESS, "添加分支设置SUCCESS");
			logger.info("************添加分支设置End**************");
			return SUCCESS;
		}else{
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, BRANCHADD, FAILURE, "添加分支设置FAILURE");
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
	@RequestMapping(value = "/{sid}/{projectCode}/{profile}/edit", method = RequestMethod.GET)
	@ResponseBody
	public Object edit(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, 
			@RequestParam("id") String id,
			@RequestParam("branchUrl") String branchUrl,
			HttpServletRequest request, HttpServletResponse response) {

		
		logger.info("**************分支编辑 保存***********projectCode:" + projectCode
				+ ",branchUrl:" + branchUrl);

		boolean flag = svnBranchConfigService.editBranch(projectCode, profile, id, branchUrl);
		
		if(flag){
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, BRANCHEDIT, SUCCESS, "分支编辑 保存SUCCESS");
			logger.info("************分支编辑 保存End**************");
			return SUCCESS;
		}else{
			operateLogService.addOperateLog(super.getUser().getUserName(), projectCode, profile, BRANCHEDIT, FAILURE, "分支编辑 保存FAILURE");
			logger.error("************分支编辑 保存Error**************");
			return FAILURE;
		}
	}
	
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/delete", method=RequestMethod.GET) 
	@ResponseBody
	public Object delete(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String profile, 
			@RequestParam("id") String id,
			HttpServletRequest request, HttpServletResponse response) {
		
		logger.info("************分支编辑　删除×××××××××××××××××××***" ) ;
		ReturnCodeEnum returnEnum = svnBranchConfigService.deleteBranch(projectCode, profile, id);
		
		if(returnEnum.equals(ReturnCodeEnum.OK_DELETEBRANCH)) {
			return SUCCESS;
		} else if(returnEnum.equals(ReturnCodeEnum.NO_DELETEBRANCH)) {
			return FAILURE;
		} else {
			//never reach
			return FAILURE;
		}
	}

}
