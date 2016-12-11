package com.team1389.command_framework;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.team1389.command_framework.command_base.Command;
import com.team1389.watch.CompositeWatchable;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.StringInfo;

public class CommandScheduler implements CompositeWatchable {
	List<Command> executing;
	String name;

	public CommandScheduler() {
		this("Scheduler");
	}

	public CommandScheduler(String name) {
		this.name = name;
		executing = new ArrayList<Command>();
	}

	public void schedule(Command command) {
		executing.add(command);
	}

	public void update() {
		ListIterator<Command> iter = executing.listIterator();
		while (iter.hasNext()) {
			if (iter.next().exec()) {
				iter.remove();
			}
		}

	}

	public boolean isFinished() {
		return executing.isEmpty();
	}

	public void cancelAll() {
		executing = new ArrayList<Command>();
	}

	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { new StringInfo("Current Command list", () -> {
			return executing.toString();
		}) };
	}

	@Override
	public String getName() {
		return name;
	}
}
