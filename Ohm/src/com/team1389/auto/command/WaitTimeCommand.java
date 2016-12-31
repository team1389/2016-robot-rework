package com.team1389.auto.command;

import com.team1389.hardware.inputs.hardware.TimedBoolean;
/**
 * This command waits a given time
 * @author Kenneth
 *
 */
public class WaitTimeCommand extends WaitForBooleanCommand{
	TimedBoolean timer;
	/**
	 * @param amount of time to wait
	 */
	public WaitTimeCommand(double time){
		super(null);
		timer=new TimedBoolean(time);
		untilTrue=timer;
	}
	/**
	 * initializes WaitForBooleanCommand
	 */
	@Override
	public void initialize(){
		super.initialize();
		
	}


}
