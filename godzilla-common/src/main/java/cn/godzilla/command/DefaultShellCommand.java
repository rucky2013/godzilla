package cn.godzilla.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultShellCommand extends AbstractShellCommand {

	Runnable runnable = null;
	
	@Override
	protected void startdoLog(CommandEnum type) {
		switch(type) {
		case INFO:
			runnable = new InfoRunnable();
			break;
		case VERSION:
			runnable = new VersionRunnable();
			break;
		case MERGE:
		case COMMIT:
		case COMMIT_RESOLVE:
			runnable = new DefaultRunnable();
			break;
		case TAIL:
			runnable = new TailRunnable();
			break;
		case LSJAR:
			runnable = new LsjarRunnable();
			break;
		case MVN:
			runnable = new MvnRunnable();
			break;
		case CPWAR:
		case GODZILLA:
			runnable = new DefaultRunnable();
			break;
		default:
			signal();
			return ;
		}
		new Thread(runnable).start();
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

		protected void doLineNotNullBusi() {
			shellReturnThreadLocal.set(line);
		}
		
		protected void doLineNullBusi() {
			signal();
		}
	}
	
	class MvnRunnable extends abstractSvnCommandRunnable {
		
		String deployLog = "<p>********maven 打包详情********<p><br />";
		
		@Override
		protected void doLineNotNullBusi() {
			shellReturnThreadLocal.set(line);
			if(line.contains("BUILD FAILURE") || line.contains("[ERROR]")) {
				mvnBuildThreadLocal.set(FAILURE);
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
			deployLogThreadLocal.set(deployLog);
			signal();
		}
	}
	
	class LsjarRunnable extends abstractSvnCommandRunnable {
		
		String jarlog = "<p>********jar lib列表详情********<p><br />";

		@Override
		protected void doLineNotNullBusi() {
			jarlog = jarlog + line + "<br >";
			shellReturnThreadLocal.set(line);
		}
		
		@Override
		protected void doLineNullBusi() {
			jarlog = jarlog + "<p>";
			jarlogThreadLocal.set(jarlog);
			signal();
		}
	}
	
	class TailRunnable extends abstractSvnCommandRunnable {
		
		String catalinaLog = "<p>********catalina.out详情********<p><br />";
		
		@Override
		protected void doLineNotNullBusi() {
			catalinaLog = catalinaLog + line + "<br/>";
			catalinaLogThreadLocal.set(catalinaLog);
		}
		
		@Override
		protected void doLineNullBusi() {
			catalinaLog = catalinaLog + "</p>";
			catalinaLogThreadLocal.set(catalinaLog);
		}
	}
	
	class DefaultRunnable extends abstractSvnCommandRunnable {
	}
	
	class VersionRunnable extends abstractSvnCommandRunnable {

		@Override
		protected void doLineNotNullBusi() {
			if(line.startsWith("versionr")) {
				String version = line.replaceAll("versionr", "");
				svnVersionThreadLocal.set(version);
			}
			shellReturnThreadLocal.set(line);
		}
		
		@Override
		protected void doLineNullBusi() {
			signal();
		}
		
	}
	class InfoRunnable extends abstractSvnCommandRunnable {

		String message = "<p>********svn info详情********<p><br />";
		
		@Override
		protected void doLineNotNullBusi() {
			message = message + line + "<br />";
			shellReturnThreadLocal.set(line);
		}
		
		@Override
		protected void doLineNullBusi() {
			message = message + "<p>";
			echoMessageThreadLocal.set(message);
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
