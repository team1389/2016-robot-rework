package com.team1389.hardware.inputs;

import com.team1389.hardware.interfaces.inputs.OpenRangeInput;

public class Timer implements OpenRangeInput{
	edu.wpi.first.wpilibj.Timer timer;
	public Timer(){
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

	@Override
	public double min() {
		return 0;
	}

	@Override
	public double max() {
		return Double.MAX_VALUE;
	}
	
}
