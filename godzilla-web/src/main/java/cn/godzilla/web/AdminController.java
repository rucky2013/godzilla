package cn.godzilla.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.godzilla.common.response.ResponseBodyJson;
import cn.godzilla.model.Project;
import cn.godzilla.service.MvnService;
import cn.godzilla.service.ProjectService;
@RequestMapping(value="admin")
public class AdminController extends GodzillaApplication{
	
	private final static Logger logger = LogManager.getLogger(TomcatController.class);
	
	@Autowired
	private MvnService mvnService;
	
	@Autowired
	private ProjectService projectService;
	/**
	 *  重启所有项目
	 *  
		项目依赖关系
		nuggets-server ->cupid
		xuanyuan->cupid
		message-center->cupid,newmanager
		fso-lark->cupid
		zeus->
		hades->xuanyuan
		uic->cupid
		hera 												
		apollo
		va-schedule 
		va-web
		uicm
		项目启动顺序
		(nuggets-server,message-center,fso-lark,zeus,hades,uic,hera,apollo,va-web,va-schedule)->(xuanyuan,newmanager)->(cupid)
	 * @return
	 */
	@RequestMapping(value="/{sid}/restartProjects", method=RequestMethod.GET)
	@ResponseBody
	public Object restartProjects(@PathVariable String sid, HttpServletRequest request, HttpServletResponse response) {
		
		List<Project> projects = projectService.queryAll();
		for(Project project: projects) {
			if(list1.contains(project.getProjectCode())) {
				//---fork join
				boolean flag = mvnService.restartTomcat(project.getProjectCode(), TEST_PROFILE);
				if(!flag) {
					return ResponseBodyJson.custom().setAll(NO_AJAX, FAILURE, project.getProjectCode()+"：启动失败").build();
				}
			}
		}
		
		return null;
	}
	
	private static List<String> list1 = new ArrayList<String>();
	private static List<String> list2 = new ArrayList<String>();
	private static List<String> list3 = new ArrayList<String>();
	static {
		list1.add("nuggets-server");
		list1.add("message-center");
		list1.add("fso-lark");
		list1.add("zeus");
		list1.add("hades");
		list1.add("uic");
		list1.add("hera");
		list1.add("apollo");
		list1.add("va-web");
		list1.add("va-schedule");
		
		list2.add("xuanyuan");
		list2.add("newmanager");
		
		list3.add("cupid");
	}
	/**
	 *  重新发布所有项目
	 * 
		项目依赖关系
		nuggets-server ->cupid
		xuanyuan->cupid
		message-center->cupid,newmanager
		fso-lark->cupid
		zeus->
		hades->xuanyuan
		uic->cupid
		hera 												
		apollo
		va-schedule 
		va-web
		uicm
		项目启动顺序
		(nuggets-server,message-center,fso-lark,zeus,hades,uic,hera,apollo,va-web,va-schedule)->(xuanyuan,newmanager)->(cupid)
	 * @return
	 */
	@RequestMapping(value="/{sid}/deployProjects", method=RequestMethod.GET)
	@ResponseBody
	public Object deployProjects(@PathVariable String sid, HttpServletRequest request, HttpServletResponse response) {
		
		
		
		return null;
	}
	
}
