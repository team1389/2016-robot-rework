package com.team1389.hardware.control;
import com.team1389.hardware.interfaces.inputs.OpenRangeInput;
import com.team1389.hardware.interfaces.outputs.OpenRangeOutput;
import com.team1389.hardware.interfaces.outputs.PercentRangeOutput;
import com.team1389.hardware.util.state.State;
import com.team1389.hardware.util.state.StateSetup;
import com.team1389.hardware.util.state.StateTracker;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;


/**
 * Can PID control a voltage controlled motor given a sensor.
 * Can do either speed control or position control.
 * 
 * Most importantly, this class will ensure that a given motor is only being used by one controller at once,
 * and that it has been given all required configuration before it runs.
 * @author Jacob Prinz
 */
public class PIDVoltageOutput {
	final StateTracker stateTracker;
	final PercentRangeOutput voltageOutput;
	
	public PIDVoltageOutput(PercentRangeOutput voltageOutput) {
		this.voltageOutput = voltageOutput;
		stateTracker = new StateTracker();
	}
	
	public OpenRangeOutput getSpeedOutput(OpenRangeInput speedSensor, PIDConfiguration config){
		PIDSource sensor = new PIDSpeedInput(speedSensor);
		PIDOutput motor = new PIDVoltageWrapper(voltageOutput);
		PIDController controller = makeController(config, sensor, motor);
		
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
		
		
		return new OpenRangeOutput() {
			
			@Override
			public void set(double speed) {
				controller.setSetpoint(speed);
				speedControlState.init();
			}
			public double min(){
				return speedSensor.min();
			}
			public double max(){
				return speedSensor.max();
			}
		};
	}
	
	public OpenRangeOutput getPositionOutput(OpenRangeInput positionSensor, PIDConfiguration config){
		PIDSource sensor = new PIDPositionInput(positionSensor);
		PIDOutput motor = new PIDVoltageWrapper(voltageOutput);
		PIDController controller = makeController(config, sensor, motor);
		
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
		
		return new OpenRangeOutput() {
			
			@Override
			public void set(double position) {
				controller.setSetpoint(position);
				positionControlState.init();
			}

			@Override
			public double min() {
				return positionSensor.min();
			}

			@Override
			public double max() {
				return positionSensor.max();
			}
		};
	}
	
	private static PIDController makeController(PIDConfiguration config, PIDSource source, PIDOutput output){
		PIDSource finalSource;
		if (config.isSensorReversed){
			finalSource = new InvertPIDSource(source);
		} else {
			finalSource = source;
		}
		PIDController controller = new PIDController(config.pidConstants.p, config.pidConstants.i, config.pidConstants.d,
				finalSource, output);
		controller.setContinuous(config.isContinuous);
		return controller;
	}
}
