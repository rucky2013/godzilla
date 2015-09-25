package cn.godzilla.mvn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import cn.godzilla.common.Application;
import cn.godzilla.common.Constant;

public class MvnBaseCommand extends Application implements Constant{
	
	private final Logger logger = LogManager.getLogger(MvnBaseCommand.class);
	public static final String AREA = "mvn";
	public static final String ENCODING = "UTF-8";
	
	public boolean execute(String command, String projectName, String env, final String username) {
		
		logger.info(command);
		Runtime rt = Runtime.getRuntime();
		Process p = null;
		try {
			p = rt.exec(command);
			
			inThreadPrint1(p, username);
			
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
		
		logger.debug("********MvnBaseCommand.execute Success*******");
		
		return true;
		
	}
	private void inThreadPrint1(Process p, String username) {
		final InputStream is1 = p.getInputStream();
		
		try {
			BufferedReader br1 = new BufferedReader(
					new InputStreamReader(is1, "utf-8"));
			String line1 = null;
			while((line1 = br1.readLine())!=null) {
				if(line1 != null) {
					
					//判断 mvndeploy 是否 执行成功
					if(line1.contains("BUILD SUCCESS")) {
						String mvnBuild = mvnBuildThreadLocal.get();
						mvnBuildThreadLocal.set(SUCCESS);
					} else if(line1.contains("BUILD FAILURE")) {
						String mvnBuild = mvnBuildThreadLocal.get();
						mvnBuildThreadLocal.set(FAILURE);
					}
					if(line1.contains("[ERROR]")) {
						mvnERRORThreadLocal.set(FAILURE);
					}
					logger.info("******MvnBaseCommand.execute-->InputStream******"+line1);
					
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is1.close();
			} catch(IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	//no use 
	public boolean executeNewThread(String command,String projectName,String env,final String username) {
		
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
			Thread t1= new Thread() {
				public void run() {

					try {
						BufferedReader br1 = new BufferedReader(
								new InputStreamReader(is1, ENCODING));
						String line1 = null;
						while ((line1 = br1.readLine()) != null) {
							if (line1 != null) {
								//判断 mvndeploy 是否 执行成功
								if(line1.contains("BUILD SUCCESS")){
									mvnBuildThreadLocal.set(SUCCESS);
								} else if(line1.contains("BUILD FAILURE")) {
									mvnBuildThreadLocal.set(BUILDFAILURE);
								}
								logger.info("******MvnBaseCommand.execute-->InputStream******"+line1);
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
			};

			Thread t2 = new Thread() {
				public void run() {

					try {
						BufferedReader br2 = new BufferedReader(
								new InputStreamReader(is2, ENCODING));
						String line2 = null;
						while ((line2 = br2.readLine()) != null) {
							if (line2 != null) {
								logger.info("******MvnBaseCommand.execute-->ErrorStream******"+line2);
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
			};

			
			t1.start();
			t2.start();
			t1.join();
			t2.join();
			
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
