package com.team1389.watch;

import edu.wpi.first.wpilibj.tables.ITable;

public interface CompositeWatchable extends Watchable {

	public Watchable[] getSubWatchables();

	@Override
	default void publishUnderName(String name, ITable table) {

		for (Watchable w : getSubWatchables()) {
			w.publish(name, table);
		}
	}

	// TODO fix these
	@Override
	public default String getPrintString() {
		String s = "";
		return s;
	}

	@Override
	public default double getLoggable() {
		return 0;
	}
}
