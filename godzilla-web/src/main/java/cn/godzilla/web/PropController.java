package cn.godzilla.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import cn.godzilla.common.StringUtil;
import cn.godzilla.model.Project;
import cn.godzilla.model.PropConfig;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.PropConfigService;
import cn.godzilla.service.UserService;


@Controller
@RequestMapping("/prop")
public class PropController extends SuperController implements Constant{
 
	private final Logger logger = LogManager.getLogger(PropController.class);
	@Autowired
	UserService userService;
	@Autowired
	PropConfigService propConfigService;
	@Autowired
	ProjectService projectService;
	/**
	 * 进入配置修改页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}", method=RequestMethod.GET)
	public Object updatePropPage(@PathVariable String sid, @PathVariable String projectCode,HttpServletRequest request) {

		logger.debug("*****PropController.updatePropPage*****");
		
		StringBuilder propTest = new StringBuilder("");
		StringBuilder propQuasiProduct = new StringBuilder("");
		StringBuilder propProduct = new StringBuilder("");
		
		propConfigService.findPropByUsername(projectCode, propTest, propQuasiProduct, propProduct);
		
		request.setAttribute("user", this.getUser());
		request.setAttribute("projectCode_", projectCode);
		request.setAttribute("propTest", propTest.toString());
		request.setAttribute("propQuasiProduct", propQuasiProduct.toString());
		request.setAttribute("propProduct", propProduct.toString());
		request.setAttribute("basePath", BASE_PATH);
		//request.setAttribute("sid", this.getSid());
		return "/query";
	}
	
	@RequestMapping(value="/{sid}/{projectCode}", method=RequestMethod.POST)
	@ResponseBody
	public Object updateProp(@PathVariable String sid, @PathVariable String projectCode,HttpServletRequest request) {
		
		String propTest = request.getParameter("p1");
		String propQuasiProduct = request.getParameter("p2");
		String propProduct = request.getParameter("p3");
		
		ReturnCodeEnum updateReturn = propConfigService.addOrUpdateProp(projectCode, propTest, propQuasiProduct, propProduct); 
		logger.info("****propTest:" + propTest);
		
		switch(updateReturn) {
		case OK_ADDUPDATEPROP:
			return SUCCESS;
		case NO_ADDUPDATEPROP:
			return FAILURE;
		}
		return FAILURE;
	}
	
	@RequestMapping(value="/{sid}/{projectCode}/queryProp")
	public Object queryProp(@PathVariable String sid, @PathVariable String projectCode, HttpServletRequest request) {
		
		String selectedProjectCode = StringUtil.getReqPrameter(request, "selectedProjectCode", "godzilla");
		String createBy = StringUtil.getReqPrameter(request, "createBy", this.getUser().getUserName());
		String selectedProfile = StringUtil.getReqPrameter(request, "selectedProfile", "");
		
		List<Project> projectList = projectService.queryAll();
		Map<String, String> profileList = propConfigService.queryAllProfile();
		List<PropConfig> propList = propConfigService.queryByProjectcodeAndCreatebyAndProfile(selectedProjectCode, createBy, selectedProfile, OK_VERIFY_STATUS);
				
		request.setAttribute("createBy", createBy);//提交人
		request.setAttribute("selectedProjectCode", selectedProjectCode);
		request.setAttribute("projectList", projectList);
		request.setAttribute("selectedProfile", selectedProfile);
		request.setAttribute("profileList", profileList);
		request.setAttribute("propList", propList);
		request.setAttribute("user", this.getUser());
		request.setAttribute("basePath", BASE_PATH);
		//request.setAttribute("sid", this.getSid());
		return "query02";
	}
	
	@RequestMapping(value="/{sid}/{projectCode}/verifyProp" , method=RequestMethod.GET) 
	public Object verifyPropListPage(@PathVariable String sid, @PathVariable String projectCode, HttpServletRequest request) {
		
		String selectedProjectCode = StringUtil.getReqPrameter(request, "selectedProjectCode", "godzilla");
		String createBy = StringUtil.getReqPrameter(request, "createBy", "");
		String selectedProfile = StringUtil.getReqPrameter(request, "selectedProfile", "");
		
		List<Project> projectList = projectService.queryAll();
		Map<String, String> profileList = propConfigService.queryAllProfile();
		List<PropConfig> propList = propConfigService.queryByProjectcodeAndCreatebyAndProfile(selectedProjectCode, createBy, selectedProfile, NOTYET_VERIFY_STATUS);
		
		request.setAttribute("createBy", createBy);//提交人
		request.setAttribute("selectedProjectCode", selectedProjectCode);
		request.setAttribute("projectList", projectList);
		request.setAttribute("selectedProfile", selectedProfile);
		request.setAttribute("profileList", profileList);
		request.setAttribute("propList", propList);
		request.setAttribute("user", this.getUser());
		request.setAttribute("basePath", BASE_PATH);
		
		return "config";
	}
	
	
	@RequestMapping(value="/{sid}/{createBy}/{projectCode}/{profile}/verifyProp" , method=RequestMethod.GET) 
	public Object verifyPropDetailPage(@PathVariable String sid, @PathVariable String projectCode, @PathVariable String propId, HttpServletRequest request) {
		
		List<PropConfig> propTestList = propConfigService.getPropConfigsByProjectcodeAndProfile(projectCode, TEST_PROFILE);
		List<PropConfig> propQuasiProductList = propConfigService.getPropConfigsByProjectcodeAndProfile(projectCode, QUASIPRODUCT_PROFILE);
		List<PropConfig> propProductList = propConfigService.getPropConfigsByProjectcodeAndProfile(projectCode, PRODUCT_PROFILE);
		
		
		request.setAttribute("user", this.getUser());
		request.setAttribute("projectCode_", projectCode);
		request.setAttribute("propTestList", propTestList);
		request.setAttribute("propQuasiProductList", propQuasiProductList);
		request.setAttribute("propProductList", propProductList);
		request.setAttribute("basePath", BASE_PATH);
		
		return "";
	}
	
	@RequestMapping(value="/{sid}/{createBy}/{projectCode}/{profile}/verifyProp" , method=RequestMethod.POST) 
	@ResponseBody
	public Object verifyProp(@PathVariable String sid, @PathVariable String createBy, @PathVariable String projectCode, @PathVariable String profile, HttpServletRequest request) {
		String status = StringUtil.getReqPrameter(request, "status", "0");
		String auditor_text = StringUtil.getReqPrameter(request, "auditor_text", "");
		
		ReturnCodeEnum updateReturn = propConfigService.verifyPropById(createBy, projectCode, profile, status, auditor_text); 
		switch(updateReturn) {
		case OK_VERIFYPROP:
			return SUCCESS;
		case NO_VERIFYPROP:
			return FAILURE;
		default:
			return FAILURE;	
		}
	}
		
		
}
