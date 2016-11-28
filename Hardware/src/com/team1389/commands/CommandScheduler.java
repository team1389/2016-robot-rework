package com.team1389.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.team1389.watch.Info;
import com.team1389.watch.StringInfo;
import com.team1389.watch.Watchable;

public class CommandScheduler implements Watchable {
	List<CommandWrapper> executing;

	public CommandScheduler() {
		executing = new ArrayList<CommandWrapper>();
	}

	public void schedule(Command command) {
		executing.add(new CommandWrapper(command));
	}

	public void update() {
		ListIterator<CommandWrapper> iter = executing.listIterator();
		while (iter.hasNext()) {
			if (iter.next().execute()) {
				iter.remove();
			}
		}
	}

	public boolean isFinished() {
		return executing.isEmpty();
	}

	class CommandWrapper {
		boolean initialized;
		Command command;

		public CommandWrapper(Command command) {
			reset();
			this.command = command;
		}

		public void init() {
			initialized = true;
			command.init();
		}

		/**
		 * This will be run at a repeated interval by the controller until it
		 * returns true
		 * 
		 * @return whether the command is finished
		 */
		public boolean execute() {
			if (!initialized) {
				init();
			}
			boolean isFinished = command.execute();
			if (isFinished) {
				reset();
			}
			return isFinished;
		}

		public void reset() {
			initialized = false;
		}

		public String toString() {
			return command.toString();
		}
	}

	public void cancelAll() {
		executing = new ArrayList<CommandWrapper>();
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Info[] getInfo() {
		return new Info[] { new StringInfo("Current Command list", () -> {
			return executing.toString();
		}) };
	}
}
