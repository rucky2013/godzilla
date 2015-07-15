package cn.godzilla.web.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertiesUtil {
	
	private final static Logger logger = LogManager.getLogger(PropertiesUtil.class);
	
	private static Properties properties = new Properties();
	
	private PropertiesUtil(){
		
	}
	public static Properties getProperties(){
		
		try {
			properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("*********************config.properties file not found");
		}
			
		return properties;
		
	}
	
	public static void main(String[] args) {
		
		String tomcatHome = PropertiesUtil.getProperties().getProperty("client.tomcat.home.path");
		
		System.out.println(tomcatHome);
		
	}

}
