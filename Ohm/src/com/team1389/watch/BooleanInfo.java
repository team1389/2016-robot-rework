package com.team1389.watch;

import com.team1389.hardware.inputs.interfaces.BooleanSource;

import edu.wpi.first.wpilibj.tables.ITable;

public class BooleanInfo extends Info{
	BooleanSource in;
	public BooleanInfo(String name,BooleanSource in){
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
