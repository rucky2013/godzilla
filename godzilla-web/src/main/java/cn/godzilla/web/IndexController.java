package cn.godzilla.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.godzilla.model.OperateLog;
import cn.godzilla.model.Project;
import cn.godzilla.service.OperateLogService;
import cn.godzilla.service.ProjectService;

@Controller
@RequestMapping(value="/")
public class IndexController {
	
	private final Logger logger = LogManager.getLogger(IndexController.class);
	
	@Autowired
	private ProjectService projectService ;
	
	@Autowired
	private OperateLogService operateLogService ;
	
	@RequestMapping(value="index")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response){
		
		List<Project> projects = projectService.queryAll();
		
		List<OperateLog> logs = operateLogService.queryAll(Long.MAX_VALUE);
		
		ModelAndView view = new ModelAndView();
		
		
		view.setViewName("index");
		
		request.setAttribute("projects", projects);
		request.setAttribute("logs", logs);
		
		logger.debug("********projects.size"+projects.size());
		logger.debug("********logs.size"+logs.size());
		
//		try {
//			request.getRequestDispatcher("index").forward(request, response);
//		} catch (ServletException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		view.addObject("projects", projects);
//		view.addObject("logs", "ces");
		return view ;
	}

}
