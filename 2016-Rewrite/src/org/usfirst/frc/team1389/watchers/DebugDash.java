package org.usfirst.frc.team1389.watchers;

import com.team1389.watch.Watcher;

import edu.wpi.first.wpilibj.tables.ITable;

public class DebugDash extends Watcher {
	static DebugDash instance = new DebugDash();
	static final boolean IS_DEBUGGING = true;

	public static DebugDash getInstance() {
		return instance;
	}

	@Override
	public void publish(ITable table) {
		if (IS_DEBUGGING) {
			super.publish(table);
		}
	}

	public void display() {
		publish(Watcher.DASHBOARD);
	}

	@Override
	public void clearWatchers() {
		// TODO doesnt do anything
		super.clearWatchers();
	}

}
