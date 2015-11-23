package cn.godzilla.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.Project;
import cn.godzilla.service.ClientConfigService;
import cn.godzilla.service.ProjectService;
import cn.godzilla.svn.BaseShellCommand;

@Component
public class SshUtil implements Constant {
	@Autowired
	private ProjectService projectService;
	@Autowired
	private ClientConfigService clientConfigService;
	@Autowired
	private BaseShellCommand command;
	
	/**
	 * 下载war包
	 * @param response
	 * @param projectCode
	 * @param profile
	 * @return
	 */
	public ReturnCodeEnum downLoadWar(String projectCode, String profile, HttpServletResponse response) {
		/**
		 * 1.限制并发　待定
		 * 日常环境  待定
		 * 准生产	待定
		 * 生产　 待定
		 **/
		return this.downLoadWar1(response, projectCode, profile);
		
	}
	
	private ReturnCodeEnum downLoadWar1(HttpServletResponse response, String projectCode, String profile) {
			
		java.io.BufferedOutputStream bos = null;
		java.io.BufferedInputStream bis = null;
		String ctxPath = SAVE_WAR_PATH ;
		//String downLoadPath = ctxPath + "/" + projectCode + ".war";
		//logger.info("****downloadpath : "+ downLoadPath);
		//1.ssh scp 到本地
		this.copyWar(projectCode, profile);
		
		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);
		String warName= project.getWarName();
		//1.5 获取 war包 文件名
		File warfile = this.searchFile(new File(ctxPath), warName);
		
		//2.输出
		try {
			long fileLength = warfile.length();
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment;filename=" + 
					new String(warfile.getName().getBytes("utf-8"),"ISO8859-1"));
			response.setHeader("Content-Length", String.valueOf(fileLength));
			
			bis = new BufferedInputStream(new FileInputStream(warfile));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while(-1!=(bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(bis!=null)
					bis.close();
				if(bos!=null) 
					bos.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	private File searchFile(final File folder, final String keyWord) {
		File[] findFolders = folder.listFiles(new FilenameFilter() {// 运用内部匿名类获得文件
			@Override
			public boolean accept(File dir, String name) {
				// 目录或文件包含关键字
				boolean flag = name.toLowerCase().contains(keyWord.toLowerCase());
				if(flag)
					return true;
				else
					return false;
			}

        });
		return findFolders[0];
	}
	
	private boolean copyWar(String projectCode, String profile) {
		ClientConfig clientConfig = clientConfigService.queryDetail(projectCode, profile);
		String clientIp = clientConfig.getRemoteIp();
		Project project = projectService.queryByProCode(projectCode, TEST_PROFILE);
		
		String tomcatHome = "";
		
		if("TEST".equals(profile)) {
			tomcatHome = "/app/tomcat";
		} else if("QUASIPRODUCT".equals(profile)) {
			clientIp = QUASIPRODUCT_WAR_IP;
			tomcatHome = "/app/tomcat";
		} else if("PRODUCT".equals(profile)) {
			clientIp = PRODUCT_WAR_IP;
			tomcatHome = "/app/tomcat";
		}
		
		tomcatHome += "/webapps/*.war";
		String str = "sh /home/godzilla/gzl/shell/server/copywar_server.sh " + clientIp + " " + tomcatHome + " " + SAVE_WAR_PATH;
		boolean flag = false;
		flag = command.execute(str, super.getUser().getUserName(), projectCode, project.getSvnUsername(), project.getSvnPassword());
		
		if(flag) {
			return true;
		} else {
			return false;
		}
		
	}
}
