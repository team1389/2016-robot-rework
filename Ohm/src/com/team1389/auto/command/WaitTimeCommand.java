package com.team1389.auto.command;

import com.team1389.hardware.inputs.hardware.TimedBoolean;

public class WaitTimeCommand extends WaitForBooleanCommand{
	TimedBoolean timer;
	public WaitTimeCommand(double time){
		super(null);
		timer=new TimedBoolean(time);
		untilTrue=timer;
	}
	@Override
	public void initialize(){
		super.initialize();
		
	}


}
