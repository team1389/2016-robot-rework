package com.team1389.commands.command_base;
public abstract class Command {
	boolean initialized;
	public Command(){
		reset();
	}
	/**
	 * This will be run at a repeated interval by the controller until it returns true
	 * @return whether the command is finished
	 */
	public final void init(){
		initialize();
		initialized=true;
	}
	protected void initialize(){

	}
	protected abstract boolean execute();
	public final boolean exec(){
		if(!initialized){
			init();
		}
		boolean isFinished=execute();
		if(isFinished){
			done();
			reset();
		}
		return isFinished;
	}
	public void done(){}
	public void reset(){
		initialized=false;
	}
}