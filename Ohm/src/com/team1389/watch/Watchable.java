package com.team1389.watch;

import edu.wpi.first.wpilibj.tables.ITable;

public interface Watchable {

	public String getName();

	void publishUnderName(String name, ITable table);

	public default void publish(String parent, ITable table) {
		publishUnderName(parent + "." + getName(), table);
	}

	public default void publish(ITable table) {
		publishUnderName(getName(), table);
	}

	public String getPrintString();

	public double getLoggable();
}
