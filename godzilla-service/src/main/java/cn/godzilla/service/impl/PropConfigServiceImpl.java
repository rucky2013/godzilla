package cn.godzilla.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.godzilla.common.StringUtil;
import cn.godzilla.common.xml.XmlUtil;
import cn.godzilla.dao.PropConfigMapper;
import cn.godzilla.model.PropConfig;
import cn.godzilla.model.RpcResult;
import cn.godzilla.service.PropConfigService;

@Service
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
	 * @return
	 */
	private List<PropConfig> getPropConfigsByProjectcodeAndProfile(String project_code, String profile) {
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("projectCode", project_code);
		parameters.put("profile", profile);
		return dao.queryListByProjectcodeAndProfile(parameters);
	}

}
