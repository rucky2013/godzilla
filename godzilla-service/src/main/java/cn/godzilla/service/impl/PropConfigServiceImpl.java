package cn.godzilla.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public void findPropByProjectCode(String projectCode, StringBuilder propTest, StringBuilder propQuasiProduct, StringBuilder propProduct) {
		
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
	/**
	 * 获取项目的  所有审核配置项 
	 * 根据 projectcode 和 profile
	 * 返回map
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
	/**
	 * 获取项目的  所有审核配置项 
	 * 根据 projectcode 和 profile
	 * 返回list
	 * @param project_code
	 * @param profile
	 * @return list
	 */
	@Override
	public List<PropConfig> getPropConfigsByProjectcodeAndProfile(String project_code, String profile) {
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("projectCode", project_code);
		parameters.put("profile", profile);
		return dao.queryListByProjectcodeAndProfile(parameters);
	}
	

	@Override
	public ReturnCodeEnum addNotVerifyProp(String projectCode, String propTest, String propQuasiProduct, String propProduct) {
		
		Map<String, String> requestPropTest = JSON.parseObject(propTest, Map.class);
		Map<String, String> requestPropQuasiProduct = JSON.parseObject(propQuasiProduct, Map.class);
		Map<String, String> requestPropProduct = JSON.parseObject(propProduct, Map.class);
		
		/*Map<String, String> dbPropTestMap = getPropMapByProjectcodeAndProfile(projectCode, TEST_PROFILE);
		Map<String, String> dbPropQuasiProductMap = getPropMapByProjectcodeAndProfile(projectCode, QUASIPRODUCT_PROFILE);
		Map<String, String> dbPropProductMap = getPropMapByProjectcodeAndProfile(projectCode, PRODUCT_PROFILE);
		*/
		boolean fg1 = addProp(requestPropTest, projectCode, TEST_PROFILE, 0);
		boolean fg2 = addProp(requestPropQuasiProduct, projectCode, QUASIPRODUCT_PROFILE, 0);
		boolean fg3 = addProp(requestPropProduct, projectCode, PRODUCT_PROFILE, 0);
		
		return (fg1 && fg2 && fg3) ? ReturnCodeEnum.getByReturnCode(OK_ADDUPDATEPROP):ReturnCodeEnum.getByReturnCode(NO_ADDUPDATEPROP);
	}
	
	/**
	 * 添加 配置
	 * @param requestProp
	 * @param projectCode
	 * @param profile
	 * @param status
	 * @return
	 */
	private boolean addProp(Map<String, String> requestProp, String projectCode, String profile, int status) {
		Set<String> requestKeys = requestProp.keySet();
		for(String requestKey: requestKeys) {
			PropConfig prop = new PropConfig();
			prop.setProjectCode(projectCode);
			prop.setProfile(profile);
			prop.setProKey(requestKey);
			prop.setProValue(requestProp.get(requestKey));
			
			prop.setRemark("");
			prop.setCreateBy(SuperController.getUser().getUserName());
			prop.setCreateTime(new Date());
			prop.setUpdateTime(new Date());
			prop.setLastValue("");//多人修改时，一个人通过会影响其他人的 旧值，所以不设置
			prop.setStatus(status);//0未审核 
			prop.setAuditor("");
			prop.setAuditorText("");
			
			dao.insert(prop);
		}
		return true;
	}

	@Override
	public Map<String, String> queryAllProfile() {
		Map<String, String> profileMap = new HashMap<String, String>();
		profileMap.put("ALL","");
		profileMap.put("TEST","TEST");
		profileMap.put("PRODUCT","PRODUCT");
		profileMap.put("QUASIPRODUCT","QUASIPRODUCT");
		return profileMap;
	}

	@Override
	public List<PropConfig> queryByProjectcodeAndCreatebyAndProfileAndStatus(String projectCode, String createBy, String profile, String status) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("project_code", projectCode);
		parameterMap.put("profile", profile);
		parameterMap.put("create_by", createBy);
		parameterMap.put("status", status);
		
		List<PropConfig> propList = dao.queryByProjectcodeAndCreatebyAndProfile(parameterMap);
		
		return propList;
	}
	
	@Override
	public List<PropConfig> queryByProjectcodeAndCreatebyAndProfileGroupBy(String projectCode, String createBy, String profile, String status) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("project_code", projectCode);
		parameterMap.put("profile", profile);
		parameterMap.put("create_by", createBy);
		parameterMap.put("status", status);
		
		List<PropConfig> propList = dao.queryByProjectcodeAndCreatebyAndProfileGroupBy(parameterMap);
		
		return propList;
	}
	
	@Override
	public void findPropByCreatebyAndProjectcodeAndProfileAndStatus(String createBy, String projectCode, String profile, StringBuilder propTest, StringBuilder propQuasiProduct, StringBuilder propProduct, String status) {
		Map<String, String> propTestMap = new HashMap<String, String>();
		Map<String, String> propQuasiProductMap = new HashMap<String, String>();
		Map<String, String> propProductMap = new HashMap<String, String>();
		
		switch(profile) {
		case TEST_PROFILE:
			propTestMap = getPropMapByCreatebyAndProjectcodeAndProfileAndStatus(createBy, projectCode, TEST_PROFILE, status);
			propTest.append(JSON.toJSONString(propTestMap));
			break;
		case QUASIPRODUCT_PROFILE:
			propQuasiProductMap = getPropMapByCreatebyAndProjectcodeAndProfileAndStatus(createBy, projectCode, QUASIPRODUCT_PROFILE, status);
			propQuasiProduct.append(JSON.toJSONString(propQuasiProductMap));
			break;
		case PRODUCT_PROFILE:
			propProductMap = getPropMapByCreatebyAndProjectcodeAndProfileAndStatus(createBy, projectCode, PRODUCT_PROFILE, status);
			propProduct.append(JSON.toJSONString(propProductMap));
			break;
		}
	}
	
	private Map<String, String> getPropMapByCreatebyAndProjectcodeAndProfileAndStatus(String createBy, String projectCode, String profile, String status) {
		Map<String, String> propMap = new HashMap<String, String>();
		List<PropConfig> propConfigList = null;

		//未审核的 配置
		if(NOTYET_VERIFY_STATUS.equals(status)) {
			propConfigList = this.queryByProjectcodeAndCreatebyAndProfileAndStatus(projectCode, createBy, profile,NOTYET_VERIFY_STATUS);
			for(PropConfig tempProp : propConfigList) {
					propMap.put(tempProp.getProKey(), tempProp.getProValue());
			}
		} else if(OK_VERIFY_STATUS.equals(status)) {
		//审核 的 配置
			propConfigList = this.getPropConfigsByProjectcodeAndProfile(projectCode, profile);
			for(PropConfig tempProp : propConfigList) {
					propMap.put(tempProp.getProKey(), tempProp.getProValue());
			}
		}
		return propMap;
	}
	
	/**
	 * 此方法 感觉需要 同步 synchronized
	 * 1.将旧配置  设为 失效 status 3
	 * 2.将未审核 配置 置为 审核
	 */
	@Override
	@Transactional
	public synchronized ReturnCodeEnum verifyPropByCreatebyAndProjectcodeAndProfile(String createBy, String projectCode, String profile, String status, String auditor_text) {
		/*boolean hasAuthority = SuperController.checkFunright(projectCode);
		if(!hasAuthority) {
			return ReturnCodeEnum.getByReturnCode(NO_AUTHORITY);
		} */
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("create_by", createBy);
		parameterMap.put("project_code", projectCode);
		parameterMap.put("profile", profile);
		
		parameterMap.put("auditor", SuperController.getUser().getUserName());
		parameterMap.put("auditor_text", auditor_text);
		parameterMap.put("status", status);
		
		if(OK_VERIFY_STATUS.equals(status)) {
			//当前 待审核 配置
			List<PropConfig> noPropList = this.queryByProjectcodeAndCreatebyAndProfileAndStatus(projectCode, createBy, profile,NOTYET_VERIFY_STATUS);
			//所有审核的配置
			List<PropConfig> propList = this.getPropConfigsByProjectcodeAndProfile(projectCode, profile);
			
			for(PropConfig tempProp : noPropList) {
				for(PropConfig oldProp : propList) {
					if(oldProp.getProKey().equals(tempProp.getProKey())) {
						tempProp.setLastValue(oldProp.getProValue());
						Map<String, Object> parameterMap1 = new HashMap<String, Object>();
						parameterMap1.put("id", tempProp.getId());
						parameterMap1.put("last_value", tempProp.getLastValue());
						dao.updatePropLastValue(parameterMap1);
					}
				}
			}
			/**
			 * 测试语句   将 新审核的 配置  所对应的 旧配置的条目 设为 失效  status = 3
			 * update t_g_properties_config a INNER JOIN t_g_properties_config b on a.pro_key = b.pro_key
			 * set a.status = 3
			 * where b.project_code = 'godzilla' and b.create_by = 'wanglin' and b.status = 0 and b.profile = 'TEST' and a.status = 1;
			 */
			int dbReturn1 = dao.changeStatusByNewverify(parameterMap);
			
			/**
			 * 更新所有 待审核配置状态
			 */
			int dbReturn2 = dao.verifyOKProp(parameterMap);
			
			return dbReturn1>0&&dbReturn2>0
					?ReturnCodeEnum.getByReturnCode(OK_VERIFYPROP)
							:ReturnCodeEnum.getByReturnCode(NO_VERIFYPROP);
		} else if (NOTYET_VERIFY_STATUS.equals(status)) {
			/**
			 * 更新所有 待审核配置状态
			 */
			int dbReturn2 = dao.verifyOKProp(parameterMap);
			return dbReturn2>0
					?ReturnCodeEnum.getByReturnCode(OK_VERIFYPROP)
							:ReturnCodeEnum.getByReturnCode(NO_VERIFYPROP);
		} else {
			//impossible here;
			return null;
		}
		
	}

}
