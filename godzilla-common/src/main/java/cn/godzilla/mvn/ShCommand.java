package cn.godzilla.mvn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import cn.godzilla.common.Application;
import cn.godzilla.common.Constant;

public class ShCommand extends Application implements Constant{
	
	private final Logger logger = LogManager.getLogger(ShCommand.class);
	public static final String ENCODING = "UTF-8";
	
	public String execute(String command) {
		
		logger.info(command);
		Runtime rt = Runtime.getRuntime();
		Process p = null;
		String jarlog = "";
		try {
			p = rt.exec(command);
			jarlog = inThreadPrint1(p);
			p.waitFor();
			p.destroy();
		} catch(Exception e) {
			logger.info(e.getMessage()) ;
			e.printStackTrace();
			try {
				p.getErrorStream().close();
				p.getInputStream().close();
				p.getOutputStream().close();
			} catch(Exception e1) {
				logger.error(e1.getMessage()) ;
				e.printStackTrace();
			}
		}
		return jarlog;
	}
	private String inThreadPrint1(Process p) {
		final InputStream is1 = p.getInputStream();
		String jarlog = "<p>********jar lib列表详情********";
		try {
			BufferedReader br1 = new BufferedReader(
					new InputStreamReader(is1, "utf-8"));
			String line1 = null;
			while((line1 = br1.readLine())!=null) {
				if(line1 != null) {
					jarlog = jarlog + "<br />" + line1;
					logger.info("******MvnBaseCommand.execute-->InputStream******"+jarlog);
				}
			}
			jarlog = jarlog + "</p>";
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is1.close();
			} catch(IOException e1) {
				e1.printStackTrace();
			}
		}
		return jarlog;
	}
}
