package com.team1389.commands;

public interface Command {
	public default void init(){
		
	}
	public boolean execute();
}
