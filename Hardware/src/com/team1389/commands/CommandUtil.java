package com.team1389.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class CommandUtil {
	public static Command combineSequential(Command...commands){
		System.out.println(commands);
		return new Command(){
			int currentIndex=0;
			@Override
			public boolean execute() {
				if(currentIndex>=commands.length){
					return true;
				}
				boolean isFinished=commands[currentIndex].execute();
				if(isFinished){
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
			public void init(){
				runningCommands=new ArrayList<>();
				runningCommands.addAll(Arrays.asList(commands));
			}
			@Override
			public boolean execute() {
				ListIterator<Command> iter = runningCommands.listIterator();
				while(iter.hasNext()){
				    if(iter.next().execute()){
				        iter.remove();
				    }
				}
				return runningCommands.isEmpty();
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
			isFinished=command.execute();
		}
	}
}