package com.team1389.system;

import com.team1389.command_framework.CommandScheduler;
import com.team1389.command_framework.command_base.Command;
import com.team1389.watch.CompositeWatchable;

/**
 * a Subsystem stores a group of related tasks that can be run periodically when the SubSystem is attached to a {@link SystemManager} in addition, subsystems must be watchable, and should offer relevant information as subWatchables (see {@link CompositeWatchable})
 * 
 * @author amind
 *
 */
public abstract class Subsystem implements CompositeWatchable {

	/**
	 * creates the subsystem
	 */
	public Subsystem() {
		scheduler = new CommandScheduler();
		COMMAND_CANCEL = () -> {
			scheduler.cancelAll();
		};
	}

	private CommandScheduler scheduler;
	protected Runnable COMMAND_CANCEL;

	protected final void thisUpdate() {
		update();
		scheduler.update();
	}

	/**
	 * initialize the subsystem - called once before periodic update calls begin
	 */
	public abstract void init();

	/**
	 * called periodically while the system is attached to a ticking {@link SystemManager}
	 */
	public abstract void update();

	protected void schedule(Command command) {
		scheduler.schedule(command);
	}

}
