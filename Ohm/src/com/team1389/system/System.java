package com.team1389.system;

import com.team1389.command_framework.CommandScheduler;
import com.team1389.command_framework.command_base.Command;
import com.team1389.watch.CompositeWatchable;

public abstract class System implements CompositeWatchable {
	public System() {
		scheduler = new CommandScheduler();
		COMMAND_CANCEL = () -> {
			scheduler.cancelAll();
		};
	}

	private CommandScheduler scheduler;
	protected Runnable COMMAND_CANCEL;

	public final void thisUpdate() {
		update();
		scheduler.update();
	}

	public abstract void init();

	public abstract void update();

	protected void schedule(Command command) {
		scheduler.schedule(command);
	}

}
