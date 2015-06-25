package cn.creditease.godzilla.svn;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SvnCommand {
	
	private final static Logger logger = LogManager.getLogger(SvnCommand.class);
	
	/**
	 * 从svn检出代码到本地
	 * @param svnPath
	 * @param localPath
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean checkout(String svnPath,String localPath,String username,String password){
		
		final String command = "svn checkout "+svnPath + " " +localPath +" --username "+username +" --password  " + password;
		
		logger.debug("*******SVN Checkout Begin******"+"from:"+svnPath+"   to->"+localPath +" by:"+username);
		
		BaseShellCommand baseShellCommand = new BaseShellCommand();
		if(baseShellCommand.execute(command)){
			
			logger.debug("******Checkout Success!*****");
			return true;
		}
		
		return false;
	}
	/**
	 * 分支合并  将主干代码合并到本地分支，冲突稍后处理 --accept postpone
	 * @param trunkUrl
	 * @param localPath
	 * @param username
	 * @param password
	 * @param projectName
	 * @return
	 */
	public boolean mergeToBranch(String trunkUrl,String localPath ,String username ,String password ,String projectName){
		
		String command = "svn merge "+trunkUrl +" "+localPath +"  --accept postpone  --username "+username+" --password "+password;
		
		logger.debug("**********mergeToBranch****"+command);
		BaseShellCommand baseShellCommand = new BaseShellCommand();
		if(baseShellCommand.execute(command)){
			return true;
		}
		
		return false;
	}
	/**
	 * 冲突标记解决  svn resolve --accept working localPath
	 * @param localPath
	 * @return
	 */
	public boolean resolve(String localPath,String username ,String password){
		
		//file=`svn st /home/godzilla/lzwdata/svntestbarnch/ |awk '{ if($1 == "C") { print $2 }}'` && test -z $file || svn add $file --username lizw --password 123456
		//String command ="svn st "+localPath+"  | awk '{if($1 == \"C\"){ print $2 }}' |xargs svn resolve --accept working " + localPath ;
		
		//test -z `svn st /home/godzilla/lzwdata/svntestbarnch | awk '{if($1=="C"){ print $2}}'` || svn resolve --accept working `svn st /home/godzilla/lzwdata/svntestbarnch |awk '{if($1=="C"){ print $2}}'`
		
		//String command = "file=`svn st "+localPath+" |awk '{if($1==\"C\"){ print $2}}'` && test -z $file || svn resolve --accept working $file --username "+username +" --password "+password;
		
		String command = "test -z `svn st "+localPath+" | awk '{if($1==\"C\"){ print $2}}'` || svn resolve --accept working `svn st "+localPath+" |awk '{if($1==\"C\"){ print $2}}'` --username "+username +" --password "+ password;
		
		logger.debug("**********resolve****"+command);
		BaseShellCommand baseShellCommand = new BaseShellCommand();
		if(baseShellCommand.execute(command)){
			return true;
		}
		return false ;
	}
	/**
	 * SVN批量添加 
	 * @param localPath
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean svnadd(String localPath ,String username ,String password){
		
		// file=`svn st /home/godzilla/lzwdata/svntestbarnch/ |awk '{ if($1 == "?") { print $2 }}'` && test -z $file || svn add $file --username lizw --password 123456
		
		String command = "file=`svn st "+localPath+" |awk '{if($1==\"?\"){ print $2}}'` && test -z $file || svn add $file --username "+username +" --password "+password;
		//String command = "svn st "+localPath + "  |grep ? |awk '{print $2}' |xargs svn add --username "+username +" --password "+password;
		logger.debug("**********svnadd****"+command);
		BaseShellCommand baseShellCommand = new BaseShellCommand();
		
		if(baseShellCommand.execute(command)){
			return true ;
		}
		
		return false;
	}
	/**
	 * SVN批量删除文件
	 * @param localPath
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean svnrm(String localPath ,String username,String password){
		
		
		//file=`svn st /home/godzilla/lzwdata/svntestbarnch/ |awk '{ if($1 == "!") { print $2 }}'` && test -z $file || svn add $file --username lizw --password 123456
		//String command = "svn st "+localPath+" | awk '{if($1 == \"!\") {print $2}}' |xargs svn rm  --username "+username +"  --password "+password ;
		
		String command = "file=`svn st "+localPath+" |awk '{if($1==\"!\"){ print $2}}'` && test -z $file || svn rm $file --username "+username +" --password "+password;
		
		logger.debug("*********svnrm****"+command);
		
		BaseShellCommand baseShellCommand = new BaseShellCommand();
		
		if(baseShellCommand.execute(command)){
			return true;
		}
	
		return false;
	}
	/**
	 * 批量提交,请先执行批量添加、
	 * @param localPath
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean svnci(String localPath,String username,String password){
		
		String command = "svn ci "+localPath +" -m "+username+" --username "+username +" --password "+password;
		
		logger.debug("*******svnci****"+command);
		
		BaseShellCommand baseShellCommand = new BaseShellCommand();
		
		if(baseShellCommand.execute(command)){
			return true;
		}
		return false;
	}
	/**
	 * 清除本地代码副本
	 * @param localPath
	 * @return
	 */
	public boolean rmLocalPath(String localPath){
		
		String command = "rm -rf "+localPath;
		
		logger.debug("*****rmLocalPath****"+command);
		BaseShellCommand baseShellCommand = new BaseShellCommand();
		
		if(baseShellCommand.execute(command)){
			return true;
		}
		return false;
		
	}
	
	/**
	 * 更新
	 * @param localPath
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean svnUp(String localPath,String username ,String password){
		
		String command ="svn up "+localPath+" --username "+username +" --password "+password;
		
		logger.debug("************svnUp******"+command);
		
		BaseShellCommand baseShellCommand = new BaseShellCommand();
		
		if(baseShellCommand.execute(command)){
			return true ;
		}
		return false ;
	}
	
	/**
	 * 将分支代码合并到主干 ，冲突文件以分支为准
	 * @param branchUrl
	 * @param localPath
	 * @param username
	 * @param password
	 * @param projectName
	 * @return
	 */
	public boolean mergeToTrunk(String branchUrl,String localPath ,String username ,String password ,String projectName){
		
		String command = "svn merge "+branchUrl +" "+localPath +"  --accept theirs-full  --username "+username+" --password"+password;
		
		logger.debug("**********mergeToBranch*****"+command);
		BaseShellCommand baseShellCommand = new BaseShellCommand();
		if(baseShellCommand.execute(command)){
			return true;
		}
		
		return false;
	}
	
	/**
	 * checkIn
	 * @param localPath
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean checkin(String localPath ,String username ,String password){
		
		String command = "svn ci "+localPath + "  --username "+username +"  --password"+password ;
		
		logger.debug("*****checkin****"+command);
		
		BaseShellCommand baseShellCommand = new BaseShellCommand();
		
		if(baseShellCommand.execute(command)){
			return true;
		}
		return false;
	}
	
	
	
}
