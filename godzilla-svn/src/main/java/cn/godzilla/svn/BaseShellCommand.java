package cn.godzilla.svn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.godzilla.echo.rocketmq.Producer;
import cn.godzilla.echo.vo.EchoMessage;
import cn.godzilla.web.SuperController;


public class BaseShellCommand extends SuperController{
	
	private final Logger logger = LogManager.getLogger(BaseShellCommand.class);
	public static final String AREA = "svn";
	public static final String ENCODING = "UTF-8";
	public boolean execute(String command) {
		final String username = super.getUser().getUserName();
		System.out.println(command);

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
								logger.debug("******BaseShellCommand.execute-->InputStream******"+line1);
								EchoMessage echoMessage = EchoMessage.getInstance(username, AREA, line1);
								Producer.sendMessageToWeb(echoMessage);
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
			}.start();

			new Thread() {
				public void run() {

					try {
						BufferedReader br2 = new BufferedReader(
								new InputStreamReader(is2, "UTF-8"));
						String line2 = null;
						while ((line2 = br2.readLine()) != null) {
							if (line2 != null) {
								logger.debug("******BaseShellCommand.execute-->ErrorStream******"+line2);
								EchoMessage echoMessage = EchoMessage.getInstance(username, AREA, line2);
								Producer.sendMessageToWeb(echoMessage);
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
			}.start();

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

}
