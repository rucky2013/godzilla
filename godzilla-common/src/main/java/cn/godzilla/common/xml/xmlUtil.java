package cn.godzilla.common.xml;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class xmlUtil {
	private final Logger logger = LogManager.getLogger(xmlUtil.class);

    private static SAXReader reader = null;
    
    static {
    	reader = new SAXReader();
    }
    
	public void modifyDoc() {
		try {
			Document doc = reader.read(new File("file/catalog.xml"));
			// 修改属性内容
			List list = doc.selectNodes("//article/@level");
			Iterator<Attribute> iter = list.iterator();
			while (iter.hasNext()) {
				Attribute attr = iter.next();
				logger.info(attr.getName() + "#" + attr.getValue() + "#" + attr.getText());
				if ("Intermediate".equals(attr.getValue())) {
					// 修改属性值
					attr.setValue("Introductory");
					logger.info(attr.getName() + "#" + attr.getValue() + "#" + attr.getText());
				}
			}
			list = doc.selectNodes("//article/@date");
			iter = list.iterator();
			while (iter.hasNext()) {
				Attribute attr = iter.next();
				logger.info(attr.getName() + "#" + attr.getValue() + "#" + attr.getText());
				if ("December-2001".equals(attr.getValue())) {
					// 修改属性值
					attr.setValue("December-2011");
					logger.info(attr.getName() + "#" + attr.getValue() + "#" + attr.getText());
				}
			}
			// 修改节点内容
			list = doc.selectNodes("//article");
			Iterator<Element> it = list.iterator();

			while (it.hasNext()) {
				Element el = it.next();
				logger.info(el.getName() + "#" + el.getText() + "#" + el.getStringValue());
				// 修改title元素
				Iterator<Element> elIter = el.elementIterator("title");
				while (elIter.hasNext()) {
					Element titleEl = elIter.next();

					logger.info(titleEl.getName() + "#" + titleEl.getText() + "#" + titleEl.getStringValue());
					if ("Java configuration with XML Schema".equals(titleEl.getTextTrim())) {
						// 修改元素文本值
						titleEl.setText("Modify the Java configuration with XML Schema");
						logger.info(titleEl.getName() + "#" + titleEl.getText() + "#" + titleEl.getStringValue());
					}
				}
			}

			// 修改节点子元素内容
			list = doc.selectNodes("//article/author");
			it = list.iterator();
			while (it.hasNext()) {
				Element el = it.next();
				logger.info(el.getName() + "#" + el.getText() + "#" + el.getStringValue());
				List<Element> childs = el.elements();
				for (Element e : childs) {
					logger.info(e.getName() + "#" + e.getText() + "#" + e.getStringValue());
					if ("Marcello".equals(e.getTextTrim())) {
						e.setText("Ayesha");
					} else if ("Vitaletti".equals(e.getTextTrim())) {
						e.setText("Malik");
					}

					logger.info(e.getName() + "#" + e.getText() + "#" + e.getStringValue());
				}
			}
			// 写入到文件
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
