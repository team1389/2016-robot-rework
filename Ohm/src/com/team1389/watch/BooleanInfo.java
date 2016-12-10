package com.team1389.watch;

import com.team1389.hardware.inputs.interfaces.BinaryInput;
import com.team1389.hardware.inputs.interfaces.BinaryInput;

import edu.wpi.first.wpilibj.tables.ITable;

public class BooleanInfo extends Info{
	/**
	 * Sets BooleanInfo method in variable to class variable
	 * Calls superclass to handle name in {@link #Info(String name)}
	 * @param name the name of hardware producing info
	 * @param in where (method) to get information			
	 */
	BinaryInput in;
	public BooleanInfo(String name,BinaryInput in){
		super(name);
		this.in=in;
	}
	@Override
	public void publish(ITable subtable) {
		subtable.putBoolean(name, in.get());
	}

	@Override
	public String toString() {
		return name+": "+in.get();
	}

	@Override
	public double loggable() {
		return in.get()?1d:0d;
	}
}
