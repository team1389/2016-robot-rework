package org.usfirst.frc.team1389.systems;

import com.team1389.hardware.inputs.DigitalInput;
import com.team1389.hardware.interfaces.inputs.PercentRangeInput;
import com.team1389.hardware.interfaces.outputs.PercentRangeOutput;
import com.team1389.hardware.watch.BooleanInfo;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.Watchable;
import com.team1389.system.System;

public class IntakeSystem implements System, Watchable{
	PercentRangeInput joystick;
	PercentRangeOutput motor;
	DigitalInput IRSensors;
	DigitalInput override;
	public IntakeSystem(PercentRangeOutput motor,PercentRangeInput joystick, DigitalInput IRSensors,DigitalInput override){
		
	}
	@Override
	public String getName() {
		return "Intake System";
	}

	@Override
	public Info[] getInfo() {
		return new Info[]{
			new BooleanInfo("Has Ball",()->{return IRSensors.get();})
		};
	}

	@Override
	public void update() {
		if(IRSensors.get()&&!override.get()){
			motor.set(-.5);
		}else{
			motor.set(joystick.get());
		}
	}

	@Override
	public void init() {
		
	}

}
