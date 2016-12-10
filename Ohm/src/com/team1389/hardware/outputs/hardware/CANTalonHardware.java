package com.team1389.hardware.outputs.hardware;

import java.util.HashMap;
import java.util.Map;

import com.team1389.configuration.PIDConfiguration;
import com.team1389.configuration.PIDConstants;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.interfaces.CANTalonFollower;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.util.state.State;
import com.team1389.util.state.StateTracker;
import com.team1389.watch.Info;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class CANTalonHardware implements Watchable {

	private final StateTracker stateTracker;
	private final CANTalon wpiTalon;
	private String currentMode;

	public CANTalonHardware(int canPort, Registry registry) {
		registry.claimCANPort(canPort);
		registry.registerWatcher(this);
		wpiTalon = new CANTalon(canPort);
		stateTracker = new StateTracker();
		currentMode = "None";
	}

	public PercentOut getVoltageOutput() {
		State voltageState = stateTracker.newState(() -> {
			wpiTalon.changeControlMode(TalonControlMode.PercentVbus);
			currentMode = "Voltage Control";
		});

		return new PercentOut((double voltage) -> {
			voltageState.init();
			wpiTalon.set(voltage);
		});
	}

	public CANTalon getWrappedTalon() {
		return wpiTalon;
	}

	public RangeOut<Speed> getSpeedOutput(PIDConfiguration config) {
		State speedState = stateTracker.newState(() -> {
			wpiTalon.changeControlMode(TalonControlMode.Speed);
			setPidConstants(wpiTalon, config.pidConstants);
			wpiTalon.reverseSensor(config.isSensorReversed);
			currentMode = "Speed";
		});

		return new RangeOut<Speed>((double speed) -> {
			speedState.init();
			wpiTalon.set(speed);
		}, // TODO
				0, 0);
	}

	public void setInverted(boolean inverted) {
		wpiTalon.setInverted(inverted);
	}

	public RangeOut<Position> getPositionOutput(PIDConfiguration config) {
		State positionState = stateTracker.newState(() -> {
			wpiTalon.changeControlMode(TalonControlMode.Position);
			setPidConstants(wpiTalon, config.pidConstants);
			wpiTalon.reverseSensor(config.isSensorReversed);
			currentMode = "Position";
		});
		return new RangeOut<Position>((double position) -> {
			positionState.init();
			wpiTalon.set(position);
		}, 0, 8192);

	}

	public RangeIn<Speed> getSpeedInput() {
		return new RangeIn<Speed>(Speed.class, () -> {
			return wpiTalon.getSpeed();
		}, 0, 1023);

	}

	public RangeIn<Position> getPositionInput() {
		return new RangeIn<Position>(Position.class, () -> {
			return wpiTalon.getPosition();
		}, 0, 8912);
	}

	public CANTalonFollower getFollower(CANTalonHardware toFollow) {
		State followingState = stateTracker.newState(() -> {
			wpiTalon.changeControlMode(TalonControlMode.Follower);
			wpiTalon.set(toFollow.wpiTalon.getDeviceID());
			currentMode = "Follower";
		});

		return new CANTalonFollower() {
			@Override
			public void follow() {
				followingState.init();
			}
		};
	}

	private void setPidConstants(CANTalon wpiTalon, PIDConstants pidConstants) {
		wpiTalon.setPID(pidConstants.p, pidConstants.i, pidConstants.d);
	}

	@Override
	public String getName() {
		return "CAN Talon " + wpiTalon.getDeviceID();
	}

	@Override
	public Info[] getInfo() {
		Map<String, String> info = new HashMap<>();
		info.put("mode", currentMode);
		switch (wpiTalon.getControlMode()) {
		case Current:
			break;
		case Disabled:
			break;
		case Follower:
			break;
		case MotionProfile:
			break;
		case PercentVbus:
			break;
		case Position:
			info.put("position", "" + wpiTalon.getPosition());
			info.put("setPoint", "" + wpiTalon.getSetpoint());
			break;
		case Speed:
			break;
		case Voltage:
			break;
		default:
			break;

		}
		// info.put("speed", "" + wpiTalon.getSpeed());
		// info.put("voltage out", "" + wpiTalon.getOutputVoltage());

		return null;
	}
}
