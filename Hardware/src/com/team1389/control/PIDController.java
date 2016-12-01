package com.team1389.control;

import com.team1389.configuration.PIDConstants;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.PIDTunableValue;
import com.team1389.hardware.value_types.Value;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class PIDController<O extends Value, I extends PIDTunableValue> extends edu.wpi.first.wpilibj.PIDController {
	private RangeOut<O> output;
	private RangeIn<I> source;
	private RangeOut<I> setpointSetter;

	public PIDController(double kP, double kI, double kD, RangeIn<I> source, RangeOut<O> output) {
		super(kP, kI, kD, null, null);
		m_pidInput = new PIDRangeIn<I>(source);
		m_pidOutput = new PIDRangeOut<O>(output);
		this.source = source;
		this.output = output;
		setOutputRange(output.min(), output.max());
		setInputRange(source.min(), source.max());
		this.setpointSetter = new RangeOut<I>((double setpoint) -> {
			setSetpoint(setpoint);
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

	private class PIDRangeOut<T extends Value> implements edu.wpi.first.wpilibj.PIDOutput {

		RangeOut<T> outputRange;

		public PIDRangeOut(RangeOut<T> voltageOutput) {
			this.outputRange = voltageOutput;
		}

		@Override
		public void pidWrite(double output) {
			outputRange.set(output);
		}

	}

	private class PIDRangeIn<T extends PIDTunableValue> implements PIDSource {
		RangeIn<T> input;

		public PIDRangeIn(RangeIn<T> input) {
			this.input = input;
		}

		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {

		}

		@Override
		public PIDSourceType getPIDSourceType() {
			try {
				return input.type.newInstance().getValueType();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return PIDSourceType.kDisplacement;
		}

		@Override
		public double pidGet() {
			return input.get();
		}

	}
}
