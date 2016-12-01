
package com.team1389.autonomous.command;

import com.team1389.command_framework.command_base.Command;

public class DoNothingCommand extends Command {

	@Override
	public boolean execute() {
		return true;
	}

}