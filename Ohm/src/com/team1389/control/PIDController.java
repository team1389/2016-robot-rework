package com.team1389.control;

import com.team1389.configuration.PIDConstants;
import com.team1389.configuration.PIDInput;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.PIDTunableValue;
import com.team1389.hardware.value_types.Value;
import com.team1389.watch.CompositeWatchable;
import com.team1389.watch.input.listener.NumberInput;

/**
 * This class applies PID control to a set of input/output streams<br>
 * Does all computation asynchronously, it updates without any prompting from the user <br>
 * 
 * @author amind
 *
 * @param <O> the value type of the output stream
 * @param <I> the value type of the input stream
 */
public class PIDController<O extends Value, I extends PIDTunableValue> extends edu.wpi.first.wpilibj.PIDController {
	private RangeOut<O> output;
	private RangeIn<I> source;
	private RangeOut<I> setpointSetter;

	/**
	 * @param kP the proportional gain of the PID controller
	 * @param kI the integral gain of the PID controller
	 * @param kD the derivative gain of the PID controller
	 * @param source the input stream
	 * @param output the output stream
	 */
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

	/**
	 * @param constants a set of gains for the PID controller
	 * @param source the input stream
	 * @param output the output stream
	 */
	public PIDController(PIDConstants constants, RangeIn<I> source, RangeOut<O> output) {
		this(constants.p, constants.i, constants.d, source, output);
	}

	/**
	 * @return an output stream that will pass applied values to the PID controller as setpoints
	 */
	public RangeOut<I> getSetpointSetter() {
		return setpointSetter;
	}

	/**
	 * @return the input stream
	 */
	public RangeIn<I> getSource() {
		return source;
	}

	/**
	 * @return the original output stream
	 */
	public RangeOut<O> getOutput() {
		return output;
	}

	/**
	 * @param name the name of the tuner watchable
	 * @return a watchable object that can accept input to adjust the gains of the PID controller
	 */
	public CompositeWatchable getPIDTuner(String name) {
		return CompositeWatchable.of(name,
				new PIDInput(name, getPID(), this::setPID).getSubWatchables(CompositeWatchable.stem)
						.put(new NumberInput("setpoint", getSetpoint(), this::setSetpoint)));
	}

	private void setPID(PIDConstants constants) {
		super.setPID(constants.p, constants.i, constants.d);
	}

	private PIDConstants getPID() {
		return new PIDConstants(getP(), getI(), getD());
	}

}
