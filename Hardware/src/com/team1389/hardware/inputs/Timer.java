package com.team1389.hardware.inputs;

import com.team1389.hardware.interfaces.inputs.RangeIn;

public class Timer extends RangeIn{
	edu.wpi.first.wpilibj.Timer timer;
	public Timer(){
		super(0d,Double.MAX_VALUE);
		timer=new edu.wpi.first.wpilibj.Timer();
		timer.start();
	}
	public void zero(){
		timer.reset();
	}
	@Override
	public double get() {
		return timer.get();
	}
	
}
