package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.value_types.Position;

import edu.wpi.first.wpilibj.AnalogPotentiometer;

public class PotentiometerHardware {
	private AnalogPotentiometer wpiPot;
	double port;
	public PotentiometerHardware(int port){
		this.wpiPot=new AnalogPotentiometer(port);
		this.port=port;
	}
	public RangeIn<Position> getAnalogInput(){
		return new RangeIn<>(Position.class,()->{return wpiPot.get();},0,1);
	}
}
