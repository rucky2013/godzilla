package cn.creditease.godzilla.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.creditease.godzilla.svn.SvnCommand;

@Controller
@RequestMapping(value="/svnmerge")
public class SvnMergeController {

	private final Logger logger = LogManager.getLogger(SvnMergeController.class);
	
	@Autowired
	private SvnCommand command;
	
	@RequestMapping(value="/docheckout")
	public ModelAndView doCheckout(){
		
		logger.debug("**********SvnMergeController.doCheckout*******");
		
		ModelAndView view = new ModelAndView();
		
		boolean flag = false;
		
		flag = command.checkout("http://10.100.142.37:9090/svn/fso/cupid/branch/cupid-normandy", "/home/godzilla/lizw", "lizhongwei", "1");
		
		if(flag){
			
			view.setViewName("index");
		}else{
		
			view.setViewName("false");
		}
		return view ;
	}
	
	@RequestMapping(value="/merge2branch")
	public ModelAndView doMerge(HttpServletRequest request,HttpServletResponse response){
		
		ModelAndView view = new ModelAndView();
		
//		String svnPath = request.getParameter("svnpath");
//		String localPath = request.getParameter("localpath");
//		String username = request.getParameter("username");
//		String password = request.getParameter("password");
//		String trunkUrl = request.getParameter("trunkUrl");
//		String projectName = request.getParameter("projectName");
		
		String svnPath = "svn://182.92.104.118/svntestbarnch";
		String localPath = "/home/godzilla/lzwdata/svntestbarnch";
		String username = "lizw";
		String password = "lizw@123";
		String trunkUrl = "svn://182.92.104.118/svntest";
		String projectName = "myselftest";
		
		boolean flag = false;
		flag = command.rmLocalPath(localPath);
		
		if(flag){
			
			flag = command.checkout(svnPath, localPath, username, password);
		}
		if(flag){
			flag = command.mergeToBranch(trunkUrl, localPath, username, password, projectName);
		}
		if(flag){
			flag = command.resolve(localPath,username,password);
		}
		if(flag){
			flag = command.svnadd(localPath, username, password);
		}
		if(flag){
			flag = command.svnrm(localPath, username, password);
		}
		if(flag){
			flag = command.svnci(localPath, username, password);
		}
		
		if(flag){
			view.setViewName("index");
		}else{
			view.setViewName("false");
		}
		return view;
	}
}
