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
import cn.godzilla.service.PropConfigProviderService;
import cn.godzilla.web.GodzillaApplication;

import com.alibaba.fastjson.JSON;

public class PropConfigProviderServiceImpl implements PropConfigProviderService {

	@Autowired
	private PropConfigMapper dao;
	
	public int insert(PropConfig record) {

		return dao.insert(record);
	}

	public int insertSelective(PropConfig record) {

		return dao.insertSelective(record);
	}

	public int update(PropConfig record) {

		return dao.update(record);
	}

	public PropConfig queryDetailById(long id) {

		return dao.queryDetailById(id);
	}

	public PropConfig queryDetailByKey(Map<String, String> map) {

		return dao.queryDetailByKey(map);
	}

	public List<PropConfig> queryList(Map<String, String> map) {
		
		return dao.queryList(map);
	}

	
	@Override
	public RpcResult propToPom(String project_code, String srcUrl, String profile) throws DocumentException, IOException,  Exception {
		/**
		 * 1.get pom.xml path
		 */
		String parentPomPath = srcUrl + "/pom.xml";
		String webPomPath = srcUrl + "/" + project_code + "-web/pom.xml";
		
		try {
			/**
			 * 2.get propconfigs from DB
			 */
			String parentVersion = getParentversionByProjectcodeAndProfile(project_code, profile);
			List<PropConfig> propconfigs = getPropConfigsByProjectcodeAndProfile(project_code, profile);
			/**
			 * 3.save propconfigs cover pom.xml
			 */
			XmlUtil.coverParentPom(parentVersion, parentPomPath, parentPomPath);
			XmlUtil.coverWebPom(propconfigs, webPomPath, webPomPath);
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
		
		return VERSION_PARENTPOM;
	}

	public List<PropConfig> getPropConfigsByProjectcodeAndProfile(String project_code, String profile) {
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("projectCode", project_code);
		parameters.put("profile", profile);
		return dao.queryListByProjectcodeAndProfile(parameters);
	}
	
}
