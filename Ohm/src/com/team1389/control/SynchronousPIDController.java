package com.team1389.control;

import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConstants;
import com.team1389.configuration.PIDInput;
import com.team1389.hardware.inputs.interfaces.BinaryInput;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.PIDTunableValue;
import com.team1389.hardware.value_types.Value;
import com.team1389.watch.Watchable;

public class SynchronousPIDController<O extends Value, I extends PIDTunableValue> extends SynchronousPID {
	protected RangeOut<O> output;
	protected RangeIn<I> source;

	public SynchronousPIDController(double kP, double kI, double kD, RangeIn<I> source, RangeOut<O> output) {
		super(kP, kI, kD);
		this.source = source;
		this.output = output;
		setInputRange(source.min(), source.max());
		setOutputRange(output.min(), output.max());
	}

	public SynchronousPIDController(PIDConstants constants, RangeIn<I> source, RangeOut<O> output) {
		this(constants.p, constants.i, constants.d, source, output);
	}

	public void update() {
		output.set(calculate(source.get()));
	}

	public RangeOut<I> getSetpointSetter() {
		return new RangeOut<I>(this::setSetpoint, source.min(), source.max());
	}

	public RangeIn<I> getSource() {
		return source;
	}

	public RangeOut<O> getOutput() {
		return output;
	}

	public Command getPIDDoCommand() {
		return getPIDDoCommand(() -> {
			return false;
		});
	}

	public Command getPIDDoCommand(double tolerance) {
		return getPIDDoCommand(() -> {
			return onTargetStable(tolerance);
		});
	}

	public Command getPIDDoCommand(BinaryInput exitCondition) {
		return CommandUtil.createCommand(() -> {
			update();
			return exitCondition.get();
		});
	}

	public void setPID(PIDConstants constants) {
		super.setPID(constants.p, constants.i, constants.d);
	}

	public PIDConstants getPID() {
		return new PIDConstants(getP(), getI(), getD());
	}

	public Watchable getPIDTuner(String name) {
		return new PIDInput(name, getPID(), this::setPID);
	}

}
