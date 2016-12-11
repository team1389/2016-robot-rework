package com.team1389.hardware.outputs.hardware;

import java.util.HashMap;
import java.util.Map;

import com.team1389.configuration.PIDConstants;
import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.interfaces.CANTalonFollower;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.registry.port_types.CAN;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.util.state.State;
import com.team1389.util.state.StateTracker;
import com.team1389.watch.Info;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class CANTalonHardware extends Hardware<CAN> {

	private final StateTracker stateTracker;
	private CANTalon wpiTalon;
	private boolean outputInverted;
	private boolean inputInverted;

	public CANTalonHardware(boolean outInverted, boolean inpInverted) {
		this.outputInverted = outInverted;
		this.inputInverted = inpInverted;
		stateTracker = new StateTracker();
	}

	public CANTalonHardware(boolean outInverted) {
		this(outInverted, false);
	}

	public PercentOut getVoltageOutput() {
		State voltageState = stateTracker.newState(() -> {
			wpiTalon.changeControlMode(TalonControlMode.PercentVbus);
		});

		return new PercentOut((double voltage) -> {
			voltageState.init();
			wpiTalon.set(voltage);
		});
	}

	public CANTalon getWrappedTalon() {
		return wpiTalon;
	}

	public RangeOut<Speed> getSpeedOutput(PIDConstants config) {
		State speedState = stateTracker.newState(() -> {
			wpiTalon.changeControlMode(TalonControlMode.Speed);
			setPidConstants(config);
		});

		return new RangeOut<Speed>((double speed) -> {
			speedState.init();
			wpiTalon.set(speed);
		}, 0, 8192);
	}

	public RangeOut<Position> getPositionOutput(PIDConstants config) {
		State positionState = stateTracker.newState(() -> {
			wpiTalon.changeControlMode(TalonControlMode.Position);
			setPidConstants(config);
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
		});

		return new CANTalonFollower() {
			@Override
			public void follow() {
				followingState.init();
			}
		};
	}

	private void setPidConstants(PIDConstants pidConstants) {
		wpiTalon.setPID(pidConstants.p, pidConstants.i, pidConstants.d);
	}

	@Override
	public Info[] getInfo() {
		Map<String, String> info = new HashMap<>();
		info.put("mode", wpiTalon.getControlMode().name());
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
		return null;
	}

	@Override
	public void initHardware(int port) {
		wpiTalon = new CANTalon(port);
		wpiTalon.setPosition(0);
		wpiTalon.reverseOutput(outputInverted);
		wpiTalon.reverseSensor(inputInverted);
	}

	@Override
	protected String getHardwareIdentifier() {
		return "Talon";
	}
}
