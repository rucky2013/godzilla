package cn.godzilla.rpc.provider;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cn.godzilla.common.Constant;
import cn.godzilla.common.xml.XmlUtil;
import cn.godzilla.model.PropConfig;
import cn.godzilla.service.PropConfigService;

public class PropertiesImportUtil {
	public static void main(String [] args) throws Exception {
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:applicationContextUtil.xml");
		
		PropConfigService propConfigService = (PropConfigService) context.getBean("propConfigService");
		
		Map<String, List<PropConfig>> proplistMap = parse("F:\\yixin_fso_app\\fso-java\\gardener\\trunk\\gardener\\gardener-web\\pom.xml", "gardener");
		
		for(PropConfig prop: proplistMap.get("test")) {
			propConfigService.insert(prop);
		}
		
		/*for(PropConfig prop: proplistMap.get("pre-online")) {
			propConfigService.insert(prop);
		}*/
		for(PropConfig prop: proplistMap.get("online")) {
			propConfigService.insert(prop);
		}
		/* */
		
	}
	
	
	public static Map<String, List<PropConfig>> parse(String filename, String projectCode) throws FileNotFoundException, DocumentException {
		Map<String, List<PropConfig>> proplistMap = new HashMap<String, List<PropConfig>>();
		Document doc = XmlUtil.parse(filename);
		Element root = doc.getRootElement();
		Element profiles = root.element("profiles");
		
		List<Element> profilelist = profiles.elements("profile");
		
		for(Element profile : profilelist) {
			String id = profile.element("id").getText();
			List<PropConfig> propConfiglist = new ArrayList<PropConfig>();
			
			Element properties = profile.element("properties");
			List<Element> propertieslist = properties.elements();
			
			int index = 1;
			for(Element property: propertieslist) {
				String key = property.getName();
				String value = property.getText();
				
				if(value.contains("\\")||value.contains("=")){
					value = "<![CDATA["+value+"]]>";
				}
				
				PropConfig prop = new PropConfig();
				prop.setProjectCode(projectCode);
				if(id.equals("test")) {
					prop.setProfile(Constant.TEST_PROFILE);
				} else if(id.equals("pre-online")) {
					prop.setProfile(Constant.QUASIPRODUCT_PROFILE);
				} else if(id.equals("online")) {
					prop.setProfile(Constant.PRODUCT_PROFILE);
				}
				
				prop.setProKey(key);
				prop.setProValue(value);
				prop.setRemark("");
				prop.setCreateBy("admin");
				prop.setCreateTime(new Date());
				prop.setUpdateTime(new Date());
				prop.setLastValue("");//多人修改时，一个人通过会影响其他人的 旧值，所以不设置
				prop.setStatus(1);//0未审核  1审核通过
				prop.setAuditor("");
				prop.setAuditorText("");
				prop.setIndexOrder(index++);
				propConfiglist.add(prop);
			}
			
			proplistMap.put(id, propConfiglist);
		}
		return proplistMap;
	}
}
