package cn.godzilla.command;

public interface Command {

	public void execute(String shellCommand, CommandEnum type);
	
}
