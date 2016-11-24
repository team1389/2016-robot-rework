package com.team1389.hardware.watch;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NumberInfo extends Info{
	NumberSource source;
	public NumberInfo(String name,NumberSource source) {
		super(name);
		this.source=source;
	}
	public interface NumberSource{
		public double get();
	}
	@Override
	public void display() {
		SmartDashboard.putNumber(name, source.get());
	}
	@Override
	public String toString() {
		return name+" "+source.get();
	}
	@Override
	public double loggable() {
		return source.get();
	}
}
