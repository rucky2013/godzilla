package cn.godzilla.command;

public interface Command {

	void execute(String shellCommand, CommandEnum type);
	
}
