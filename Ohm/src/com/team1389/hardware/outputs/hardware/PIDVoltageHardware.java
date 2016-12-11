package com.team1389.hardware.outputs.hardware;

import com.team1389.configuration.PIDConstants;
import com.team1389.control.PIDController;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.util.state.State;
import com.team1389.util.state.StateSetup;
import com.team1389.util.state.StateTracker;

/**
 * Can PID control a voltage controlled motor given a sensor. Can do either speed control or position control.
 * 
 * Most importantly, this class will ensure that a given motor is only being used by one controller at once, and that it has been given all required configuration before it runs.
 * 
 * @author Jacob Prinz
 */
public class PIDVoltageHardware {
	final StateTracker stateTracker;
	final PercentOut voltageOutput;

	public PIDVoltageHardware(PercentOut voltageOutput) {
		this.voltageOutput = voltageOutput;
		stateTracker = new StateTracker();
	}

	public RangeOut<Speed> getSpeedOutput(RangeIn<Speed> speedSensor, PIDConstants config) {
		PIDController<Percent, Speed> controller = new PIDController<Percent, Speed>(config, speedSensor,
				voltageOutput);

		State speedControlState = stateTracker.newState(new StateSetup() {
			@Override
			public void setup() {
				controller.enable();
			}

			@Override
			public void end() {
				controller.disable();
			}
		});

		return controller.getSetpointSetter().addChangeListener(() -> {
			speedControlState.init();
		});
	}

	public RangeOut<Position> getPositionOutput(RangeIn<Position> positionSensor, PIDConstants config) {
		PIDController<Percent, Position> controller = new PIDController<Percent, Position>(config, positionSensor,
				voltageOutput);

		State positionControlState = stateTracker.newState(new StateSetup() {
			@Override
			public void setup() {
				controller.enable();
			}

			@Override
			public void end() {
				controller.disable();
			}
		});

		return controller.getSetpointSetter().addChangeListener(() -> {
			positionControlState.init();
		});

	}
}
