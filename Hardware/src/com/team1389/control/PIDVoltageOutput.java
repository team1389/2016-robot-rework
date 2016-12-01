package com.team1389.control;

import com.team1389.control.pid_wrappers.output.PIDControlledRange;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.util.state.State;
import com.team1389.util.state.StateSetup;
import com.team1389.util.state.StateTracker;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

/**
 * Can PID control a voltage controlled motor given a sensor. Can do either
 * speed control or position control.
 * 
 * Most importantly, this class will ensure that a given motor is only being
 * used by one controller at once, and that it has been given all required
 * configuration before it runs.
 * 
 * @author Jacob Prinz
 */
public class PIDVoltageOutput {
	final StateTracker stateTracker;
	final PercentOut voltageOutput;

	public PIDVoltageOutput(PercentOut voltageOutput) {
		this.voltageOutput = voltageOutput;
		stateTracker = new StateTracker();
	}

	public RangeOut<Speed> getSpeedOutput(RangeIn<Speed> speedSensor, PIDConfiguration config) {
		PIDSource sensor = new PIDSpeedInput(speedSensor);
		PIDOutput motor = new PIDControlledRange<Percent>(voltageOutput);
		PIDController controller = PIDSystemCreator.makeController(config, sensor, motor);

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

		return new RangeOut<Speed>((double speed) -> {
			controller.setSetpoint(speed);
			speedControlState.init();
		} , speedSensor.min(), speedSensor.max());
	}

	public RangeOut<Position> getPositionOutput(RangeIn<Position> positionSensor, PIDConfiguration config) {
		PIDSource sensor = new PIDPositionInput(positionSensor);
		PIDOutput motor = new PIDControlledRange<Percent>(voltageOutput);
		PIDController controller = PIDSystemCreator.makeController(config, sensor, motor);

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

		return new RangeOut<Position>((double position) -> {
			controller.setSetpoint(position);
			positionControlState.init();
		} , positionSensor.min(), positionSensor.max());
	}
}
