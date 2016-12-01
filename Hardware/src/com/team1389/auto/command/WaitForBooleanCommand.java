package com.team1389.auto.command;

import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.inputs.interfaces.BooleanSource;

/**
 * This command waits until the given {@link BooleanSource} returns true
 * 
 * @author amind
 *
 */
public class WaitForBooleanCommand extends Command {
	BooleanSource untilTrue;

	/**
	 * 
	 * @param untilTrue the {@link BooleanSource} to wait for
	 */
	public WaitForBooleanCommand(BooleanSource untilTrue) {
		this.untilTrue = untilTrue;
	}

	@Override
	protected boolean execute() {
		return untilTrue.get();
	}

}
