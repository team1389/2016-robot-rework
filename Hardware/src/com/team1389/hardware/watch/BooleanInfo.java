package com.team1389.hardware.watch;

import com.team1389.hardware.interfaces.inputs.BooleanSource;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class BooleanInfo extends Info{
	BooleanSource in;
	public BooleanInfo(String name,BooleanSource in){
		super(name);
		this.in=in;
	}
	@Override
	public void display() {
		SmartDashboard.putBoolean(name, in.get());
	}

	@Override
	public String toString() {
		return name+" "+in.get();
	}

	@Override
	public double loggable() {
		return in.get()?1d:0d;
	}
}
