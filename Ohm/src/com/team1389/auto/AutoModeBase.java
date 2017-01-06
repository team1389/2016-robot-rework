package com.team1389.auto;

import com.team1389.command_framework.command_base.Command;

/**
 * An abstract class that is the basis of the robot's autonomous routines.
 */
public abstract class AutoModeBase {

	/**
	 * frequency of command tick calls during
	 * {@link #runCommand(Command command)}
	 */
	protected static final double m_update_rate = 1.0 / 50.0;

	/**
	 * whether this auto mode is currently running
	 */
	protected boolean m_active = false;

	/**
	 * executes this auto mode. <br>
	 * routines are intended to run a series of simultaneous and sequential
	 * commands
	 * 
	 * @throws AutoModeEndedException
	 *             if the auto mode is forced to exit early
	 */
	protected abstract void routine() throws AutoModeEndedException;

	/**
	 * runs the auto mode with error handling and calls {@link #done()} when
	 * routine exits
	 */
	public void run() {
		m_active = true;
		try {
			routine();
		} catch (AutoModeEndedException e) {
			System.out.println("Auto mode done, ended early");
			return;
		}
		done();
		System.out.println("Auto mode done");
	}

	/**
	 * called when this auto finished running its routine <br>
	 * overload this method to run code when routine exits, regardless of
	 * whether it ended prematurely
	 */
	public void done() {
	}

	/**
	 * stops the current auto <br>
	 * calling this method will cause any commands running as part of this auto
	 * routine to be cancelled, and the auto will exit immediately.
	 */
	public void stop() {
		m_active = false;
	}

	/**
	 * @return whether this autoMode is currently running its routine
	 */
	public boolean isActive() {
		return m_active;
	}

	/**
	 * checks if auto mode should be running <br>
	 * @return whether the auto mode is supposed to be running 
	 * @throws AutoModeEndedException if the auto mode is not supposed to be running
	 */
	protected boolean isActiveWithThrow() throws AutoModeEndedException {
		if (!isActive()) {
			throw new AutoModeEndedException();
		}
		return isActive();
	}

	/**
	 * executes the given command every {@link #m_update_rate} seconds
	 * returns when command is complete or {@link #m_active} is set to false
	 * @param action the command to run
	 * @throws AutoModeEndedException if {@link #m_active} is set to false prematurely
	 */
	public void runCommand(Command action) throws AutoModeEndedException {
		isActiveWithThrow();
		action.init();
		boolean isFinished = false;
		while (isActiveWithThrow() && !isFinished) {
			isFinished = action.exec();
			long waitTime = (long) (m_update_rate * 1000.0);
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		action.done();
	}

}