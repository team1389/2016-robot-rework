package com.team1389.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class CommandScheduler {
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
	}

	public void cancelAll() {
		// TODO check if command need to do something before they die
		executing = new ArrayList<CommandWrapper>();
	}
}
