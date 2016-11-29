package com.team1389.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.team1389.commands.command_base.Command;
import com.team1389.watch.Info;
import com.team1389.watch.StringInfo;
import com.team1389.watch.Watchable;

public class CommandScheduler implements Watchable {
	List<Command> executing;

	public CommandScheduler() {
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
				if(isFinished()){
					System.out.println("finished running all commands");
				}
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
