package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.inputs.interfaces.ScalarInput;
import com.team1389.hardware.value_types.Value;

public class Timer implements ScalarInput<Value>{
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
