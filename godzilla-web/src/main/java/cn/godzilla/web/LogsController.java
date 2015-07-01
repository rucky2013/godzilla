package cn.godzilla.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.godzilla.model.MvnCmdLog;
import cn.godzilla.model.SvnChangeLog;
import cn.godzilla.model.SvnCmdLog;
import cn.godzilla.model.SvnConflict;
import cn.godzilla.service.MvnCmdLogService;
import cn.godzilla.service.SvnChangeLogService;
import cn.godzilla.service.SvnCmdLogService;
import cn.godzilla.service.SvnConflictService;

@Controller
@RequestMapping(value="logs" ,method=RequestMethod.POST)

public class LogsController {
	
	private final Logger logger = LogManager.getLogger(LogsController.class);
	
	@Autowired
	private SvnCmdLogService svnCmdLogService;
	
	@Autowired
	private SvnConflictService svnConflictService;
	
	@Autowired
	private SvnChangeLogService svnChangeLogService;
	
	@Autowired
	private MvnCmdLogService mvnCmdLogService ;
	
	@RequestMapping(value ="svncmd/{user}")
	@ResponseBody
	public void addSvnCmdLog(@PathVariable("user") String user,
			@RequestParam("url") String url,
			@RequestParam("cmd") String cmd,
			HttpServletRequest request, HttpServletResponse response){
		
		
		SvnCmdLog log = new SvnCmdLog();
		log.setUserName(user);
		log.setRepositoryUrl(url);
		log.setCommands(cmd);
		
		logger.info("***********"+user +" begin insert t_g_svn_command_logs .....");
		
		svnCmdLogService.insertSelective(log);
		
	}
	@RequestMapping(value ="svnconflict/{user}")
	@ResponseBody
	public void addSvnConflictLog(@PathVariable("user") String user,
			@RequestParam("pcode") String pcode,
			@RequestParam("url") String url,
			@RequestParam("filename") String filename,
			HttpServletRequest request, HttpServletResponse response){
		
		
		SvnConflict log = new SvnConflict();
		log.setUserName(user);
		
		log.setProjectCode(pcode);
		
		log.setBranchUrl(url);
		
		log.setFileName(filename);
		
		logger.info("***********"+user +"  冲突记录入库 t_g_svn_conflict .....");
		
		svnConflictService.insertSelective(log);
		
	}
	
	@RequestMapping(value ="svnchange/{user}")
	@ResponseBody
	public void addSvnChangeLog(@PathVariable("user") String user,
			@RequestParam("type") Integer type,
			@RequestParam("url") String url,
			@RequestParam("filename") String filename,
			HttpServletRequest request, HttpServletResponse response){
		
		
		SvnChangeLog log = new SvnChangeLog();
		
		log.setUserName(user);
		
		log.setType(type);
		
		log.setRepositoryUrl(url);
		
		log.setFileName(filename);
		
		logger.info("***********"+user +"  svn变更记录入库 t_g_svn_change_logs .....");
		
		svnChangeLogService.insertSelective(log);
		
	}
	
	@RequestMapping(value ="mvncmd")
	@ResponseBody
	public void addMvnCmdLog(@PathVariable("user") String user,
			@RequestParam("pcode") String pcode,
			@RequestParam("cmd") String cmd,
			@RequestParam("env") String env,
			HttpServletRequest request, HttpServletResponse response){
		
		
		MvnCmdLog log = new MvnCmdLog();
		
		log.setCommands(cmd);
		
		log.setProfile(pcode);
		
		log.setUserName(user);
		
		log.setProfile(env);
		
		logger.info("***********"+user +"  Maven执行命令记录到 t_g_maven_command_logs .....");
		
		mvnCmdLogService.insertSelective(log);
		
	}
}
