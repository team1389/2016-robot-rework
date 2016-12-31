package com.team1389.watch.info;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.tables.ITable;

public class BooleanInfo extends SimpleWatchable {
	/**
	 * Sets BooleanInfo method in variable to class variable Calls superclass to handle name in {@link #Info(String name)}
	 * 
	 * @param name the name of hardware producing info
	 * @param in where (method) to get information
	 */
	BooleanSupplier in;

	public BooleanInfo(String name, BooleanSupplier in) {
		super(name);
		this.in = in;
	}

	@Override
	public void publishUnderName(String name, ITable subtable) {
		subtable.putBoolean(name, in.getAsBoolean());
	}

	@Override
	public String getPrintString() {
		return getName() + ": " + in.getAsBoolean();
	}

	@Override
	public double getLoggable() {
		return in.getAsBoolean() ? 1 : 0;
	}

}
