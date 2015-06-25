package cn.creditease.godzilla.mvn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

public class MvnBaseCommand {
	
	private final Logger logger = LogManager.getLogger(MvnBaseCommand.class);
	
	public boolean execute(String command,String projectName,String env,String username) {
		
		if(StringUtils.isEmpty(command) || StringUtils.isEmpty(projectName) || StringUtils.isEmpty(env) ||StringUtils.isEmpty(username)){
			
			logger.debug("*************MvnBaseCommand.execute 参数为空!");
			return false ;
			
		}
		
		logger.debug(command);
		Runtime rt = Runtime.getRuntime();
		Process p = null;
		try {
			p = rt.exec(command);
			// 获取进程的标准输入流
			final InputStream is1 = p.getInputStream();
			// 获取进城的错误流
			final InputStream is2 = p.getErrorStream();

			// 启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流 a
			new Thread() {
				public void run() {

					try {
						BufferedReader br1 = new BufferedReader(
								new InputStreamReader(is1, "UTF-8"));
						String line1 = null;
						while ((line1 = br1.readLine()) != null) {
							if (line1 != null) {
								logger.debug("******MvnBaseCommand.execute-->InputStream******"+line1);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							is1.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();

			new Thread() {
				public void run() {

					try {
						BufferedReader br2 = new BufferedReader(
								new InputStreamReader(is2, "UTF-8"));
						String line2 = null;
						while ((line2 = br2.readLine()) != null) {
							if (line2 != null) {
								logger.debug("******MvnBaseCommand.execute-->ErrorStream******"+line2);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							is2.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();

			p.waitFor();
			p.destroy();
			
			logger.debug("********MvnBaseCommand.execute Success*******");
			
			return true;
		} catch (Exception e) {
			
			logger.debug(e.getMessage());
			e.printStackTrace();
			
			try {
				p.getErrorStream().close();
				p.getInputStream().close();
				p.getOutputStream().close();
			} catch (Exception ee) {
				logger.debug(ee.getMessage());
				ee.printStackTrace();
			}
		}
		return false;
	}

}
