package cn.godzilla.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultShellCommand extends AbstractShellCommand {

	DefaultRunnable runnable = null;
	
	@Override
	protected void startdoLog(CommandEnum type) {
		switch(type) {
		case INFO:
			runnable = new InfoRunnable();
			new Thread(runnable).start();
			break;
		case VERSION:
			runnable = new VersionRunnable();
			new Thread(runnable).start();
			break;
		case MERGE:
		case COMMIT:
		case COMMIT_RESOLVE:
		case CPWAR:
		case GODZILLA:
			runnable = new DefaultRunnable();
			new Thread(runnable).start();
			break;
		case RESTART:
			runnable = new RestartAndTailRunnable();
			new Thread(runnable).start();
			break;
		case LSJAR:
			runnable = new LsjarRunnable();
			new Thread(runnable).start();
			break;
		case MVN:
			runnable = new MvnRunnable();
			new Thread(runnable).start();
			break;
		default:
			signal();
			return ;
		}
	}
	
	@Override
	protected void afterProcessShell(CommandEnum type) {
		switch(type) {
		case INFO:
			echoMessageThreadLocal.set(((InfoRunnable)runnable).echoMessage);
			break;
		case VERSION:
			svnVersionThreadLocal.set(((VersionRunnable)runnable).svnVersion);
			break;
		case MERGE:
		case COMMIT:
		case COMMIT_RESOLVE:
		case CPWAR:
		case GODZILLA:
			break;
		case RESTART:
			catalinaLogThreadLocal.set(((RestartAndTailRunnable)runnable).catalinaLog);
			break;
		case LSJAR:
			jarlogThreadLocal.set(((LsjarRunnable)runnable).jarlog);
			break;
		case MVN:
			deployLogThreadLocal.set(((MvnRunnable)runnable).deployLog);
			mvnBuildThreadLocal.set(((MvnRunnable)runnable).mvnBuild);
			break;
		default:
			return ;
		}
		shellReturnThreadLocal.set(runnable.shellReturn);
	}
	
	abstract class abstractSvnCommandRunnable implements Runnable {
		
		String line = null;
		@Override
		public void run() {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while(!stop) {
				try {
					line = bufferedReader.readLine();
				} catch(IOException e) {
					signal();
					break;
				}
				if(line!=null) {
					doLineNotNullBusi();
				} else {
					doLineNullBusi();
					break;
				}
			}
		}

		abstract protected void doLineNotNullBusi() ;
		
		abstract protected void doLineNullBusi() ;
	}
	
	class DefaultRunnable extends abstractSvnCommandRunnable {
		
		String shellReturn = "";
		
		protected void doLineNotNullBusi() {
			shellReturn = line;
		}
		
		protected void doLineNullBusi() {
			signal();
		}
	}
	
	class MvnRunnable extends DefaultRunnable {
		
		String deployLog = "<p>********maven 打包详情********<p><br />";
		
		String mvnBuild = SUCCESS;
		@Override
		protected void doLineNotNullBusi() {
			shellReturn =line;
			if(line.contains("BUILD FAILURE") || line.contains("[ERROR]")) {
				mvnBuild = FAILURE;
			}
			Pattern pattern = Pattern.compile("^[0-9]+.+");
			Matcher matcher = pattern.matcher(line);
			if(matcher.matches()) {
				return ;
			} else {
				deployLog = deployLog + line + "<br />";
			}
		}
		
		@Override
		protected void doLineNullBusi() {
			deployLog = deployLog + "</p>";
			signal();
		}
	}
	
	class LsjarRunnable extends DefaultRunnable {
		
		String jarlog = "<p>********jar lib列表详情********<p><br />";

		@Override
		protected void doLineNotNullBusi() {
			jarlog = jarlog + line + "<br >";
			shellReturn = line;
		}
		
		@Override
		protected void doLineNullBusi() {
			jarlog = jarlog + "<p>";
			signal();
		}
	}
	
	class RestartAndTailRunnable extends DefaultRunnable {
		
		String catalinaLog = "<p>********catalina.out详情********<p><br />";
		
		@Override
		protected void doLineNotNullBusi() {
			catalinaLog = catalinaLog + line + "<br/>";
		}
		
		@Override
		protected void doLineNullBusi() {
			catalinaLog = catalinaLog + "</p>";
		}
	}
	
	class VersionRunnable extends DefaultRunnable {
		
		String svnVersion = "";
		@Override
		protected void doLineNotNullBusi() {
			if(line.startsWith("versionr")) {
				String version = line.replaceAll("versionr", "");
				svnVersion = version;
			}
			shellReturn = line;
		}
		
		@Override
		protected void doLineNullBusi() {
			signal();
		}
		
	}
	class InfoRunnable extends DefaultRunnable {

		String echoMessage = "<p>********svn info详情********<p><br />";
		
		@Override
		protected void doLineNotNullBusi() {
			echoMessage = echoMessage + line + "<br />";
			shellReturn = line;
		}
		
		@Override
		protected void doLineNullBusi() {
			echoMessage = echoMessage + "<p>";
			signal();
		}
		
	}
	
	private void signal() {
		lock.lock();
		isSignaled = true;
		done.signal();
		lock.unlock();
	}
}
