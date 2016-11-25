package com.team1389.hardware.watch;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class StringInfo extends Info{
	StringSource source;

	public StringInfo(String name, StringSource source2) {
		super(name);
		this.source=source2;
	}
	public interface StringSource{
		public String get();
	}
	@Override
	public void display() {
		SmartDashboard.putString(name, source.get());
	}
	@Override
	public String toString() {
		return name+": "+source.get();
	}
	@Override
	public double loggable() {
		return -1;
	}
}
