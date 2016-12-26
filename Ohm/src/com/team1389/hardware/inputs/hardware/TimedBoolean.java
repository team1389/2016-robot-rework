package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.inputs.interfaces.BooleanSupplier;

public class TimedBoolean implements BooleanSupplier{
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
	public Boolean get() {
		return timer.get()>time;
	}
}
