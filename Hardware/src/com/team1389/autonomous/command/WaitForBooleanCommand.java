package com.team1389.autonomous.command;

import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.inputs.interfaces.BooleanSource;

public class WaitForBooleanCommand extends Command{
	BooleanSource untilTrue;
	public WaitForBooleanCommand(BooleanSource untilTrue){
		this.untilTrue=untilTrue;
	}
	@Override
	protected boolean execute() {
		return untilTrue.get();
	}
	
}
