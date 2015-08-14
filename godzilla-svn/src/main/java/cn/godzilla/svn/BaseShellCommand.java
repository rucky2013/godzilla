package cn.godzilla.svn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import cn.godzilla.common.Application;
import cn.godzilla.echo.rocketmq.Producer;
import cn.godzilla.echo.vo.EchoMessage;


public class BaseShellCommand extends Application{
	
	private final Logger logger = LogManager.getLogger(BaseShellCommand.class);
	public static final String AREA = "svn";
	public static final String ENCODING = "UTF-8";
	public boolean execute(String command, final String username) {
		
		if(StringUtils.isEmpty(command) || StringUtils.isEmpty(username)){
			
			logger.debug("*************svn baseBaseCommand.execute 参数为空!");
			return false ;
			
		}
		System.out.println(command);
		
		Runtime rt = Runtime.getRuntime();
		Process p = null;
		try {
			
			p = rt.exec(command);
			
			if(!isEcho){
				if(command.contains(" status ")) {
					inThreadPrint1(p);
				} else if(command.contains(" version ")) {
					inThreadPrint2(p);
				} else {
					inThreadPrint3(p);
				}
				
			} else {
				newThreadPrint(p, username);
			}
			
			p.waitFor();
			p.destroy();
			
			logger.debug("********BaseShellCommand.execute Success*******");
			
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
	
	/**
	 * no echo
	 * svn status
	 * @param p
	 */
	private void inThreadPrint1(Process p) {
		// 获取进程的标准输入流
		final InputStream is1 = p.getInputStream();
		try {
			BufferedReader br1 = new BufferedReader(
					new InputStreamReader(is1, "UTF-8"));
			String line1 = null;
			while ((line1 = br1.readLine()) != null) {
				if (line1 != null) {
					logger.info("******BaseShellCommand.execute-->InputStream******"+line1);
					String message = echoMessageThreadLocal.get();
					echoMessageThreadLocal.set(message+" \r\n "+line1);
				}
			}
		} catch (Exception e) {
			logger.debug("******Stream closed******");
		} finally {
			try {
				is1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * no echo
	 * svn version 
	 * @param p
	 */
	private void inThreadPrint2(Process p) {
		// 获取进程的标准输入流
		final InputStream is1 = p.getInputStream();
		try {
			BufferedReader br1 = new BufferedReader(
					new InputStreamReader(is1, "UTF-8"));
			String line1 = null;
			while ((line1 = br1.readLine()) != null) {
				if (line1 != null) {
					logger.info("******BaseShellCommand.execute-->InputStream******"+line1);
					if(line1.startsWith("versionr")) {
						String version = line1.replaceAll("versionr", "");
						svnVersionThreadLocal.set(version);
					}
					String message = shellReturnThreadLocal.get();
					shellReturnThreadLocal.set(line1);
				}
			}
		} catch (Exception e) {
			logger.debug("******Stream closed******");
		} finally {
			try {
				is1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * no echo
	 * svn merge commit 
	 * @param p
	 */
	private void inThreadPrint3(Process p) {
		// 获取进程的标准输入流
		final InputStream is1 = p.getInputStream();
		try {
			BufferedReader br1 = new BufferedReader(
					new InputStreamReader(is1, "UTF-8"));
			String line1 = null;
			while ((line1 = br1.readLine()) != null) {
				if (line1 != null) {
					logger.info("******BaseShellCommand.execute-->InputStream******"+line1);
					String message = shellReturnThreadLocal.get();
					shellReturnThreadLocal.set(line1);
				}
			}
		} catch (Exception e) {
			logger.debug("******Stream closed******");
		} finally {
			try {
				is1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void newThreadPrint(Process p, final String username) throws InterruptedException {
		// 获取进程的标准输入流
		final InputStream is1 = p.getInputStream();
		// 获取进城的错误流
		final InputStream is2 = p.getErrorStream();

		// 启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流 a
		Thread t1 = new Thread() {
			public void run() {

				try {
					BufferedReader br1 = new BufferedReader(
							new InputStreamReader(is1, "UTF-8"));
					String line1 = null;
					while ((line1 = br1.readLine()) != null) {
						if (line1 != null) {
							EchoMessage echoMessage = EchoMessage.getInstance(username, AREA, line1);
							if(isEcho)  Producer.sendMessageToWeb(echoMessage);
							logger.info("******BaseShellCommand.execute-->InputStream******"+line1);
						}
					}
				} catch (Exception e) {
					//e.printStackTrace();
					logger.debug("******Stream closed******");
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
							new InputStreamReader(is2, "UTF-8"));
					String line2 = null;
					while ((line2 = br2.readLine()) != null) {
						if (line2 != null) {
							EchoMessage echoMessage = EchoMessage.getInstance(username, AREA, line2);
							if(isEcho)  Producer.sendMessageToWeb(echoMessage);
							logger.info("******BaseShellCommand.execute-->ErrorStream******"+line2);
						}
					}
				} catch (IOException e) {
					//e.printStackTrace();
					logger.debug("******Stream closed******");
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
	}

}
