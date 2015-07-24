package cn.godzilla.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.common.xml.XmlUtil;
import cn.godzilla.dao.PropConfigMapper;
import cn.godzilla.model.PropConfig;
import cn.godzilla.model.RpcResult;
import cn.godzilla.service.PropConfigService;
import cn.godzilla.web.SuperController;

import com.alibaba.fastjson.JSON;

@Service("propConfigService")
public class PropConfigServiceImpl implements PropConfigService {

	@Autowired
	private PropConfigMapper dao;
	
	@Override
	public int insert(PropConfig record) {

		return dao.insert(record);
	}

	@Override
	public int insertSelective(PropConfig record) {

		return dao.insertSelective(record);
	}

	@Override
	public int update(PropConfig record) {

		return dao.update(record);
	}

	@Override
	public PropConfig queryDetailById(long id) {

		return dao.queryDetailById(id);
	}

	@Override
	public PropConfig queryDetailByKey(Map<String, String> map) {

		return dao.queryDetailByKey(map);
	}

	@Override
	public List<PropConfig> queryList(Map<String, String> map) {
		
		return dao.queryList(map);
	}

	
	@Override
	public RpcResult propToPom(String project_code, String branchname, String profile) throws DocumentException, IOException,  Exception {
		/**
		 * 1.get pom.xml path
		 */
		String[] args = new String[1];
		args[0] = branchname;
		String parentPomPath = StringUtil.getPath("parentPom",args);
		String webPomPath = StringUtil.getPath("webPom",args);
		
		try {
			/**
			 * 2.get propconfigs from DB
			 */
			String parentVersion = getParentversionByProjectcodeAndProfile(project_code, profile);
			List<PropConfig> propconfigs = getPropConfigsByProjectcodeAndProfile(project_code, profile);
			/**
			 * 3.save propconfigs cover pom.xml
			 */
			XmlUtil.coverParentPom(parentVersion, parentPomPath, StringUtil.getPath("parentPom1",args));
			XmlUtil.coverWebPom(propconfigs, webPomPath, StringUtil.getPath("webPom1",args));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return RpcResult.create(SUCCESS);
	}
	/**
	 * 获取项目父pom的  parent.version 
	 * @param project_code
	 * @param profile
	 * @return
	 */
	private String getParentversionByProjectcodeAndProfile(String project_code, String profile) {
		
		return "0.0.1-SNAPSHOT11";
	}
	/**
	 * 获取项目的  所有审核配置项 
	 * @param project_code
	 * @param profile
	 * @return list
	 */
	private List<PropConfig> getPropConfigsByProjectcodeAndProfile(String project_code, String profile) {
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("projectCode", project_code);
		parameters.put("profile", profile);
		return dao.queryListByProjectcodeAndProfile(parameters);
	}
	
	/**
	 * 获取项目的  所有审核配置项 
	 * @param project_code
	 * @param profile
	 * @return map
	 */
	private Map<String, String> getPropMapByProjectcodeAndProfile(String projectCode, String profile) {
		
		List<PropConfig> propConfigList = getPropConfigsByProjectcodeAndProfile(projectCode, profile);
		Map<String, String> propMap = new HashMap<String, String>();
		
		for(PropConfig tempProp : propConfigList) {
			propMap.put(tempProp.getProKey(), tempProp.getProValue());
		}
		return propMap;
	}
	
	@Override
	public void findPropByUsername(String projectCode, StringBuilder propTest, StringBuilder propQuasiProduct, StringBuilder propProduct) {
		
		Map<String, String> propTestMap = new HashMap<String, String>();
		Map<String, String> propQuasiProductMap = new HashMap<String, String>();
		Map<String, String> propProductMap = new HashMap<String, String>();
		
		propTestMap = getPropMapByProjectcodeAndProfile(projectCode, TEST_PROFILE);
		propQuasiProductMap = getPropMapByProjectcodeAndProfile(projectCode, QUASIPRODUCT_PROFILE);
		propProductMap = getPropMapByProjectcodeAndProfile(projectCode, PRODUCT_PROFILE);
		
		propTest.append(JSON.toJSONString(propTestMap));
		propQuasiProduct.append(JSON.toJSONString(propQuasiProductMap));
		propProduct.append(JSON.toJSONString(propProductMap));
	}

	@Override
	public ReturnCodeEnum addOrUpdateProp(String projectCode, String propTest, String propQuasiProduct, String propProduct) {
		
		Map<String, String> requestPropTest = JSON.parseObject(propTest, Map.class);
		Map<String, String> requestPropQuasiProduct = JSON.parseObject(propQuasiProduct, Map.class);
		Map<String, String> requestPropProduct = JSON.parseObject(propProduct, Map.class);
		
		Map<String, String> dbPropTestMap = getPropMapByProjectcodeAndProfile(projectCode, TEST_PROFILE);
		Map<String, String> dbPropQuasiProductMap = getPropMapByProjectcodeAndProfile(projectCode, QUASIPRODUCT_PROFILE);
		Map<String, String> dbPropProductMap = getPropMapByProjectcodeAndProfile(projectCode, PRODUCT_PROFILE);
		
		boolean fg1 = compare2AddOrUpdate(requestPropTest, dbPropTestMap, projectCode, TEST_PROFILE);
		boolean fg2 = compare2AddOrUpdate(requestPropQuasiProduct, dbPropQuasiProductMap, projectCode, QUASIPRODUCT_PROFILE);
		boolean fg3 = compare2AddOrUpdate(requestPropProduct, dbPropProductMap, projectCode, PRODUCT_PROFILE);
		
		return (fg1 && fg2 && fg3) ? ReturnCodeEnum.getByReturnCode(OK_ADDUPDATEPROP):ReturnCodeEnum.getByReturnCode(NO_ADDUPDATEPROP);
	}

	/**
	 * 判断  修改配置key  是否存在  数据库  
	 * 
	 * if true   update  
	 * if false  insert
	 * @param requestPropTest
	 * @param dbPropTestMap
	 * @param projectCode
	 * @param testProfile
	 * @return
	 */
	private boolean compare2AddOrUpdate(Map<String, String> requestProp, Map<String, String> dbPropMap, String projectCode, String profile) {
		
		Set<String> requestKeys = requestProp.keySet();
		for(String requestKey: requestKeys) {
			if(dbPropMap.containsKey(requestKey)) {
				Map<String, Object> parameterMap = new HashMap<String, Object>();
				parameterMap.put("project_code", projectCode);
				parameterMap.put("profile", profile);
				parameterMap.put("pro_key", requestKey);
				parameterMap.put("pro_value", requestProp.get(requestKey));
				parameterMap.put("pro_value_old", dbPropMap.get(requestKey));
				
				dao.updatePropByProkey(parameterMap);
			} else {
				PropConfig prop = new PropConfig();
				prop.setProjectCode(projectCode);
				prop.setProfile(profile);
				prop.setProKey(requestKey);
				prop.setProValue(requestProp.get(requestKey));
				
				prop.setRemark("");
				prop.setCreateBy(SuperController.getUser().getUserName());
				prop.setCreateTime(new Date());
				prop.setUpdateTime(new Date());
				prop.setLastValue("");
				prop.setStatus(0);//待审核
				prop.setAuditor("");
				prop.setAuditorText("");
				
				dao.insert(prop);
			}
		}
		return true;
	}

}
