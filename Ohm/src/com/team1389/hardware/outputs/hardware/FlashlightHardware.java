package com.team1389.hardware.outputs.hardware;

import com.team1389.hardware.outputs.interfaces.DigitalOut;

import edu.wpi.first.wpilibj.Solenoid;

public class FlashlightHardware {
	private Solenoid wpiSolenoid;
	public FlashlightHardware(int pcmPort){
		this.wpiSolenoid=new Solenoid(pcmPort);
	}
	public DigitalOut getDigitalOut(){
		return (boolean val)->{
			wpiSolenoid.set(val);
		};
	}
}
