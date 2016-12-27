package com.team1389.hardware.outputs.hardware;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.team1389.configuration.PIDConstants;
import com.team1389.hardware.Hardware;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.CAN;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.util.state.State;
import com.team1389.util.state.StateTracker;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class CANTalonHardware extends Hardware<CAN> {

	private final StateTracker stateTracker;
	private Optional<CANTalon> wpiTalon;
	private boolean outputInverted;
	private boolean inputInverted;
	private State voltageState, speedState, positionState, followingState;

	public CANTalonHardware(boolean outInverted, boolean inpInverted, CAN requestedPort, Registry registry) {
		super(requestedPort, registry);
		this.outputInverted = outInverted;
		this.inputInverted = inpInverted;
		stateTracker = new StateTracker();
	}

	public CANTalonHardware(boolean outInverted, CAN requestedPort, Registry registry) {
		this(outInverted, false, requestedPort, registry);
	}

	public PercentOut getVoltageOutput() {
		return new PercentOut((double voltage) -> {
			voltageState.init();
			wpiTalon.set(voltage);
		});
	}

	public CANTalon getWrappedTalon() {
		return wpiTalon;
	}

	public RangeOut<Speed> getSpeedOutput(PIDConstants config) {
		return new RangeOut<Speed>((double speed) -> {
			speedState.init();
			wpiTalon.set(speed);
		}, 0, 8192);
	}

	public RangeOut<Position> getPositionOutput(PIDConstants config) {

		return new RangeOut<Position>((double position) -> {
			positionState.init();
			wpiTalon.set(position);
		}, 0, 8192);

	}

	public RangeIn<Speed> getSpeedInput() {
		return new RangeIn<Speed>(Speed.class, () -> {
			wpiTalon.reverseSensor(inputInverted);
			return wpiTalon.getSpeed();
		}, 0, 1023);

	}

	public RangeIn<Position> getPositionInput() {
		return new RangeIn<Position>(Position.class, () -> {
			wpiTalon.reverseSensor(inputInverted);
			return wpiTalon.getPosition();
		}, 0, 8912);
	}

	public CANTalonFollower getFollower(CANTalonHardware toFollow) {
		State followStuff = followingState;
		followingState = () -> {
			followStuff.init();
			if(toFollow.wpiTalon.get().set(toFollow.getPort());
			wpiTalon.get().reverseOutput(toFollow.outputInverted ^ outputInverted);
		};
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
	public Watchable[] getSubWatchables() {
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
	protected String getHardwareIdentifier() {
		return "Talon";
	}

	public interface CANTalonFollower {
		public void follow();
	}

	@Override
	public void init(CAN port) {
		CANTalon talon = new CANTalon(port.index());
		voltageState = stateTracker.newState(() -> {
			talon.setInverted(outputInverted);
			talon.changeControlMode(TalonControlMode.PercentVbus);
		});
		speedState = stateTracker.newState(() -> {
			talon.reverseOutput(outputInverted);
			talon.reverseSensor(inputInverted);
			talon.changeControlMode(TalonControlMode.Speed);
			setPidConstants(config);
		});
		positionState = stateTracker.newState(() -> {
			talon.reverseOutput(outputInverted);
			talon.reverseSensor(inputInverted);
			talon.changeControlMode(TalonControlMode.Position);
			setPidConstants(config);
		});
		followingState = stateTracker.newState(() -> {
			talon.changeControlMode(TalonControlMode.Follower);
		});
		talon.setPosition(0);
		wpiTalon = Optional.of(talon);
	}

}
