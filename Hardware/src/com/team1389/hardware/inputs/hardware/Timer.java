package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.inputs.interfaces.ScalarInput;

public class Timer implements ScalarInput{
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
	
}
