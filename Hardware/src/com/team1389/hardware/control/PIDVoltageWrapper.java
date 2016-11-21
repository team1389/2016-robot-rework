package com.team1389.hardware.control;

import com.team1389.hardware.interfaces.outputs.PercentRangeOutput;

import edu.wpi.first.wpilibj.PIDOutput;

public class PIDVoltageWrapper implements PIDOutput{
	
	PercentRangeOutput voltageOutput;
	
	public PIDVoltageWrapper(PercentRangeOutput voltageOutput) {
		this.voltageOutput = voltageOutput;
	}

	@Override
	public void pidWrite(double output) {
		voltageOutput.set(output);
	}

}
