package com.team1389.hardware.outputs.hardware;

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
import com.team1389.util.AddList;
import com.team1389.util.state.State;
import com.team1389.util.state.StateTracker;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.NumberInfo;
import com.team1389.watch.info.StringInfo;

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

	public RangeIn<Speed> getSpeedInput() {
		return new RangeIn<Speed>(Speed.class, () -> {
			return getSpeed();
		}, 0, 1023);

	}

	public RangeIn<Position> getPositionInput() {
		return new RangeIn<Position>(Position.class, () -> {
			return getPosition();
		}, 0, 8912);
	}

	public PercentOut getVoltageOutput() {
		return new PercentOut(voltage -> {
			voltageState.init();
			wpiTalon.ifPresent(talon -> talon.set(voltage));
		});
	}

	public RangeOut<Speed> getSpeedOutput(PIDConstants config) {
		setupSpeedState(config);
		return new RangeOut<Speed>(speed -> {
			speedState.init();
			wpiTalon.ifPresent((CANTalon talon) -> {
				talon.set(speed);
			});
		}, 0, 8192);
	}

	public RangeOut<Position> getPositionOutput(PIDConstants config) {
		setupPositionState(config);
		return new RangeOut<Position>(position -> {
			positionState.init();
			wpiTalon.ifPresent((CANTalon talon) -> {
				talon.set(position);
			});
		}, 0, 8192);

	}

	public CANTalonFollower getFollower(CANTalonHardware toFollow) {
		setupFollowingState(toFollow);
		return new CANTalonFollower() {
			@Override
			public void follow() {
				followingState.init();
			}
		};
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		stem = super.getSubWatchables(stem);
		stem.put(new StringInfo("mode", getControlMode()::name));
		switch (getControlMode()) {
		case Current:
			break;
		case Disabled:
			break;
		case Follower:
			break;
		case MotionProfile:
			break;
		case PercentVbus:
			stem.put(new NumberInfo("voltage", this::getVoltage), getPositionInput().getWatchable("position"));
			break;
		case Position:
			stem.put(getPositionInput().getWatchable("position"), new NumberInfo("setpoint", this::getSetpoint));
			break;
		case Speed:
			stem.put(getSpeedInput().getWatchable("speed"), new NumberInfo("setpoint", this::getSetpoint));
			break;
		case Voltage:
			stem.put(new NumberInfo("voltage", this::getVoltage), getPositionInput().getWatchable("position"));
			break;
		default:
			break;

		}
		return stem;
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
		talon.setPosition(0);
		wpiTalon = Optional.of(talon);
	}

	private void setupPositionState(PIDConstants config) {
		positionState = stateTracker.newState(() -> {
			if (wpiTalon.isPresent()) {
				CANTalon talon = getWrappedTalon();
				talon.reverseOutput(outputInverted);
				talon.reverseSensor(inputInverted);
				talon.changeControlMode(TalonControlMode.Position);
				setPID(config);
			}
		});
	}

	private void setupSpeedState(PIDConstants config) {
		speedState = stateTracker.newState(() -> {
			if (wpiTalon.isPresent()) {
				CANTalon talon = getWrappedTalon();
				talon.reverseOutput(outputInverted);
				talon.reverseSensor(inputInverted);
				talon.changeControlMode(TalonControlMode.Speed);
				setPID(config);
			}
		});
	}

	private void setupFollowingState(CANTalonHardware toFollow) {
		followingState = stateTracker.newState(() -> {
			if (wpiTalon.isPresent() && toFollow.wpiTalon.isPresent()) {
				CANTalon talon = wpiTalon.get();
				talon.changeControlMode(TalonControlMode.Follower);
				talon.set(toFollow.getPort());
				talon.reverseOutput(toFollow.outputInverted ^ outputInverted);
			}
		});
	}

	private void setPID(PIDConstants pidConstants) {
		wpiTalon.ifPresent(talon -> {
			talon.setPID(pidConstants.p, pidConstants.i, pidConstants.d);
		});
	}

	public double getSetpoint() {
		return wpiTalon.map(talon -> talon.getSetpoint()).orElse(0.0);

	}

	public TalonControlMode getControlMode() {
		return wpiTalon.map(talon -> talon.getControlMode()).orElse(TalonControlMode.Disabled);
	}

	private double getPosition() {
		return wpiTalon.map(talon -> talon.getPosition()).orElse(0.0);
	}

	private double getSpeed() {
		return wpiTalon.map(talon -> talon.getSpeed()).orElse(0.0);
	}

	private double getVoltage() {
		return wpiTalon.map(talon -> talon.getOutputVoltage()).orElse(0.0);
	}

	public CANTalon getWrappedTalon() {
		return wpiTalon.get();
	}

	@Override
	public void failInit() {
		wpiTalon = Optional.empty();
	}

}
