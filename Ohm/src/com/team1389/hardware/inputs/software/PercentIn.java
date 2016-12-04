package com.team1389.hardware.inputs.software;

import com.team1389.hardware.inputs.interfaces.ScalarInput;
import com.team1389.hardware.value_types.Percent;

/**
 * An input that gives a value from -1 to 1
 * 
 * @author Jacob Prinz
 */
public class PercentIn extends RangeIn<Percent>{
	public PercentIn(ScalarInput<Percent> val){
		super(Percent.class,val,-1,1);
	}
	protected PercentIn(RangeIn<?> in){
		this(ScalarInput.mapToPercent(in.input, in.min, in.max));
	}
	public PercentIn applyDeadband(double deadband) {
		input=ScalarInput.applyDeadband(input, deadband);
		return this;
	}
}
