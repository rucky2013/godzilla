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

import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.common.response.ResponseBodyJson;
import cn.godzilla.model.Project;
import cn.godzilla.model.PropConfig;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.service.PropConfigService;
import cn.godzilla.service.UserService;

/**
 * 需要  prop表 建立 唯一约束   根据   project  createby profile key
 * @author 201407280166
 *
 */
@Component
@RequestMapping("/prop")
public class PropController extends GodzillaApplication implements Constant{
 
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
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/updateProp", method=RequestMethod.GET)
	public Object updatePropPage(@PathVariable String sid, @PathVariable String projectCode,HttpServletRequest request) {

		logger.debug("*****PropController.updatePropPage*****");
		
		StringBuilder propTest = new StringBuilder("");
		StringBuilder propQuasiProduct = new StringBuilder("");
		StringBuilder propProduct = new StringBuilder("");
		
		propConfigService.findPropByProjectCode(projectCode, propTest, propQuasiProduct, propProduct);
		
		request.setAttribute("user", this.getUser());
		request.setAttribute("propTest", this.replaceHtml(propTest.toString()));
		request.setAttribute("propQuasiProduct", this.replaceHtml(propQuasiProduct.toString()));
		request.setAttribute("propProduct", this.replaceHtml(propProduct.toString()));
		request.setAttribute("basePath", BASE_PATH);
		return "/query";
	}
	
	/**
	 * 提交 申请 审核配置
	 * @param sid
	 * @param projectCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/updateProp", method=RequestMethod.POST)
	@ResponseBody
	public Object updateProp(@PathVariable String sid, @PathVariable String projectCode,HttpServletRequest request) {
		
		logger.debug("*****PropController.updateProp*****");
		
		String propTest = StringUtil.getReqPrameter(request, "p1");
		String propQuasiProduct = StringUtil.getReqPrameter(request, "p2");
		String propProduct = StringUtil.getReqPrameter(request, "p3");
		
		ReturnCodeEnum updateReturn = propConfigService.addNotVerifyProp(projectCode, propTest, propQuasiProduct, propProduct); 
		
		return ResponseBodyJson.custom().setAll(updateReturn, UPDATEPROP).build();
	}
	/**
	 * 查看  配置 页面
	 * @param sid
	 * @param projectCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/queryProp")
	public Object queryPropPage(@PathVariable String sid, @PathVariable String projectCode, HttpServletRequest request) {
		
		//String selectedProjectCode = StringUtil.getReqPrameter(request, "selectedProjectCode", "godzilla");
		String createBy = StringUtil.getReqPrameter(request, "createBy", "");
		String selectedProfile = StringUtil.getReqPrameter(request, "selectedProfile", TEST_PROFILE);
		
		List<Project> projectList = projectService.queryAll();
		Map<String, String> profileList = propConfigService.queryAllProfile();
		//List<PropConfig> propList = propConfigService.queryByProjectcodeAndCreatebyAndProfileAndStatus(selectedProjectCode, createBy, selectedProfile, OK_VERIFY_STATUS);
		List<PropConfig> propList = propConfigService.queryByProjectcodeAndCreatebyAndProfileAndStatus(projectCode, createBy, selectedProfile, OK_VERIFY_STATUS);
		
		request.setAttribute("createBy", createBy);//提交人
		//request.setAttribute("selectedProjectCode", selectedProjectCode);
		request.setAttribute("projectList", projectList);
		request.setAttribute("selectedProfile", selectedProfile);
		request.setAttribute("profileList", profileList);
		request.setAttribute("propList", this.replaceHtml(propList));
		request.setAttribute("user", this.getUser());
		request.setAttribute("basePath", BASE_PATH);
		return "query02";
	}
	
	/**
	 * 配置排序 页面
	 * @param sid
	 * @param projectCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/propSort", method=RequestMethod.GET)
	public Object propSortPage(@PathVariable String sid, @PathVariable String projectCode, HttpServletRequest request) {
		
		//String selectedProjectCode = StringUtil.getReqPrameter(request, "selectedProjectCode", "godzilla");
		String createBy = StringUtil.getReqPrameter(request, "createBy", "");
		String selectedProfile = StringUtil.getReqPrameter(request, "selectedProfile", PROFILE_TEST);
		
		List<Project> projectList = projectService.queryAll();
		Map<String, String> profileList = propConfigService.queryAllProfile();
		//List<PropConfig> propList = propConfigService.queryByProjectcodeAndCreatebyAndProfileAndStatus(selectedProjectCode, createBy, selectedProfile, OK_VERIFY_STATUS);
		List<PropConfig> propList = propConfigService.queryByProjectcodeAndCreatebyAndProfileAndStatus(projectCode, createBy, selectedProfile, OK_VERIFY_STATUS);
		
		request.setAttribute("createBy", createBy);//提交人
		//request.setAttribute("selectedProjectCode", selectedProjectCode);
		request.setAttribute("projectList", projectList);
		request.setAttribute("selectedProfile", selectedProfile);
		request.setAttribute("profileList", profileList);
		request.setAttribute("propList", this.replaceHtml(propList));
		request.setAttribute("user", this.getUser());
		request.setAttribute("basePath", BASE_PATH);
		return "propSort";
	}
	
	/**
	 * 提交排序 结果
	 * @param sid
	 * @param projectCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{sid}/{projectCode}/propSort", method=RequestMethod.POST)
	@ResponseBody
	public Object propSort(@PathVariable String sid, @PathVariable String projectCode, HttpServletRequest request) {
		
		//String selectedProjectCode = StringUtil.getReqPrameter(request, "selectedProjectCode", "godzilla");
		String sortJson = StringUtil.getReqPrameter(request, "sortJson", "");
		
		ReturnCodeEnum returnenum = propConfigService.resortPropById(sortJson);
		
		if(returnenum == ReturnCodeEnum.OK_SORTPROP){
			return SUCCESS;
		} else {
			return FAILURE;
		}
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
	@RequestMapping(value="/{sid}/{projectCode}/verifyProp" , method=RequestMethod.GET) 
	public Object verifyPropListPage(@PathVariable String sid, @PathVariable String projectCode, HttpServletRequest request) {
		
		//String selectedProjectCode = StringUtil.getReqPrameter(request, "selectedProjectCode", "godzilla");
		String createBy = StringUtil.getReqPrameter(request, "createBy", "");
		String selectedProfile = StringUtil.getReqPrameter(request, "selectedProfile", "");
		
		List<Project> projectList = projectService.queryAll();
		Map<String, String> profileList = propConfigService.queryAllProfile();
		//List<PropConfig> propList = propConfigService.queryByProjectcodeAndCreatebyAndProfileGroupBy(selectedProjectCode, createBy, selectedProfile, NOTYET_VERIFY_STATUS);
		List<PropConfig> propList = propConfigService.queryByProjectcodeAndCreatebyAndProfileGroupBy(projectCode, createBy, selectedProfile, NOTYET_VERIFY_STATUS);
		
		request.setAttribute("createBy", createBy);//提交人
		//request.setAttribute("selectedProjectCode", selectedProjectCode);
		request.setAttribute("projectList", projectList);
		request.setAttribute("selectedProfile", selectedProfile);
		request.setAttribute("profileList", profileList);
		request.setAttribute("propList", this.replaceHtml(propList));
		request.setAttribute("user", this.getUser());
		request.setAttribute("basePath", BASE_PATH);
		
		return "config";
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
	@RequestMapping(value="/{sid}/{projectCode}/{createBy}/{profile}/verifyProp" , method=RequestMethod.GET) 
	public Object verifyPropDetailPage(@PathVariable String sid, @PathVariable String createBy, @PathVariable String projectCode, @PathVariable String profile, HttpServletRequest request) {
		
		/*List<PropConfig> propTestList = propConfigService.getPropConfigsByProjectcodeAndProfile(projectCode, TEST_PROFILE);
		List<PropConfig> propQuasiProductList = propConfigService.getPropConfigsByProjectcodeAndProfile(projectCode, QUASIPRODUCT_PROFILE);
		List<PropConfig> propProductList = propConfigService.getPropConfigsByProjectcodeAndProfile(projectCode, PRODUCT_PROFILE);
		
		request.setAttribute("user", this.getUser());
		request.setAttribute("projectCode_", projectCode);
		request.setAttribute("propTestList", propTestList);
		request.setAttribute("propQuasiProductList", propQuasiProductList);
		request.setAttribute("propProductList", propProductList);*/
		
		StringBuilder propTest = new StringBuilder("");
		StringBuilder propQuasiProduct = new StringBuilder("");
		StringBuilder propProduct = new StringBuilder("");
		propConfigService.findPropByCreatebyAndProjectcodeAndProfileAndStatus(createBy, projectCode, profile, propTest, propQuasiProduct, propProduct, NOTYET_VERIFY_STATUS);
		
		StringBuilder oldpropTest = new StringBuilder("");
		StringBuilder oldpropQuasiProduct = new StringBuilder("");
		StringBuilder oldpropProduct = new StringBuilder("");
		propConfigService.findPropByCreatebyAndProjectcodeAndProfileAndStatus(createBy, projectCode, profile, oldpropTest, oldpropQuasiProduct, oldpropProduct, OK_VERIFY_STATUS);
		
		request.setAttribute("user", this.getUser());
		
		request.setAttribute("propTest", this.replaceHtml(propTest.toString()));
		request.setAttribute("propQuasiProduct", this.replaceHtml(propQuasiProduct.toString()));
		request.setAttribute("propProduct", this.replaceHtml(propProduct.toString()));
		
		request.setAttribute("oldpropTest", this.replaceHtml(oldpropTest.toString()));
		request.setAttribute("oldpropQuasiProduct", this.replaceHtml(oldpropQuasiProduct.toString()));
		request.setAttribute("oldpropProduct", this.replaceHtml(oldpropProduct.toString()));
		
		request.setAttribute("basePath", BASE_PATH);
		
		return "query_textarea";
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
	@RequestMapping(value="/{sid}/{projectCode}/{createBy}/{profile}/verifyProp" , method=RequestMethod.POST) 
	@ResponseBody
	public Object verifyProp(@PathVariable String sid, @PathVariable String createBy, @PathVariable String projectCode, @PathVariable String profile, HttpServletRequest request) {
		String status = StringUtil.getReqPrameter(request, "status", "0");
		String auditor_text = StringUtil.getReqPrameter(request, "auditor_text", "");
		
		ReturnCodeEnum updateReturn = propConfigService.verifyPropByCreatebyAndProjectcodeAndProfile(createBy, projectCode, profile, status, auditor_text); 
		
		return ResponseBodyJson.custom().setAll(updateReturn, VERIFYPROP).build();
	}
		
		
}
