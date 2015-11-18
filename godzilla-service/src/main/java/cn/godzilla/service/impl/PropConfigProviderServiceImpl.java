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
	
	@Override
	public RpcResult propToPom(String project_code, String webPath, String profile, String parentVersion, ClientConfig clientConfig) throws DocumentException, IOException,  Exception {
		/*
		 * 1.get pom.xml path
		 */
		String webPomPath = webPath + "/pom.xml";
		List<PropConfig> propconfigs = this.getPropConfigsByProjectcodeAndProfile(project_code, profile);
		try {
			/*
			 * 2.get propconfigs from DB
			 */
			parentVersion = StringUtil.isEmpty(parentVersion)?DEFAULT_VERSION_PARENTPOM:parentVersion;
			
			/*
			 * 2.5 check local web-pom.xml dev-test profile VS db ${projectCode-profile}'s profile 
			 */
			String looseProp = XmlUtil.comparePropFromWebPomVSDb(profile, propconfigs, webPomPath);
			if(!"".equals(looseProp)) {
				return RpcResult.createLooseprop(FAILURE, 0L, looseProp);
			}
			
			/*
			 * 3.change deploy war tomcat properties for web/pom.xml
			 * if tomcat-need-plugin == 0
			 *    delete plugin; 
			 * else 
			 * 	  replace plugin;
			 */
			if(clientConfig.getTomcatNeedPlugin()!=null && !"0".equals(clientConfig.getTomcatNeedPlugin())){
				XmlUtil.coverWebPomforPlugin(project_code, clientConfig, webPomPath, webPomPath);
			} else {
				XmlUtil.deleteWebPomPlugin(webPomPath, webPomPath);
			}
			
			/*
			 * 4.save propconfigs cover pom.xml
			 */
			
			//XmlUtil.coverParentPom(parentVersion, parentPomPath, parentPomPath);
			//this.replaceHtml(propconfigs);
			XmlUtil.coverWebPom(propconfigs, webPomPath, webPomPath);
			
			
		} catch(Exception e) {
			e.printStackTrace();
			return RpcResult.create(FAILURE, 0L);
		}
		return RpcResult.create(SUCCESS, 0L);
	}
	
	private String replaceHtml(String string) {
		return string.replace("&lt;", "<").replace("&gt;", ">").replace("&#39;", "'");
	}
	
	@Autowired
	private PropConfigMapper propConfigMapper;
	
	public List<PropConfig> getPropConfigsByProjectcodeAndProfile(String project_code, String profile) {
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("projectCode", project_code);
		parameters.put("profile", profile);
		return propConfigMapper.queryListByProjectcodeAndProfile(parameters);
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

}
