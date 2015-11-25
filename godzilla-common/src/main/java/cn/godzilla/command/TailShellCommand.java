package cn.godzilla.command;


public class TailShellCommand extends DefaultShellCommand {
	
	public String catalinaLog = "";
	
	public String shellReturn = "";
	
	public void signal() {
		lock.lock();
		isSignaled = true;
		done.signal();
		lock.unlock();
	}
	
	@Override
	protected void afterProcessShell(CommandEnum type) {
		switch(type) {
		case RESTART:
			catalinaLog = ((RestartAndTailRunnable)runnable).catalinaLog;
			break;
		default:
			return ;
		}
		shellReturn = 0+"";
	}
}
