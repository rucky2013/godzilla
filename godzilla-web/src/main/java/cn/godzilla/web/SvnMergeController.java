package cn.godzilla.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.godzilla.common.ResultInfo;
import cn.godzilla.svn.BaseShellCommand;

/**
 * 合并代码
 * @author ZhongweiLee
 *
 */
@Controller
@RequestMapping(value="/svnmerge",method=RequestMethod.POST)
public class SvnMergeController {

	private final Logger logger = LogManager.getLogger(SvnMergeController.class);
	
	/**
	 * 代码合并
	 * @param branchPath
	 * 项目分支svn地址
	 * @param svnpwd
	 * svn密码
	 * @param trunkPath
	 * 项目主干svn地址
	 * @param projectName
	 * 项目code
	 * @param clientIp
	 * 客户端IP
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/merge")
	@ResponseBody
	public ResultInfo doMerge(@RequestParam("branchPath") String branchPath,
			@RequestParam("svnpwd") String svnpwd,
			@RequestParam("trunkPath") String trunkPath,
			@RequestParam("projectName") String projectName,
			@RequestParam("clientIp") String clientIp,
			HttpServletRequest request, HttpServletResponse response) {
		
		ResultInfo info = new ResultInfo();
		
		logger.info("************代码合并Begin***********");
		
		String svnuser="lizw" ;//用户名暂时写死
		
		String localPath="/home/godzilla/svndata/test";  //先写死
		
		boolean flag = false;
		
		try {
			
			BaseShellCommand command = new BaseShellCommand();
			String str = "sh /home/godzilla/svn_server.sh merge "+branchPath+" "+localPath+" "+svnuser+" "+ svnpwd +" "+trunkPath+" "+projectName+" "+clientIp ;
			flag = command.execute(str);
			
		} catch (Exception e) {
			
			logger.error(e);
			e.printStackTrace();
			
		}
		
		if(flag){
			info.setMessage("Success");
			logger.info("************代码合并End**************");
		}else{
			info.setMessage("Error");
			logger.error("************代码合并Error**************");
		}
		
		return info;
	}
	/**
	 * 提交主干
	 * @param branchPath
	 * 项目分支svn地址
	 * @param svnpwd
	 * svn密码
	 * @param trunkPath
	 * 项目主干svn地址
	 * @param projectName
	 * 项目code
	 * @param clientIp
	 * 客户端IP
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/commit")
	@ResponseBody
	public ResultInfo doCommit(@RequestParam("branchPath") String branchPath,
			@RequestParam("svnpwd") String svnpwd,
			@RequestParam("trunkPath") String trunkPath,
			@RequestParam("projectName") String projectName,
			@RequestParam("clientIp") String clientIp,
			HttpServletRequest request, HttpServletResponse response) {
		
		ResultInfo info = new ResultInfo();
		
		logger.info("************提交主干Begin***********");
		
		String svnuser="lizw" ;//用户名暂时写死
		String localPath="";
		
		boolean flag = false;
		
		try {
			String str = "sh /home/godzilla/svn_server.sh commit "+branchPath+" "+localPath+" "+svnuser+" "+ svnpwd +" "+trunkPath+" "+projectName+" "+clientIp ;
			BaseShellCommand command = new BaseShellCommand();
			flag = command.execute(str);
			
		} catch (Exception e) {
			logger.error(e);
			return info;
		}
		
		
		if(flag){
			info.setMessage("Success");
			logger.info("************提交主干End**************");
		}else{
			info.setMessage("Error");
			logger.error("************提交主干Error**************");
		}
		return info;
	}
}
