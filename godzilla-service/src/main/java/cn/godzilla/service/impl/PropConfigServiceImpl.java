package cn.godzilla.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.common.StringUtil;
import cn.godzilla.common.xml.XmlUtil;
import cn.godzilla.dao.PropBillMapper;
import cn.godzilla.dao.PropConfigMapper;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.PropBill;
import cn.godzilla.model.PropConfig;
import cn.godzilla.service.PropConfigService;
import cn.godzilla.util.GodzillaServiceApplication;

import com.alibaba.fastjson.JSON;

public class PropConfigServiceImpl extends GodzillaServiceApplication implements PropConfigService {

	@Autowired
	private PropConfigMapper propConfigMapper;
	@Autowired
	private PropBillMapper propBillMapper;

	@Override
	public int insert(PropConfig record) {

		return propConfigMapper.insert(record);
	}

	@Override
	public void findPropByProjectCode(String projectCode, String profile, StringBuilder propTest, StringBuilder propQuasiProduct, StringBuilder propProduct) {

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
	 * 获取项目的 所有审核配置项 根据 projectcode 和 profile 返回map
	 * 
	 * @param project_code
	 * @param profile
	 * @return map
	 */
	private Map<String, String> getPropMapByProjectcodeAndProfile(String projectCode, String profile) {

		List<PropConfig> propConfigList = getPropConfigsByProjectcodeAndProfile(projectCode, profile);
		Map<String, String> propMap = new HashMap<String, String>();

		for (PropConfig tempProp : propConfigList) {
			propMap.put(tempProp.getProKey(), tempProp.getProValue());
		}
		return propMap;
	}

	/**
	 * 获取项目的 所有审核配置项 根据 projectcode 和 profile 返回list
	 * 
	 * @param project_code
	 * @param profile
	 * @return list
	 */
	@Override
	public List<PropConfig> getPropConfigsByProjectcodeAndProfile(String project_code, String profile) {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("projectCode", project_code);
		parameters.put("profile", profile);
		return propConfigMapper.queryListByProjectcodeAndProfile(parameters);
	}

	@Override
	public ReturnCodeEnum addNotVerifyProp(String projectCode, String profile, String propTest, String propQuasiProduct, String propProduct) {

		Map<String, String> requestPropTest = JSON.parseObject(propTest, Map.class);
		Map<String, String> requestPropQuasiProduct = JSON.parseObject(propQuasiProduct, Map.class);
		Map<String, String> requestPropProduct = JSON.parseObject(propProduct, Map.class);

		/*
		 * Map<String, String> dbPropTestMap =
		 * getPropMapByProjectcodeAndProfile(projectCode, TEST_PROFILE);
		 * Map<String, String> dbPropQuasiProductMap =
		 * getPropMapByProjectcodeAndProfile(projectCode, QUASIPRODUCT_PROFILE);
		 * Map<String, String> dbPropProductMap =
		 * getPropMapByProjectcodeAndProfile(projectCode, PRODUCT_PROFILE);
		 */
		long billId = createPropBill(projectCode, getUser().getUserName());
		boolean fg1 = addProp(requestPropTest, projectCode, TEST_PROFILE, 0, billId);
		boolean fg2 = addProp(requestPropQuasiProduct, projectCode, QUASIPRODUCT_PROFILE, 0, billId);
		boolean fg3 = addProp(requestPropProduct, projectCode, PRODUCT_PROFILE, 0, billId);

		return (fg1 && fg2 && fg3) ? ReturnCodeEnum.getByReturnCode(OK_ADDUPDATEPROP) : ReturnCodeEnum.getByReturnCode(NO_ADDUPDATEPROP);
	}

	/**
	 * 添加 审核工单
	 * 
	 * @param projectCode
	 * @param userName
	 * @return
	 */
	private long createPropBill(String projectCode, String userName) {
		PropBill propBill = new PropBill();
		propBill.setCreateby(userName);
		propBill.setProjectCode(projectCode);
		propBill.setStatus("0");
		propBillMapper.insertSelective(propBill);
		return propBill.getId();
	}

	/**
	 * 添加 配置
	 * 
	 * @param requestProp
	 * @param projectCode
	 * @param profile
	 * @param status
	 * @return
	 */
	private boolean addProp(Map<String, String> requestProp, String projectCode, String profile, int status, long billId) {
		Set<String> requestKeys = requestProp.keySet();
		for (String requestKey : requestKeys) {
			PropConfig prop = new PropConfig();
			prop.setBillId(billId);
			prop.setProjectCode(projectCode);
			prop.setProfile(profile);
			prop.setProKey(requestKey);
			prop.setProValue(requestProp.get(requestKey));
			prop.setRemark("");
			prop.setCreateBy(GodzillaServiceApplication.getUser().getUserName());
			prop.setCreateTime(new Date());
			prop.setUpdateTime(new Date());
			prop.setLastValue("");// 多人修改时，一个人通过会影响其他人的 旧值，所以不设置
			prop.setStatus(status);// 0未审核
			prop.setAuditor("");
			prop.setAuditorText("");
			prop.setIndexOrder(0);
			propConfigMapper.insert(prop);
		}
		return true;
	}

	@Override
	public Map<String, String> queryAllProfile(String projectCode, String profile) {
		Map<String, String> profileMap = new HashMap<String, String>();
		profileMap.put("TEST", "TEST");
		profileMap.put("PRODUCT", "PRODUCT");
		profileMap.put("QUASIPRODUCT", "QUASIPRODUCT");
		return profileMap;
	}

	@Override
	public List<PropConfig> queryByProjectcodeAndCreatebyAndProfileAndStatus(String projectCode, String profile, String createBy, String status) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("project_code", projectCode);
		parameterMap.put("profile", profile);
		parameterMap.put("create_by", createBy);
		parameterMap.put("status", status);

		List<PropConfig> propList = propConfigMapper.queryByProjectcodeAndCreatebyAndProfile(parameterMap);

		return propList;
	}

	@Override
	public List<PropConfig> queryByProjectcodeAndCreatebyAndProfileGroupBy(String projectCode, String profile, String createBy, String status) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("project_code", projectCode);
		parameterMap.put("profile", profile);
		parameterMap.put("create_by", createBy);
		parameterMap.put("status", status);

		List<PropConfig> propList = propConfigMapper.queryByProjectcodeAndCreatebyAndProfileGroupBy(parameterMap);

		return propList;
	}

	@Override
	public List<PropBill> queryAllPropBill(String projectCode, String profile) {

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("project_code", projectCode);
		parameterMap.put("status", "0");
		List<PropBill> propBillList = propBillMapper.queryPropBillByProjectCodeAndStatus(parameterMap);
		return propBillList;
	}

	// 20151110 创建prop_bill表重写配置审核
	@Deprecated
	private void findPropByCreatebyAndProjectcodeAndProfileAndStatus(String projectCode, String profile, String createBy, StringBuilder propTest, StringBuilder propQuasiProduct, StringBuilder propProduct, String status) {
		Map<String, String> propTestMap = new HashMap<String, String>();
		Map<String, String> propQuasiProductMap = new HashMap<String, String>();
		Map<String, String> propProductMap = new HashMap<String, String>();

		switch (profile) {
		case TEST_PROFILE:
			propTestMap = getPropMapByCreatebyAndProjectcodeAndProfileAndStatus(projectCode, createBy, TEST_PROFILE, status);
			propTest.append(JSON.toJSONString(propTestMap));
			break;
		case QUASIPRODUCT_PROFILE:
			propQuasiProductMap = getPropMapByCreatebyAndProjectcodeAndProfileAndStatus(projectCode, createBy, QUASIPRODUCT_PROFILE, status);
			propQuasiProduct.append(JSON.toJSONString(propQuasiProductMap));
			break;
		case PRODUCT_PROFILE:
			propProductMap = getPropMapByCreatebyAndProjectcodeAndProfileAndStatus(projectCode, createBy, PRODUCT_PROFILE, status);
			propProduct.append(JSON.toJSONString(propProductMap));
			break;
		}
	}

	public void findPropByCreatebyAndProjectcodeAndProfileAndStatus(String projectCode, String profile, String createBy, StringBuilder propTest, StringBuilder propQuasiProduct, StringBuilder propProduct, String status, Long billId) {
		Map<String, String> propTestMap = new HashMap<String, String>();
		Map<String, String> propQuasiProductMap = new HashMap<String, String>();
		Map<String, String> propProductMap = new HashMap<String, String>();

		propTestMap = getPropMapByBillidAndStatus(createBy, projectCode, TEST_PROFILE, status, billId);
		propTest.append(JSON.toJSONString(propTestMap));
		propQuasiProductMap = getPropMapByBillidAndStatus(createBy, projectCode, QUASIPRODUCT_PROFILE, status, billId);
		propQuasiProduct.append(JSON.toJSONString(propQuasiProductMap));
		propProductMap = getPropMapByBillidAndStatus(createBy, projectCode, PRODUCT_PROFILE, status, billId);
		propProduct.append(JSON.toJSONString(propProductMap));

	}

	private Map<String, String> getPropMapByBillidAndStatus(String projectCode, String profile, String createBy, String status, Long billId) {
		Map<String, String> propMap = new HashMap<String, String>();
		List<PropConfig> propConfigList = null;

		// 未审核的 配置
		if (NOTYET_VERIFY_STATUS.equals(status)) {
			propConfigList = this.queryPropConfigByProjectcodeAndProfileAndStatusAndBillid(projectCode, profile, createBy, NOTYET_VERIFY_STATUS, billId);
			for (PropConfig tempProp : propConfigList) {
				propMap.put(tempProp.getProKey(), tempProp.getProValue());
			}
		} else if (OK_VERIFY_STATUS.equals(status)) {
			// 审核 的 配置
			propConfigList = this.getPropConfigsByProjectcodeAndProfile(projectCode, profile);
			for (PropConfig tempProp : propConfigList) {
				propMap.put(tempProp.getProKey(), tempProp.getProValue());
			}
		}
		return propMap;
	}

	private List<PropConfig> queryPropConfigByProjectcodeAndProfileAndStatusAndBillid(String projectCode, String profile, String createBy, String status, Long billId) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("project_code", projectCode);
		parameterMap.put("profile", profile);
		parameterMap.put("create_by", createBy);
		parameterMap.put("status", status);
		parameterMap.put("billId", billId);

		List<PropConfig> propList = propConfigMapper.queryByProjectcodeAndCreatebyAndProfileAndBillid(parameterMap);

		return propList;
	}

	// 20151110 创建prop_bill表重写配置审核
	@Deprecated
	private Map<String, String> getPropMapByCreatebyAndProjectcodeAndProfileAndStatus(String projectCode, String profile, String createBy, String status) {
		Map<String, String> propMap = new HashMap<String, String>();
		List<PropConfig> propConfigList = null;

		// 未审核的 配置
		if (NOTYET_VERIFY_STATUS.equals(status)) {
			propConfigList = this.queryByProjectcodeAndCreatebyAndProfileAndStatus(projectCode, createBy, profile, NOTYET_VERIFY_STATUS);
			for (PropConfig tempProp : propConfigList) {
				propMap.put(tempProp.getProKey(), tempProp.getProValue());
			}
		} else if (OK_VERIFY_STATUS.equals(status)) {
			// 审核 的 配置
			propConfigList = this.getPropConfigsByProjectcodeAndProfile(projectCode, profile);
			for (PropConfig tempProp : propConfigList) {
				propMap.put(tempProp.getProKey(), tempProp.getProValue());
			}
		}
		return propMap;
	}

	/**
	 * 此方法 感觉需要 同步 synchronized 1.将旧配置 设为 失效 status 3 2.将未审核 配置 置为 审核
	 */

	@Override
	@Transactional
	@Deprecated
	public synchronized ReturnCodeEnum verifyPropByCreatebyAndProjectcodeAndProfile(String projectCode, String profile, String createBy, String status, String auditor_text) {
		/*
		 * boolean hasAuthority = SuperController.checkFunright(projectCode);
		 * if(!hasAuthority) { return
		 * ReturnCodeEnum.getByReturnCode(NO_AUTHORITY); }
		 */
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("create_by", createBy);
		parameterMap.put("project_code", projectCode);
		parameterMap.put("profile", profile);

		parameterMap.put("auditor", GodzillaServiceApplication.getUser().getUserName());
		parameterMap.put("auditor_text", auditor_text);
		parameterMap.put("status", status);

		if (OK_VERIFY_STATUS.equals(status)) {
			// 当前 待审核 配置
			List<PropConfig> noPropList = this.queryByProjectcodeAndCreatebyAndProfileAndStatus(projectCode, createBy, profile, NOTYET_VERIFY_STATUS);
			// 所有审核的配置
			List<PropConfig> propList = this.getPropConfigsByProjectcodeAndProfile(projectCode, profile);

			for (PropConfig tempProp : noPropList) {
				for (PropConfig oldProp : propList) {
					if (oldProp.getProKey().equals(tempProp.getProKey())) {
						tempProp.setLastValue(oldProp.getProValue());
						Map<String, Object> parameterMap1 = new HashMap<String, Object>();
						parameterMap1.put("id", tempProp.getId());
						parameterMap1.put("last_value", tempProp.getLastValue());
						propConfigMapper.updatePropLastValue(parameterMap1);
					}
				}
			}
			/**
			 * 测试语句 将 新审核的 配置 所对应的 旧配置的条目 设为 失效 status = 3 update
			 * t_g_properties_config a left JOIN t_g_properties_config b on
			 * a.pro_key = b.pro_key and a.project_code = b.project_code and
			 * a.profile = b.profile set a.status = 3 where b.project_code =
			 * #{project_code} and b.profile = #{profile} and a.status = 1 and
			 * b.create_by = #{create_by} and b.status = 0;
			 */
			int dbReturn1 = propConfigMapper.changeStatusByNewverify(parameterMap);

			/**
			 * 更新所有 待审核配置状态
			 */
			int dbReturn2 = propConfigMapper.verifyOKProp(parameterMap);

			return dbReturn1 >= 0 && dbReturn2 >= 0 ? ReturnCodeEnum.getByReturnCode(OK_VERIFYPROP) : ReturnCodeEnum.getByReturnCode(NO_VERIFYPROP);
		} else if (STOP_VERIFY_STATUS.equals(status)) {
			/**
			 * 更新所有 待审核配置状态
			 */
			int dbReturn2 = propConfigMapper.verifyOKProp(parameterMap);
			return dbReturn2 > 0 ? ReturnCodeEnum.getByReturnCode(OK_VERIFYPROP) : ReturnCodeEnum.getByReturnCode(NO_VERIFYPROP);
		} else {
			// impossible here;
			return null;
		}

	}

	@Override
	@Transactional
	public ReturnCodeEnum verifyPropByCreatebyAndProjectcodeAndALLProfile(String projectCode, String profile, String createBy, String status, String auditor_text, Long billId) {
		ReturnCodeEnum re1 = this.verifyPropByCreatebyAndProjectcodeAndProfile(createBy, projectCode, TEST_PROFILE, status, auditor_text, billId);
		ReturnCodeEnum re2 = this.verifyPropByCreatebyAndProjectcodeAndProfile(createBy, projectCode, QUASIPRODUCT_PROFILE, status, auditor_text, billId);
		ReturnCodeEnum re3 = this.verifyPropByCreatebyAndProjectcodeAndProfile(createBy, projectCode, PRODUCT_PROFILE, status, auditor_text, billId);
		if (re1.equals(ReturnCodeEnum.getByReturnCode(OK_VERIFYPROP)) && re1.equals(re2) && re2.equals(re3)) {
			return re1;
		} else {
			return ReturnCodeEnum.getByReturnCode(NO_VERIFYPROP);
		}
	}

	private ReturnCodeEnum verifyPropByCreatebyAndProjectcodeAndProfile(String projectCode, String profile, String createBy, String status, String auditor_text, Long billId) {
		// 当前 待审核 配置
		List<PropConfig> noPropList = this.queryPropConfigByProjectcodeAndProfileAndStatusAndBillid(projectCode, createBy, profile, NOTYET_VERIFY_STATUS, billId);
		// 所有审核的配置
		List<PropConfig> propList = this.getPropConfigsByProjectcodeAndProfile(projectCode, profile);

		if (OK_VERIFY_STATUS.equals(status)) {
			for (PropConfig tempProp : noPropList) {
				boolean flag = false;
				// 如果 是旧配置则更新
				for (PropConfig oldProp : propList) {
					if (oldProp.getProKey().equals(tempProp.getProKey())) {
						flag = true;
						// 旧配置失效
						Map<String, Object> parameterMap = new HashMap<String, Object>();
						parameterMap.put("id", oldProp.getId());
						parameterMap.put("status", "3");

						propConfigMapper.updatePropStatusById(parameterMap);
						// 新配置设置旧值,并置为有效
						tempProp.setLastValue(oldProp.getProValue());
						Map<String, Object> parameterMap1 = new HashMap<String, Object>();
						parameterMap1.put("id", tempProp.getId());
						parameterMap1.put("last_value", tempProp.getLastValue());
						parameterMap1.put("status", "1");

						parameterMap1.put("auditor", GodzillaServiceApplication.getUser().getUserName());
						parameterMap1.put("auditor_text", auditor_text);
						propConfigMapper.updatePropLastValueAndStatusById(parameterMap1);

						// 设置propbill意见及状态
						Map<String, Object> parameterMap2 = new HashMap<String, Object>();
						parameterMap2.put("id", billId);
						parameterMap2.put("status", "1");

						parameterMap2.put("auditor", GodzillaServiceApplication.getUser().getUserName());
						parameterMap2.put("auditor_text", auditor_text);
						propBillMapper.updatePropBillById(parameterMap2);
					}
				}
				// 2如果 是新配置则添加
				if (!flag) {
					// 新配置设置旧值,并置为有效
					Map<String, Object> parameterMap1 = new HashMap<String, Object>();
					parameterMap1.put("id", tempProp.getId());
					parameterMap1.put("status", "1");

					parameterMap1.put("auditor", GodzillaServiceApplication.getUser().getUserName());
					parameterMap1.put("auditor_text", auditor_text);
					propConfigMapper.updatePropLastValueAndStatusById(parameterMap1);

					// 设置propbill意见及状态
					Map<String, Object> parameterMap2 = new HashMap<String, Object>();
					parameterMap2.put("id", billId);
					parameterMap2.put("status", "1");

					parameterMap2.put("auditor", GodzillaServiceApplication.getUser().getUserName());
					parameterMap2.put("auditor_text", auditor_text);
					propBillMapper.updatePropBillById(parameterMap2);
				}
			}

			return ReturnCodeEnum.getByReturnCode(OK_VERIFYPROP);

		} else if (STOP_VERIFY_STATUS.equals(status)) {
			/**
			 * 更新所有 待审核配置状态
			 */
			// 设置propbill意见及状态
			Map<String, Object> parameterMap2 = new HashMap<String, Object>();
			parameterMap2.put("id", billId);
			parameterMap2.put("status", "2");
			parameterMap2.put("auditor", GodzillaServiceApplication.getUser().getUserName());
			parameterMap2.put("auditor_text", auditor_text);
			int dbReturn1 = propBillMapper.updatePropBillById(parameterMap2);

			int dbReturn2 = 1;
			for (PropConfig tempProp : noPropList) {
				parameterMap2.put("status", 2);
				dbReturn2 = propConfigMapper.verifyNOProp(parameterMap2);
			}

			return dbReturn2 > 0 && dbReturn1 > 0 ? ReturnCodeEnum.getByReturnCode(OK_VERIFYPROP) : ReturnCodeEnum.getByReturnCode(NO_VERIFYPROP);
		} else {
			// impossible here;
		}
		// impossible here;
		return ReturnCodeEnum.getByReturnCode(NO_VERIFYPROP);
	}

	@Override
	public ReturnCodeEnum resortPropById(String projectCode, String profile, String sortJson) {
		Map<String, Integer> propMap = (Map<String, Integer>) JSON.parse(sortJson);
		Set<Entry<String, Integer>> entrySet = propMap.entrySet();
		for (Entry<String, Integer> entry : entrySet) {
			Map<String, Integer> parameterMap = new HashMap<String, Integer>();
			parameterMap.put("id", Integer.parseInt(entry.getKey()));
			parameterMap.put("index_order", entry.getValue());
			propConfigMapper.updatePropIndex(parameterMap);
		}
		return ReturnCodeEnum.getByReturnCode(OK_SORTPROP);
	}

	// ================
	@Override
	public ReturnCodeEnum propToPom(String project_code, String webPath, String profile, String parentVersion, ClientConfig clientConfig) {
		/**
		 * 1.get pom.xml path
		 */
		String webPomPath = webPath + "/pom.xml";

		try {
			/**
			 * 2.get propconfigs from DB
			 */
			parentVersion = StringUtil.isEmpty(parentVersion) ? DEFAULT_VERSION_PARENTPOM : parentVersion;

			/**
			 * 4.change deploy war tomcat properties for web/pom.xml if
			 * tomcat-need-plugin == 0 delete plugin; else replace plugin;
			 */
			if (clientConfig.getTomcatNeedPlugin() != null && !"0".equals(clientConfig.getTomcatNeedPlugin())) {
				XmlUtil.coverWebPomforPlugin(project_code, clientConfig, webPomPath, webPomPath);
			} else {
				XmlUtil.deleteWebPomPlugin(webPomPath, webPomPath);
			}

			/**
			 * 3.save propconfigs cover pom.xml
			 */
			List<PropConfig> propconfigs = this.getPropConfigsByProjectcodeAndProfile1(project_code, profile);
			// XmlUtil.coverParentPom(parentVersion, parentPomPath,
			// parentPomPath);
			// this.replaceHtml(propconfigs);
			XmlUtil.coverWebPom(propconfigs, webPomPath, webPomPath);

		} catch (Exception e) {
			e.printStackTrace();
			return ReturnCodeEnum.getByReturnCode(NO_CHANGEPOM);
		}
		return ReturnCodeEnum.getByReturnCode(OK_CHANGEPOM);
	}

	public List<PropConfig> getPropConfigsByProjectcodeAndProfile1(String project_code, String profile) {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("projectCode", project_code);
		parameters.put("profile", profile);
		return propConfigMapper.queryListByProjectcodeAndProfile(parameters);
	}
}
