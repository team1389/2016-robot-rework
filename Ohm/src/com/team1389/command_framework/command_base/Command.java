package com.team1389.command_framework.command_base;

public abstract class Command {
	boolean initialized;
	boolean finished;

	public Command() {
		reset();
	}

	/**
	 * This will be run at a repeated interval by the controller until it
	 * returns true
	 * 
	 * @return whether the command is finished
	 */

	protected void initialize() {

	}

	public void done() {
	}

	protected abstract boolean execute();

	public final void init() {
		initialize();
		initialized = true;
	}

	public final boolean exec() {
		if (!initialized) {
			init();
		}
		finished = execute();
		if (finished) {
			done();
			reset();
		}
		return finished;
	}

	public final boolean isFinished() {
		return finished;
	}

	public void reset() {
		initialized = false;
	}
}