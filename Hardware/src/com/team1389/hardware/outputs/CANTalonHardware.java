package com.team1389.hardware.outputs;

import java.util.HashMap;
import java.util.Map;

import com.team1389.hardware.configuration.PIDConstants;
import com.team1389.hardware.control.PIDConfiguration;
import com.team1389.hardware.interfaces.inputs.OpenRangeInput;
import com.team1389.hardware.interfaces.outputs.CANTalonFollower;
import com.team1389.hardware.interfaces.outputs.OpenRangeOutput;
import com.team1389.hardware.interfaces.outputs.PercentRangeOutput;
import com.team1389.hardware.registry.CANPort;
import com.team1389.hardware.registry.Constructor;
import com.team1389.hardware.util.state.State;
import com.team1389.hardware.util.state.StateTracker;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.Watchable;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class CANTalonHardware implements Watchable {
	public static final Constructor<CANPort, CANTalonHardware> constructor = (CANPort port) -> {
		return new CANTalonHardware(port);
	};

	private final StateTracker stateTracker;
	private final CANTalon wpiTalon;
	private String currentMode;
	
	private CANTalonHardware(CANPort port) {
		stateTracker = new StateTracker();
		wpiTalon = new CANTalon(port.number);
		wpiTalon.setPosition(0);
		currentMode = "None";
	}

	public PercentRangeOutput getVoltageOutput() {
		State voltageState = stateTracker.newState(() -> {
			wpiTalon.changeControlMode(TalonControlMode.PercentVbus);
			currentMode = "Voltage Control";
		});

		return (double voltage) -> {
			voltageState.init();
			wpiTalon.set(voltage);
		};
	}
	public CANTalon getWrappedTalon(){
		return wpiTalon;
	}
	public OpenRangeOutput getSpeedOutput(PIDConfiguration config) {
		State speedState = stateTracker.newState(() -> {
			wpiTalon.changeControlMode(TalonControlMode.Speed);
			setPidConstants(wpiTalon, config.pidConstants);
			wpiTalon.reverseSensor(config.isSensorReversed);
			currentMode = "Speed";
		});

		return new OpenRangeOutput() {

			@Override
			public void set(double val) {
				speedState.init();
				wpiTalon.set(val);				
			}

			@Override
			public double min() {
				//TODO
				return 0;
			}

			@Override
			public double max() {
				//TODO
				return 0;
			}

		};
	}
	public void setInverted(boolean inverted){
		wpiTalon.setInverted(inverted);
	}
	public OpenRangeOutput getPositionOutput(PIDConfiguration config) {
		State positionState = stateTracker.newState(() -> {
			wpiTalon.changeControlMode(TalonControlMode.Position);
			setPidConstants(wpiTalon, config.pidConstants);
			wpiTalon.reverseSensor(config.isSensorReversed);
			currentMode = "Position";
		});
		return new OpenRangeOutput() {

			@Override
			public void set(double val) {
				positionState.init();
				wpiTalon.set(val);				
			}

			@Override
			public double min() {
				return 0;
			}

			@Override
			public double max() {
				return 8192;
			}
		
		};
	}

	public OpenRangeInput getSpeedInput() {
		wpiTalon.configEncoderCodesPerRev(1023);
		return
				
				new OpenRangeInput() {

			@Override
			public double get() {
				return wpiTalon.getSpeed();
			}

			@Override
			public double min() {
				return 0;
			}

			@Override
			public double max() {
				return 1023;
			}
		};
	}

	public OpenRangeInput getPositionInput() {
		//wpiTalon.configEncoderCodesPerRev(4096);
		return new OpenRangeInput() {

			@Override
			public double get() {
				return wpiTalon.getPosition();
			}

			@Override
			public double min() {
				return 0;
			}

			@Override
			public double max() {
				return 4096;
			}
		};
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
		switch(wpiTalon.getControlMode()){
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
		//info.put("speed", "" + wpiTalon.getSpeed());
		//info.put("voltage out", "" + wpiTalon.getOutputVoltage());

		return null;
	}
}
