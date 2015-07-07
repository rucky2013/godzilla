package cn.godzilla.common.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;

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

public class XmlUtils {
	private static final Logger logger = LogManager.getLogger(XmlUtils.class);

	public static final String GET_XMLFILE_PATH = "F:/yixin_fso_app/godzilla/godzilla-web/pom.xml";

	public static final String SAVE_XMLFILE_PATH = "F:/yixin_fso_app/godzilla/godzilla-web/pom1.xml";

	public XmlUtils() {

	}

	public static void main(String args[]) throws DocumentException, IOException {
		Document doc = parse(GET_XMLFILE_PATH);
		logger.info(doc);

		// pom1.xml
		Document doc1 = parse(SAVE_XMLFILE_PATH);
		printDocument(doc1);
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
		
		saveDocument(doc);
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
	 * @throws IOException
	 */
	public static void saveDocument(Document doc) throws IOException {
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(new FileOutputStream(SAVE_XMLFILE_PATH), format);
		writer.write(doc);
		writer.close();
	}

}