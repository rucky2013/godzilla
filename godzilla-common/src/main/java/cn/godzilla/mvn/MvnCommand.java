package cn.godzilla.mvn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MvnCommand extends MvnBaseCommand{
	
	private final Logger logger = LogManager.getLogger(MvnCommand.class);
	
	public boolean install(String pomUrl,String projectName,String env ,String username){
		
		String command = "mvn clean install -f  "+ pomUrl +" -P"+env;
		
		logger.debug("****************"+username+"-->execute:"+command +",on "+projectName);
		
		return this.execute(command, projectName, env, username);
	}
	
	public boolean deploy(String pomUrl,String projectName,String env ,String username){
		
		String command = "mvn clean deploy -f  "+ pomUrl +" -P"+env;
		
		logger.debug("****************"+username+"-->execute:"+command +",on "+projectName);
		
		return this.execute(command, projectName, env, username);
	}

}
