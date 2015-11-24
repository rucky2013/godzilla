package cn.godzilla.command;

public class TailShellCommand extends DefaultShellCommand {
	
	public void signal() {
		lock.lock();
		isSignaled = true;
		done.signal();
		lock.unlock();
	}
}
