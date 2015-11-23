package cn.godzilla.common;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * 哥斯拉全局变量 threadlocal
 * 
 * @author 201407280166
 *
 */
public abstract class Application implements Constant {

	// sid 登录用户标识　 web端　service端不共享
	protected static ThreadLocal<String> sidThreadLocal = new ThreadLocal<String>() {
		protected String initialValue() {
			return "";
		};
	};

	protected static ThreadLocal<String> projectcodeThreadLocal = new ThreadLocal<String>() {
		protected String initialValue() {
			return SERVER_USER;
		};
	};
	protected static ThreadLocal<String> profileThreadLocal = new ThreadLocal<String>() {
		protected String initialValue() {
			return TEST_PROFILE;
		};
	};

	protected static ThreadLocal<String> echoMessageThreadLocal = new ThreadLocal<String>() {
		protected String initialValue() {
			return "";
		};
	};
	
	protected static ThreadLocal<String> shellReturnThreadLocal = new ThreadLocal<String>() {
		protected String initialValue() {
			return "";
		};
	};
	
	//tail -f catalina.out 
	protected static ThreadLocal<String> catalinaLogThreadLocal = new ThreadLocal<String>() {
		protected String initialValue() {
			return "";
		}
	};
	// show jar lib list
	protected static ThreadLocal<String> jarlogThreadLocal = new ThreadLocal<String>() {
		protected String initialValue() {
			return "";
		}
	};
	protected static ThreadLocal<String> svnVersionThreadLocal = new ThreadLocal<String>() {
		protected String initialValue() {
			return "";
		};
	};
	
	protected static ThreadLocal<String> mvnBuildThreadLocal = new ThreadLocal<String>() {
		protected String initialValue() {
			return SUCCESS;
		};
	};

	/**
	 * 日常环境 通过访问 index.jsp 判断 是否 项目启动成功 其他环境暂不需要
	 * 
	 * @param IP
	 * @param war_name
	 * @return
	 */
	protected boolean ifSuccessStartTomcat(String IP, String war_name) {
		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		int timeout = DEFAULT_TIMEOUT;
		int i = 0;
		while (true) {
			String test_url = "http://" + IP + ":8080/" + war_name + "/index.jsp";
			StringBuilder rs = new StringBuilder();
			try {
				RequestConfig config = RequestConfig.custom().setSocketTimeout(10000).setConnectionRequestTimeout(10000).build();
				CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(config).build();
				HttpResponse response = client.execute(new HttpGet(test_url));

				if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
					// 20151102 不检查标识,返回200即成功
					return true;
				}
				if (HttpStatus.SC_NOT_FOUND == response.getStatusLine().getStatusCode()) {
					// 如果信息码 为 4xx 或者 5xx 则退出
					return false;
				} else if (HttpStatus.SC_INTERNAL_SERVER_ERROR == response.getStatusLine().getStatusCode()) {
					return false;
				}
			} catch (IOException e1) {
				System.out.println("---httpclient 报异常，预计为超时---");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// time out seconds : return false;
			i++;
			if (i >= timeout) {
				return false;
			}
		}
	}

	/**
	 * 
	 * |快速判断|首页展示启动与否使用| 日常环境 通过访问 index.jsp 判断 是否 项目启动成功 其他环境暂不需要 project.state
	 * 1.已启动 0.未知
	 * 
	 * @param IP
	 * @param war_name
	 * @return
	 */
	protected boolean ifSuccessStartProject(String IP, String war_name) {

		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		String test_url = "http://" + IP + ":8080/" + war_name + "/index.jsp";
		StringBuilder rs = new StringBuilder();
		try {
			RequestConfig config = RequestConfig.custom().setSocketTimeout(100).setConnectionRequestTimeout(100).setConnectTimeout(1000).build();
			CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(config).build();
			HttpResponse response = client.execute(new HttpGet(test_url));

			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				return true;
			}
			if (HttpStatus.SC_NOT_FOUND == response.getStatusLine().getStatusCode()) {
				// 如果信息码 为 4xx 或者 5xx 则退出
				return false;
			} else if (HttpStatus.SC_INTERNAL_SERVER_ERROR == response.getStatusLine().getStatusCode()) {
				return false;
			}
		} catch (IOException e1) {
			System.out.println("---httpclient 报异常，预计为超时---");
		}
		return false;
	}

	protected void isEmpty(Object o) {
		if (o == null)
			throw new BusinessException("数据为null->" + o.getClass().getName());
		if (o instanceof String) {
			if (StringUtil.isEmpty((String) o)) {
				throw new BusinessException("字符串为空");
			}
		}
	}

	protected void isEmpty(Object o, String errorMsg) {
		if (o == null)
			throw new BusinessException(errorMsg);
		if (o instanceof String) {
			if (StringUtil.isEmpty((String) o)) {
				throw new BusinessException(errorMsg);
			}
		}
	}

	protected void isEmpty(Object o, String errorCode, String errorMsg) {
		if (o == null)
			throw new BusinessException(errorCode, errorMsg);
		if (o instanceof String) {
			if (StringUtil.isEmpty((String) o)) {
				throw new BusinessException(errorCode, errorMsg);
			}
		}
	}
}
