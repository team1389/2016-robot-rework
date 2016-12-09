package com.team1389.command_framework.command_base;

/**
 * A command is an object that should be used in an iterative matter. It runs a step of itself in {@link #exec() exec}, and runs other methods at different
 * points of the command's lifestyle.
 * @author Josh
 *
 */
public abstract class Command {
	boolean initialized;
	boolean finished;

	/**
	 * Constructs the new command but does not start it yet.
	 */
	public Command() {
		reset();
	}


	/**
	 * Called by {@link #init() init}. Should be overridden if necessary.
	 */
	protected void initialize() {

	}

	/**
	 * Called when the command is finished executing. Should be overridden if necessary.
	 */
	public void done() {
	}

	/**
	 * This will be run at a repeated interval by the controller until it
	 * returns true. Called by {@link #exec() exec}
	 * 
	 * @return Whether the command is finished
	 */

	protected abstract boolean execute();

	/**
	 * Called to start the command. Calls {@link #initialize() initialize}
	 */
	public final void init() {
		initialize();
		initialized = true;
	}

	/**
	 * Progresses the command through one step. If this is called when the command is not initialized, it will initialize the command.
	 * @return Whether the command is finished
	 */
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

	/**
	 * 
	 * @return Whether the command is finished
	 */
	public final boolean isFinished() {
		return finished;
	}

	/**
	 * Resets the command back to its beginning state, ready to be initialized again
	 */
	public void reset() {
		initialized = false;
	}
}