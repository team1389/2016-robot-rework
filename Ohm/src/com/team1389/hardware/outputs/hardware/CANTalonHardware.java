package com.team1389.hardware.outputs.hardware;

import java.util.Optional;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;
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

/**
 * This class offers input/output stream sources for a Talon SRX.
 * <p>
 * Furthermore, this class will ensure that the Talon has been given all required configuration before it enters any control mode. <br>
 * TODO add limit switch support
 * 
 * @author amind
 *
 */
public class CANTalonHardware extends Hardware<CAN> {

	private final StateTracker stateTracker;
	private Optional<CANTalon> wpiTalon;
	private boolean outputInverted;
	private boolean inputInverted;
	private State voltageState, speedState, positionState, followingState;

	/**
	 * @param outInverted whether the motor output should be inverted (used for both voltage and position control modes)
	 * @param inpInverted whether the sensor input should be inverted
	 * @param requestedPort the port to attempt to initialize this hardware
	 * @param registry the registry associated with the robot
	 * @see <a href="https://www.ctr-electronics.com/Talon%20SRX%20Software%20Reference%20Manual.pdf">Talon SRX user manual</a> for more information on output/input inversion
	 */
	@SuppressWarnings("javadoc")
	public CANTalonHardware(boolean outInverted, boolean inpInverted, CAN requestedPort, Registry registry) {
		super(requestedPort, registry);
		this.outputInverted = outInverted;
		this.inputInverted = inpInverted;
		stateTracker = new StateTracker();
	}

	/**
	 * assumes input is not inverted
	 * 
	 * @param outInverted whether the motor output should be inverted (used for both voltage and position control modes)
	 * @param requestedPort the port to attempt to initialize this hardware
	 * @param registry the registry associated with the robot
	 * @see <a href="https://www.ctr-electronics.com/Talon%20SRX%20Software%20Reference%20Manual.pdf">Talon SRX user manual</a> for more information on output/input inversion
	 */
	@SuppressWarnings("javadoc")
	public CANTalonHardware(boolean outInverted, CAN requestedPort, Registry registry) {
		this(outInverted, false, requestedPort, registry);
	}

	/**
	 * @return a speed input stream indicating the current speed of the motor based on the talon's preferred speed sensor <br>
	 *         uses a default range of [0,1023] specified by the talon manual, but may vary depending on the speed sensor
	 */
	public RangeIn<Speed> getSpeedInput() {
		return new RangeIn<Speed>(Speed.class, () -> {
			return getSpeed();
		}, 0, 1023);

	}

	/**
	 * @return a position input stream indicating the current speed of the motor based on the talon's preferred position sensor <br>
	 *         uses a default range of [0,8192] specified by the Vex versaplanetary encoder, but may vary depending on the sensor
	 */
	public RangeIn<Position> getPositionInput() {
		return new RangeIn<Position>(Position.class, () -> {
			return getPosition();
		}, 0, 8912);
	}

	/**
	 * @return a percent output stream that sets the voltage of the talon in PercentVBus mode
	 */
	public PercentOut getVoltageOutput() {
		return new PercentOut(voltage -> {
			voltageState.init();
			wpiTalon.ifPresent(talon -> talon.set(voltage));
		});
	}

	/**
	 * @param config the speed PIDConstants
	 * @return a speed output stream
	 */
	public RangeOut<Speed> getSpeedOutput(PIDConstants config) {
		return getSpeedOutput(config, 0.0);
	}

	/**
	 * @param config the P,I, and D gains
	 * @param feedForward the F gain
	 * @return a speed output stream
	 */
	public RangeOut<Speed> getSpeedOutput(PIDConstants config, double feedForward) {
		setupSpeedState(config, feedForward);
		return new RangeOut<Speed>(speed -> {
			speedState.init();
			wpiTalon.ifPresent(talon -> talon.set(speed));
		}, 0, 8192);
	}

	/**
	 * 
	 * @param config the P, I, and D gains
	 * @return a position output stream that controls the position of the talon
	 */
	public RangeOut<Position> getPositionOutput(PIDConstants config) {
		setupPositionState(config);
		return new RangeOut<Position>(position -> {
			positionState.init();
			wpiTalon.ifPresent(talon -> talon.set(position));
		}, 0, 8192);

	}

	/**
	 * @param toFollow the master Talon to follow
	 * @return a follower object
	 */
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

	/**
	 * represents a slave talon in a {@link CANTalonGroup}
	 * 
	 * @author amind
	 *
	 */
	public interface CANTalonFollower {
		/**
		 * 
		 */
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

	private void setupSpeedState(PIDConstants config, double feedForward) {
		speedState = stateTracker.newState(() -> {
			if (wpiTalon.isPresent()) {
				CANTalon talon = getWrappedTalon();
				talon.reverseOutput(outputInverted);
				talon.reverseSensor(inputInverted);
				talon.changeControlMode(TalonControlMode.Speed);
				setPID(config);
				talon.setF(feedForward);
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

	/**
	 * 
	 * @return the setpoint of the talon
	 */
	public double getSetpoint() {
		return wpiTalon.map(talon -> talon.getSetpoint()).orElse(0.0);

	}

	/**
	 * 
	 * @return the current control mode of the talon
	 */
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

	/**
	 * @return the WPILib talon object
	 * @throws NullPointerException if the talon failed to initialize due to port claiming failure
	 */
	public CANTalon getWrappedTalon() {
		return wpiTalon.get();
	}

	@Override
	public void failInit() {
		wpiTalon = Optional.empty();
	}

}
