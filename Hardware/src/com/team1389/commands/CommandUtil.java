package com.team1389.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import com.team1389.commands.command_base.Command;
import com.team1389.commands.command_base.CommandSource;

public class CommandUtil {
	public static Command combineSequential(Command...commands){
		return new Command(){
			int currentIndex=0;
			@Override
			public boolean execute() {
				if(currentIndex>=commands.length){
					return true;
				}
				boolean isFinished=commands[currentIndex].exec();
				if(isFinished){
					System.out.println("next command");
					isFinished=false;
					currentIndex++;
				}
				return isFinished&&currentIndex>=commands.length;
			}
		
		};
	}
	public static Command combineSimultaneous(Command...commands){

		return new Command(){
			List<Command> runningCommands;
			@Override
			public void initialize(){
				runningCommands=new ArrayList<>();
				runningCommands.addAll(Arrays.asList(commands));
				for(Command c:runningCommands){
					c.init();
				}
			}
			@Override
			public boolean execute() {
				ListIterator<Command> iter = runningCommands.listIterator();
				while(iter.hasNext()){
				    if(iter.next().exec()){
				        iter.remove();
				    }
				}
				return runningCommands.isEmpty();
			}
		
		};
	}
	public static Command createCommand(CommandSource execute){
		return new Command(){
			@Override
			protected boolean execute() {
				return execute.execute();
			}
		};
	}
	/**
	 * runs a command and hangs the thread until the command finishes
	 * @param command
	 */
	public static void executeCommand(Command command){
		boolean isFinished=false;
		if(!isFinished){
			isFinished=command.exec();
		}
	}
}