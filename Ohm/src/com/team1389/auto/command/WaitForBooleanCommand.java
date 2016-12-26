package com.team1389.auto.command;

import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.inputs.interfaces.BooleanSupplier;

/**
 * This command waits until the given {@link BooleanSupplier} returns true
 * 
 * @author amind
 *
 */
public class WaitForBooleanCommand extends Command {
	BooleanSupplier untilTrue;

	/**
	 * 
	 * @param untilTrue the {@link BooleanSupplier} to wait for
	 */
	public WaitForBooleanCommand(BooleanSupplier untilTrue) {
		this.untilTrue = untilTrue;
	}

	@Override
	protected boolean execute() {
		return untilTrue.get();
	}

}
