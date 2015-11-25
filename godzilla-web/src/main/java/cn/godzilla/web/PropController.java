package cn.godzilla.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.common.response.ResponseBodyJson;
import cn.godzilla.model.Project;
import cn.godzilla.model.PropBill;
import cn.godzilla.model.PropConfig;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.PropConfigService;
import cn.godzilla.service.UserService;
import cn.godzilla.util.GodzillaWebApplication;

/**
 * 需要  prop表 建立 唯一约束   根据   project  createby profile key
 *
 */
@Component
@RequestMapping("/prop")
public class PropController extends GodzillaWebApplication {
 
	private final Logger logger = LogManager.getLogger(PropController.class);
	@Autowired
	UserService userService;
	@Autowired
	PropConfigService propConfigService;
	@Autowired
	ProjectService projectService;
	@Autowired
	OperateLogService operateLogService;
	/**
	 * 进入配置修改页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/updateProp", method=RequestMethod.GET)
	public Object updatePropPage(@PathVariable String sid, @PathVariable String projectCode,HttpServletRequest request) {

		logger.debug("*****PropController.updatePropPage*****");
		
		StringBuilder propTest = new StringBuilder("");
		StringBuilder propQuasiProduct = new StringBuilder("");
		StringBuilder propProduct = new StringBuilder("");
		
		propConfigService.findPropByProjectCode(projectCode, TEST_PROFILE, propTest, propQuasiProduct, propProduct);
		
		request.setAttribute("user", GodzillaWebApplication.getUser());
		request.setAttribute("propTest", this.replaceHtml(propTest.toString()));
		request.setAttribute("propQuasiProduct", this.replaceHtml(propQuasiProduct.toString()));
		request.setAttribute("propProduct", this.replaceHtml(propProduct.toString()));
		request.setAttribute("basePath", BASE_PATH);
		return "/updatePropPage";
	}
	
	/**
	 * 提交 申请 审核配置
	 * @param sid
	 * @param projectCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/updateProp", method=RequestMethod.POST)
	@ResponseBody
	public Object updateProp(@PathVariable String sid, @PathVariable String projectCode,HttpServletRequest request) {
		
		logger.debug("*****PropController.updateProp*****");
		
		String propTest = StringUtil.getReqPrameter(request, "p1");
		String propQuasiProduct = StringUtil.getReqPrameter(request, "p2");
		String propProduct = StringUtil.getReqPrameter(request, "p3");
		
		ReturnCodeEnum updateReturn = propConfigService.addNotVerifyProp(projectCode, TEST_PROFILE, propTest, propQuasiProduct, propProduct); 
		
		return ResponseBodyJson.custom().setAll(updateReturn, UPDATEPROP).build().log();
	}
	/**
	 * 查询 配置 页面
	 * @param sid
	 * @param projectCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/queryProp")
	public Object queryPropPage(@PathVariable String sid, @PathVariable String projectCode, HttpServletRequest request) {
		
		//String selectedProjectCode = StringUtil.getReqPrameter(request, "selectedProjectCode", "godzilla");
		String createBy = StringUtil.getReqPrameter(request, "createBy", "");
		String selectedProfile = StringUtil.getReqPrameter(request, "selectedProfile", TEST_PROFILE);
		
		List<Project> projectList = projectService.queryAll(projectCode, TEST_PROFILE);
		Map<String, String> profileList = propConfigService.queryAllProfile(projectCode, TEST_PROFILE);
		//List<PropConfig> propList = propConfigService.queryByProjectcodeAndCreatebyAndProfileAndStatus(selectedProjectCode, createBy, selectedProfile, OK_VERIFY_STATUS);
		List<PropConfig> propList = propConfigService.queryByProjectcodeAndCreatebyAndProfileAndStatus(projectCode, TEST_PROFILE, selectedProfile, createBy, OK_VERIFY_STATUS);
		
		request.setAttribute("createBy", createBy);//提交人
		//request.setAttribute("selectedProjectCode", selectedProjectCode);
		request.setAttribute("projectList", projectList);
		request.setAttribute("selectedProfile", selectedProfile);
		request.setAttribute("profileList", profileList);
		request.setAttribute("propList", this.replaceHtml(propList));
		request.setAttribute("user", getUser());
		request.setAttribute("basePath", BASE_PATH);
		return "queryPropPage";
	}
	
	/**
	 * 配置排序 页面
	 * @param sid
	 * @param projectCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/propSort", method=RequestMethod.GET)
	public Object propSortPage(@PathVariable String sid, @PathVariable String projectCode, HttpServletRequest request) {
		
		//String selectedProjectCode = StringUtil.getReqPrameter(request, "selectedProjectCode", "godzilla");
		String createBy = StringUtil.getReqPrameter(request, "createBy", "");
		String selectedProfile = StringUtil.getReqPrameter(request, "selectedProfile", TEST_PROFILE);
		
		List<Project> projectList = projectService.queryAll(projectCode, TEST_PROFILE);
		Map<String, String> profileList = propConfigService.queryAllProfile(projectCode, TEST_PROFILE);
		//List<PropConfig> propList = propConfigService.queryByProjectcodeAndCreatebyAndProfileAndStatus(selectedProjectCode, createBy, selectedProfile, OK_VERIFY_STATUS);
		List<PropConfig> propList = propConfigService.queryByProjectcodeAndCreatebyAndProfileAndStatus(projectCode, TEST_PROFILE, selectedProfile, createBy, OK_VERIFY_STATUS);
		
		request.setAttribute("createBy", createBy);//提交人
		//request.setAttribute("selectedProjectCode", selectedProjectCode);
		request.setAttribute("projectList", projectList);
		request.setAttribute("selectedProfile", selectedProfile);
		request.setAttribute("profileList", profileList);
		request.setAttribute("propList", this.replaceHtml(propList));
		request.setAttribute("user", getUser());
		request.setAttribute("basePath", BASE_PATH);
		return "propSortPage";
	}
	
	/**
	 * 提交排序 结果
	 * @param sid
	 * @param projectCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/propSort", method=RequestMethod.POST)
	@ResponseBody
	public Object propSort(@PathVariable String sid, @PathVariable String projectCode, HttpServletRequest request) {
		
		//String selectedProjectCode = StringUtil.getReqPrameter(request, "selectedProjectCode", "godzilla");
		String sortJson = StringUtil.getReqPrameter(request, "sortJson", "");
		
		ReturnCodeEnum returnenum = propConfigService.resortPropById(projectCode, TEST_PROFILE, sortJson);
		
		return ResponseBodyJson.custom().setAll(returnenum, SORTPROP).build().log();
	}
	
	/**
	 * 将返回json串中de特殊字符替换 
	 * 使之在html页面正常显示后台数据
	 * @param propList
	 * @return
	 */
	private List<PropConfig> replaceHtml(List<PropConfig> propList) {
		for(PropConfig prop: propList) {
			prop.setProValue(this.replaceHtml(prop.getProValue()));
		}
		return propList;
	}
	/**
	 * 将返回json串中de特殊字符替换 
	 * 使之在html页面正常显示后台数据
	 * @param String
	 * @return
	 */
	private String replaceHtml(String string) {
		return string.replace("<", "&lt;").replace(">", "&gt;").replace("'", "&#39;");
	}

	/**
	 * 进入审核 列表页
	 * @param sid
	 * @param projectCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/verifyProp" , method=RequestMethod.GET) 
	public Object verifyPropListPage(@PathVariable String sid, @PathVariable String projectCode, HttpServletRequest request) {
		
		String createBy = StringUtil.getReqPrameter(request, "createBy", "");
		String selectedProfile = StringUtil.getReqPrameter(request, "selectedProfile", "");
		
		List<Project> projectList = projectService.queryAll(projectCode, TEST_PROFILE);
		Map<String, String> profileList = propConfigService.queryAllProfile(projectCode, TEST_PROFILE);
		//List<PropConfig> propList = propConfigService.queryByProjectcodeAndCreatebyAndProfileGroupBy(projectCode, createBy, selectedProfile, NOTYET_VERIFY_STATUS);
		List<PropBill> propBillList = propConfigService.queryAllPropBill(projectCode, TEST_PROFILE);
		
		request.setAttribute("createBy", createBy);//提交人
		request.setAttribute("projectList", projectList);
		request.setAttribute("selectedProfile", selectedProfile);
		request.setAttribute("profileList", profileList);
		request.setAttribute("propList", propBillList);
		request.setAttribute("user", getUser());
		request.setAttribute("basePath", BASE_PATH);
		
		return "verifyPropListPage";
	}
	
	/**
	 * 配置审核页面
	 * 查询出  所有 添加的配置  （未审核） 和所有 审核的配置
	 * web页面进行对比
	 * @param sid
	 * @param projectCode
	 * @param propId
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/{createBy}/{billId}/verifyProp" , method=RequestMethod.GET) 
	public Object verifyPropDetailPage(@PathVariable String sid, @PathVariable String createBy, @PathVariable String projectCode, @PathVariable String profile, @PathVariable Long billId, HttpServletRequest request) {
		
		StringBuilder propTest = new StringBuilder("");
		StringBuilder propQuasiProduct = new StringBuilder("");
		StringBuilder propProduct = new StringBuilder("");
		Map<String, StringBuilder> propStrings = propConfigService.findPropByCreatebyAndProjectcodeAndProfileAndStatus(projectCode, profile, createBy, propTest, propQuasiProduct, propProduct, NOTYET_VERIFY_STATUS, billId);
		propTest = propStrings.get("propTest");
		propQuasiProduct = propStrings.get("propQuasiProduct");
		propProduct = propStrings.get("propProduct");
		
		StringBuilder oldpropTest = new StringBuilder("");
		StringBuilder oldpropQuasiProduct = new StringBuilder("");
		StringBuilder oldpropProduct = new StringBuilder("");
		Map<String, StringBuilder> oldpropStrings = propConfigService.findPropByCreatebyAndProjectcodeAndProfileAndStatus(projectCode, profile, createBy, oldpropTest, oldpropQuasiProduct, oldpropProduct, OK_VERIFY_STATUS, billId);
		oldpropTest = oldpropStrings.get("propTest");
		oldpropQuasiProduct = oldpropStrings.get("propQuasiProduct");
		oldpropProduct = oldpropStrings.get("propProduct");
		
		request.setAttribute("user", getUser());
		
		request.setAttribute("propTest", this.replaceHtml(propTest.toString()));
		request.setAttribute("propQuasiProduct", this.replaceHtml(propQuasiProduct.toString()));
		request.setAttribute("propProduct", this.replaceHtml(propProduct.toString()));
		
		request.setAttribute("oldpropTest", this.replaceHtml(oldpropTest.toString()));
		request.setAttribute("oldpropQuasiProduct", this.replaceHtml(oldpropQuasiProduct.toString()));
		request.setAttribute("oldpropProduct", this.replaceHtml(oldpropProduct.toString()));
		
		request.setAttribute("basePath", BASE_PATH);
		
		return "verifyPropDetailPage";
	}
	
	
	/**
	 * 审核
	 * @param sid
	 * @param createBy
	 * @param projectCode
	 * @param profile
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/{profile}/{createBy}/{billId}/verifyProp" , method=RequestMethod.POST) 
	@ResponseBody
	public Object verifyProp(@PathVariable String sid, @PathVariable String createBy, @PathVariable String projectCode, @PathVariable String profile, @PathVariable Long billId, HttpServletRequest request) {
		String status = StringUtil.getReqPrameter(request, "status", "0");
		String auditor_text = StringUtil.getReqPrameter(request, "auditor_text", "");
		
		ReturnCodeEnum updateReturn = propConfigService.verifyPropByCreatebyAndProjectcodeAndALLProfile(projectCode, profile, createBy, status, auditor_text, billId); 
		
		return ResponseBodyJson.custom().setAll(updateReturn, VERIFYPROP).build().log();
	}
		
		
}
