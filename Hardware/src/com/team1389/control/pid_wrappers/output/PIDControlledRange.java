package com.team1389.control.pid_wrappers.output;

import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.valueTypes.Value;

public class PIDControlledRange<T extends Value> implements edu.wpi.first.wpilibj.PIDOutput{
	
	RangeOut<T> outputRange;
	
	public PIDControlledRange(RangeOut<T> voltageOutput) {
		this.outputRange = voltageOutput;
	}

	@Override
	public void pidWrite(double output) {
		outputRange.set(output);
	}

}
