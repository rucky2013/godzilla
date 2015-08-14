package cn.godzilla.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.godzilla.common.StringUtil;
import cn.godzilla.common.xml.XmlUtil;
import cn.godzilla.dao.ClientConfigMapper;
import cn.godzilla.dao.PropConfigMapper;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.PropConfig;
import cn.godzilla.model.RpcResult;
import cn.godzilla.service.PropConfigProviderService;

public class PropConfigProviderServiceImpl implements PropConfigProviderService {

	@Autowired
	private PropConfigMapper propConfigMapper;
	@Autowired
	private ClientConfigMapper clientConfigMapper;
	
	@Override
	public RpcResult propToPom(String project_code, String srcUrl, String profile, String parentVersion) throws DocumentException, IOException,  Exception {
		/**
		 * 1.get pom.xml path
		 */
		String parentPomPath = srcUrl + "/pom.xml";
		String webPomPath = srcUrl + "/" + project_code + "-web/pom.xml";
		
		try {
			/**
			 * 2.get propconfigs from DB
			 */
			parentVersion = StringUtil.isEmpty(parentVersion)?DEFAULT_VERSION_PARENTPOM:parentVersion;
			List<PropConfig> propconfigs = this.getPropConfigsByProjectcodeAndProfile(project_code, profile);
			/**
			 * 3.save propconfigs cover pom.xml
			 */
			XmlUtil.coverParentPom(parentVersion, parentPomPath, parentPomPath);
			XmlUtil.coverWebPom(propconfigs, webPomPath, webPomPath);
			/**
			 * 4.change deploy war tomcat properties for web/pom.xml
			 * if tomcat-need-plugin == 0
			 *    delete plugin; 
			 * else 
			 * 	  replace plugin;
			 */
			ClientConfig clientConfig = this.getTomcatpluginProp(project_code, profile);
			if(clientConfig.getTomcatNeedPlugin()!=null && !"0".equals(clientConfig.getTomcatNeedPlugin())){
				XmlUtil.coverWebPomforPlugin(project_code, clientConfig, webPomPath, webPomPath);
			} else {
				XmlUtil.deleteWebPomPlugin(webPomPath, webPomPath);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return RpcResult.create(SUCCESS);
	}
	private ClientConfig getTomcatpluginProp(String project_code, String profile) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("project_code", project_code);
		parameterMap.put("profile", profile);
		ClientConfig clientConfig = clientConfigMapper.queryDetail(parameterMap);
		return clientConfig;
	}

	/**
	 * 获取项目父pom的  parent.version 
	 * @param project_code
	 * @param profile
	 * @return
	 */
	private String getParentversionByProjectcodeAndProfile(String project_code, String profile) {
		
		return DEFAULT_VERSION_PARENTPOM;
	}

	public List<PropConfig> getPropConfigsByProjectcodeAndProfile(String project_code, String profile) {
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("projectCode", project_code);
		parameters.put("profile", profile);
		return propConfigMapper.queryListByProjectcodeAndProfile(parameters);
	}
	
}
