package com.team1389.control;

import com.team1389.configuration.PIDConfiguration;
import com.team1389.configuration.PIDConstants;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.PIDTunableValue;
import com.team1389.hardware.value_types.Value;

public class PIDController<O extends Value, I extends PIDTunableValue> extends edu.wpi.first.wpilibj.PIDController {
	private RangeOut<O> output;
	private RangeIn<I> source;
	private RangeOut<I> setpointSetter;
	public PIDController(double kP, double kI, double kD, RangeIn<I> source, RangeOut<O> output) {
		super(kP, kI, kD, PIDRangeIn.get(source), PIDRangeOut.get(output));
		this.source = source;
		this.output = output;
		setOutputRange(output.min(), output.max());
		setInputRange(source.min(), source.max());
		this.setpointSetter = new RangeOut<I>((double setpoint) -> {
			setSetpoint(setpoint);
			enable();
		}, source.min(), source.max());
	}

	public PIDController(PIDConstants constants, RangeIn<I> source, RangeOut<O> output) {
		this(constants.p, constants.i, constants.d, source, output);
	}

	public PIDController(PIDConfiguration config, RangeIn<I> source, RangeOut<O> output) {
		this(config.pidConstants, source, output);
		if (config.isSensorReversed) {
			source.invert();
		}
		setContinuous(config.isContinuous);
	}

	public RangeOut<I> getSetpointSetter() {
		return setpointSetter;
	}

	public RangeIn<I> getSource() {
		return source;
	}
	public RangeOut<O> getOutput(){
		return output;
	}


	
}
