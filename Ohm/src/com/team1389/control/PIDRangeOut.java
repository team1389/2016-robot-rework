package com.team1389.control;

import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Value;

public class PIDRangeOut<T extends Value> implements edu.wpi.first.wpilibj.PIDOutput {

	RangeOut<T> outputRange;

	public PIDRangeOut(RangeOut<T> voltageOutput) {
		this.outputRange = voltageOutput;
	}

	@Override
	public void pidWrite(double output) {
		outputRange.set(output);
	}
	public static <T extends Value> PIDRangeOut<T> get(RangeOut<T> out){
		return new PIDRangeOut<>(out);
	}

}
