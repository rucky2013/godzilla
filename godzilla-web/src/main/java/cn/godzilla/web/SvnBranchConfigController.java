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
import cn.godzilla.model.ProjPrivate;
import cn.godzilla.model.SvnBranchConfig;
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
			@RequestParam("currentVersion") String currentVersion,
			HttpServletRequest request, HttpServletResponse response) {

		
		logger.info("**************添加分支设置***********projectCode:" + projectCode
				+ ",branchUrl:" + branchUrl);

		boolean flag = svnBranchConfigService.addNewBranch(projectCode, branchUrl, currentVersion);
		
		if(flag){
			logger.info("************添加分支设置End**************");
			return SUCCESS;
		}else{
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
			@RequestParam("currentVersion") String currentVersion,
			HttpServletRequest request, HttpServletResponse response) {

		
		logger.info("**************分支编辑 保存***********projectCode:" + projectCode
				+ ",branchUrl:" + branchUrl);

		boolean flag = svnBranchConfigService.editBranch(id, branchUrl, currentVersion);
		
		if(flag){
			logger.info("************分支编辑 保存End**************");
			return SUCCESS;
		}else{
			logger.error("************分支编辑 保存Error**************");
			return FAILURE;
		}
	}

}
