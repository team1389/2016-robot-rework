package com.team1389.watch.info;

import edu.wpi.first.wpilibj.tables.ITable;

public abstract class Info {
	String name;

	public Info(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void publish(String pathFromParent, ITable table) {
		this.publishWithName(pathFromParent + "." + getName(), table);
	}

	protected abstract void publishWithName(String name, ITable table);

	@Override
	public abstract String toString();

	public abstract double loggable();
}
