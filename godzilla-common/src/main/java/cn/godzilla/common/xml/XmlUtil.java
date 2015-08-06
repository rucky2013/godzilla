package cn.godzilla.common.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.godzilla.model.PropConfig;

public class XmlUtil {
	private static final Logger logger = LogManager.getLogger(XmlUtil.class);

	//public static final String PARENT_POMPATH = "F:/yixin_fso_app/godzilla/pom.xml";
	//public static final String SAVE_PARENT_POMPATH = "F:/yixin_fso_app/godzilla/pom1.xml";

	//public static final String WEB_POMPATH = "F:/yixin_fso_app/godzilla/godzilla-web/pom.xml";
	//public static final String SAVE_WEB_POMPATH = "F:/yixin_fso_app/godzilla/godzilla-web/pom1.xml";

	public static void main(String args[]) throws DocumentException, IOException {
		/*Document doc = parse(GET_XMLFILE_PATH);
		logger.info(doc);

		// pom1.xml
		Document doc1 = parse(SAVE_XMLFILE_PATH);
		printDocument(doc1);*/
		//coverParentPom("SDFLJ", PARENT_POMPATH, SAVE_PARENT_POMPATH);
	}
	
	/**
	 * 修改pom.xml parent.version
	 * @param parentVersion
	 * @param parentPomPath
	 * @param savePomPath
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public static void coverParentPom(String parentVersion, String parentPomPath, String savePomPath) throws DocumentException, IOException {
		Document doc = parse(parentPomPath);
		Element root = doc.getRootElement();
		Element parent_version = root.element("properties").element("parent.version");

		parent_version.setText(parentVersion);
		logger.info("++|++|++>ParentPom.properties:"+parent_version.getText().toString());
		saveDocument(doc, savePomPath);
	}
	/**
	 * 将配置项  写入pom.xml
	 * @param propconfigs
	 * @param savePomPath 
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public static void coverWebPom(List<PropConfig> propconfigs, String webPomPath, String savePomPath) throws DocumentException, IOException {
		Document doc = parse(webPomPath);
		Element root = doc.getRootElement();
		Element properties = root.element("profiles").element("profile").element("properties");
		properties.clearContent();
		
		for(PropConfig propconfig : propconfigs) {
			properties.addElement(propconfig.getProKey()).setText(propconfig.getProValue());
		}
		logger.info("++|++|++>coverWebPom.properties:"+properties.getText().toString());
		saveDocument(doc, savePomPath);
	}


	/**
	 * 从根节点遍历，来修改XML文件,并保存。
	 * 
	 * @param doc
	 * @throws IOException
	 */
	public static void updateDocument(Document doc) throws IOException {

		Element root = doc.getRootElement();
		// System.out.println(root.asXML());

		Element properties = root.element("profiles").element("profile").element("properties");
		properties.clearContent();

		properties.addAttribute("id", "11");
		properties.addElement("name").setText("Jack Chen");
		properties.addElement("sex").setText("男");
		properties.addElement("date").setText("2001-04-01");
		properties.addElement("email").setText("chen@163.com");
		properties.addElement("QQ").setText("2366001");

		System.out.println(properties.getText().toString());
		
	//	saveDocument(doc, SAVE_WEB_POMPATH);
	}

	/**
	 * 解析XML文档
	 * 
	 * @param xmlFile
	 * @return XML文档
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public static Document parse(String xmlFile) throws DocumentException, FileNotFoundException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File(xmlFile));
		return doc;
	}

	/***
	 * 将XML文档输出到控制台
	 * 
	 * @param doc
	 * @throws IOException
	 */
	public static void printDocument(Document doc) throws IOException {
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(new OutputStreamWriter(System.out), format);
		writer.write(doc);
		writer.close();
	}

	/**
	 * 保存XML文档
	 * 
	 * @param doc
	 * @param filePath
	 * @throws IOException
	 */
	public static void saveDocument(Document doc, String filePath) throws IOException {
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(new FileOutputStream(filePath), format);
		writer.write(doc);
		writer.close();
	}
	
}