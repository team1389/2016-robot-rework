package com.team1389.command_framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Supplier;

import com.team1389.command_framework.command_base.Command;

/**
 * contains useful static operations for commands
 * 
 * @author amind
 *
 */
public class CommandUtil {
	/**
	 * combines the given array of commands into a single command that runs the array of commands simultaneously
	 * @param commands the array of commands to combine
	 * @return the simultaneous combined command
	 */
	public static Command combineSequential(Command... commands) {
		return new Command() {
			int currentIndex = 0;

			@Override
			public boolean execute() {
				if (currentIndex >= commands.length) {
					return true;
				}
				boolean isFinished = commands[currentIndex].exec();
				if (isFinished) {
					System.out.println("next command");
					isFinished = false;
					currentIndex++;
				}
				return isFinished && currentIndex >= commands.length;
			}

		};
	}
	/**
	 * combines the given array of commands into a single command that runs the array of commands simultaneously
	 * @param commands the array of commands to combine
	 * @return the combined command
	 */
	public static Command combineSimultaneous(Command... commands) {

		return new Command() {
			List<Command> runningCommands;

			@Override
			public void initialize() {
				runningCommands = new ArrayList<>();
				runningCommands.addAll(Arrays.asList(commands));
				for (Command c : runningCommands) {
					c.init();
				}
			}

			@Override
			public boolean execute() {
				ListIterator<Command> iter = runningCommands.listIterator();
				while (iter.hasNext()) {
					if (iter.next().exec()) {
						iter.remove();
					}
				}
				return runningCommands.isEmpty();
			}

		};
	}
	/**
	 * creates a command out of the given supplier, which will be called as the command's execute method
	 * @param execute the supplier to execute each command tick
	 * @return a new command
	 */
	public static Command createCommand(Supplier<Boolean> execute) {
		return new Command() {
			@Override
			protected boolean execute() {
				return execute.get();
			}
		};
	}

	/**
	 * runs a command and hangs the thread until the command finishes
	 * 
	 * @param command the command to execute
	 */
	public static void executeCommand(Command command) {
		boolean isFinished = false;
		if (!isFinished) {
			isFinished = command.exec();
		}
	}
}