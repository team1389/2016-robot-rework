package org.usfirst.frc.team1389.systems;

import com.team1389.hardware.inputs.DigitalInput;
import com.team1389.hardware.interfaces.inputs.PercentIn;
import com.team1389.hardware.interfaces.outputs.PercentOut;
import com.team1389.hardware.watch.BooleanInfo;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.Watchable;
import com.team1389.system.System;

public class IntakeSystem implements System, Watchable{
	PercentIn joystick;
	PercentOut motor;
	DigitalInput IRSensors;
	DigitalInput override;
	public IntakeSystem(PercentOut motor,PercentIn joystick, DigitalInput IRSensors,DigitalInput override){
		this.motor=motor;
		this.joystick=joystick;
		this.IRSensors=IRSensors;
		this.override=override;
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
			motor.set(-.15);
		}else{
			motor.set(joystick.get());
		}
	}

	@Override
	public void init() {
		
	}

}
