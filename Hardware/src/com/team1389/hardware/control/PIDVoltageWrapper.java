package com.team1389.hardware.control;

import com.team1389.hardware.outputs.software.PercentOut;

import edu.wpi.first.wpilibj.PIDOutput;

public class PIDVoltageWrapper implements PIDOutput{
	
	PercentOut voltageOutput;
	
	public PIDVoltageWrapper(PercentOut voltageOutput) {
		this.voltageOutput = voltageOutput;
	}

	@Override
	public void pidWrite(double output) {
		voltageOutput.set(output);
	}

}
