package com.team1389.system;

import com.team1389.command_framework.CommandScheduler;
import com.team1389.command_framework.command_base.Command;
import com.team1389.watch.Watchable;

public abstract class System implements Watchable {
	public System() {
		scheduler = new CommandScheduler();
		enterDefaultMode();
		defaultModeListener = () -> {
			enterDefaultMode();
		};
	}

	private CommandScheduler scheduler;
	protected boolean inDefaultMode=true;;
	protected Runnable defaultModeListener;

	public final void update() {
		getInput();
		if (inDefaultMode) {
			defaultUpdate();
		} else {
			scheduler.update();
			inDefaultMode = scheduler.isFinished();
		}
	}

	public final void enterDefaultMode() {
		scheduler.cancelAll();
		inDefaultMode = true;
	}

	public abstract void init();

	public abstract void defaultUpdate();

	public abstract void getInput();

	protected void schedule(Command command) {
		scheduler.schedule(command);
		//inDefaultMode = false;
	}

}