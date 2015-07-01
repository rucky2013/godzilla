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
@RequestMapping("setting")
public class SvnBranchConfigController {

	private final Logger logger = LogManager.getLogger(SvnBranchConfigController.class);

	@Autowired
	SvnBranchConfigService svnBranchConfigService;

	@Autowired
	ProjPrivateService projPrivateService;

	/**
	 * 根据项目编号projectCode，查询分支列表
	 * 
	 * @param projectCode
	 *            项目编号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "branch/list/{projectCode}", method = RequestMethod.GET)
	@ResponseBody
	public List<SvnBranchConfig> getBranchs(
			@PathVariable("projectCode") String projectCode,
			HttpServletRequest request, HttpServletResponse response) {

		logger.info("***********查询分支列表********projectCode:" + projectCode);
		return svnBranchConfigService.queryListByProjectCode(projectCode);
	}

	/**
	 * 设置私有工作台，选择分支 save to t_g_project_private
	 * 
	 * @param projectCode
	 *            项目编码
	 * @param userName
	 *            用户名
	 * @param currentBranchUrl
	 *            当前分支
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "branch/selected", method = RequestMethod.PUT)
	@ResponseBody
	public ProjPrivate selectBranch(
			@RequestParam("projectCode") String projectCode,
			@RequestParam("userName") String userName,
			@RequestParam("currentBranchUrl") String currentBranchUrl,
			HttpServletRequest request, HttpServletResponse response) {

		logger.info("**************操作分支设置***********projectCode:" + projectCode
				+ ",userName:" + userName + ",currentBranchUrl:"
				+ currentBranchUrl);

		ProjPrivate info = new ProjPrivate();

		info.setProjectCode(projectCode);
		info.setUserName(userName);
		info.setCurrentBranchUrl(currentBranchUrl);

		return projPrivateService.save(info);

	}
	
	@RequestMapping(value = "branch/add", method = RequestMethod.POST)
	@ResponseBody
	public boolean addBranch(
			@RequestParam("projectCode") String projectCode,
			@RequestParam("branchUrl") String branchUrl,
			@RequestParam("branchName") String branchName,
			HttpServletRequest request, HttpServletResponse response) {

		logger.info("**************操作分支设置***********projectCode:" + projectCode
				+ ",branchUrl:" + branchUrl + ",branchName:"
				+ branchName);

		SvnBranchConfig svnBranchConfig = new SvnBranchConfig();

		svnBranchConfig.setBranchName(branchName);
		svnBranchConfig.setBranchUrl(branchUrl);
		svnBranchConfig.setProjectCode(projectCode);

		if (svnBranchConfigService.insert(svnBranchConfig) > 0){
			
			return true ;
		}
		return false;

	}

	/**
	 * 关闭虚拟主干
	 * 
	 * @param projectCode
	 * @param userName
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "private/{projectCode}/{userName}/off", method = RequestMethod.PUT)
	@ResponseBody
	public ProjPrivate virtualOff(
			@PathVariable("projectCode") String projectCode,
			@PathVariable("userName") String userName,
			HttpServletRequest request, HttpServletResponse response) {

		logger.info("**************关闭虚拟主干***********projectCode:" + projectCode
				+ ",userName:" + userName);

		ProjPrivate info = new ProjPrivate();

		info.setProjectCode(projectCode);
		info.setUserName(userName);
		info.setIfVirtual(Constant.FALSE);

		return projPrivateService.save(info);

	}

	/**
	 * 启用虚拟主干
	 * 
	 * @param projectCode
	 * @param userName
	 * @param virtualTruckUrl
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "private/virtualon", method = RequestMethod.PUT)
	@ResponseBody
	public ProjPrivate virtualOn(
			@RequestParam("projectCode") String projectCode,
			@RequestParam("userName") String userName,
			@RequestParam("virtualTruckUrl") String virtualTruckUrl,
			HttpServletRequest request, HttpServletResponse response) {

		logger.info("**************启用虚拟主干***********projectCode:" + projectCode
				+ ",userName:" + userName + ",virtualTruckUrl:"
				+ virtualTruckUrl);

		ProjPrivate info = new ProjPrivate();

		info.setProjectCode(projectCode);
		info.setUserName(userName);
		info.setIfVirtual(Constant.TRUE);
		info.setVirtualTruckUrl(virtualTruckUrl);

		return projPrivateService.save(info);

	}

	/**
	 * 查看私有配置 是否启用虚拟主干等
	 * 
	 * @param projectCode
	 *            项目编号
	 * @param userName
	 *            用户名
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "private/{projectCode}/{userName}", method = RequestMethod.GET)
	@ResponseBody
	public ProjPrivate getProjPrivate(
			@PathVariable("projectCode") String projectCode,
			@PathVariable("userName") String userName,
			HttpServletRequest request, HttpServletResponse response) {

		logger.info("**************查看私有配置***********projectCode:" + projectCode
				+ ",userName:" + userName);

		return projPrivateService.queryDetail(projectCode, userName);
	}

}
