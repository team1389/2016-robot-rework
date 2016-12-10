package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.inputs.interfaces.BinaryInput;

public class TimedBoolean implements BinaryInput{
	Timer timer;
	double time;
	public TimedBoolean(double time){
		timer=new Timer();
		this.time=time;
	}
	public void start(){
		timer.zero();
	}
	@Override
	public boolean get() {
		return timer.get()>time;
	}
}
