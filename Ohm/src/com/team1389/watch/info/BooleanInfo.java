package com.team1389.watch.info;

import com.team1389.hardware.inputs.interfaces.BinaryInput;

import edu.wpi.first.wpilibj.tables.ITable;

public class BooleanInfo extends SimpleWatchable {
	/**
	 * Sets BooleanInfo method in variable to class variable Calls superclass to handle name in {@link #Info(String name)}
	 * 
	 * @param name the name of hardware producing info
	 * @param in where (method) to get information
	 */
	BinaryInput in;

	public BooleanInfo(String name, BinaryInput in) {
		super(name);
		this.in = in;
	}

	@Override
	public void publishUnderName(String name, ITable subtable) {
		subtable.putBoolean(name, in.get());
	}

	@Override
	public String getPrintString() {
		return getName() + ": " + in.get();
	}

	@Override
	public double getLoggable() {
		return in.get() ? 1d : 0d;
	}

}
