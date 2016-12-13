package org.usfirst.frc.team1389.systems;

import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConstants;
import com.team1389.control.SynchronousPIDController;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.ButtonEnumMap;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Angle;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.System;
import com.team1389.watch.Watchable;

public class ArmSystem extends System {

	public RangeOut<Angle> elevator;
	SynchronousPIDController<Percent, Angle> elevatorPID;
	ButtonEnumMap<ArmLocation> buttons;
	AngleIn armVal;
	Command profileMover;
	double inputAngle;

	public ArmSystem(RangeOut<Percent> elevator, ButtonEnumMap<ArmLocation> map, AngleIn armVal) {
		this.buttons = map;
		this.armVal = armVal;
		elevatorPID = new SynchronousPIDController<Percent, Angle>(new PIDConstants(.003, 0, 0), armVal, elevator);
		this.elevator = elevatorPID.getSetpointSetter().getProfiledOut(20, 0);
		this.inputAngle = 0;
	}

	@Override
	public void init() {
		elevator.set(inputAngle);
		buttons.addChangeListener(defaultModeListener);
	}

	@Override
	public void getInput() {
		inputAngle = buttons.getVal().angle;
	}

	@Override
	public void defaultUpdate() {
		elevator.set(inputAngle);
		elevatorPID.update();
	}

	public enum ArmLocation {
		DOWN(0), DEFENSE(45), LOW_GOAL(25), VERTICAL(90);

		ArmLocation(double angle) {
			this.angle = angle;
		}

		public ArmLocation next() {
			if (ordinal() < values().length - 1) {
				return values()[ordinal() + 1];
			}
			return this;
		}

		public ArmLocation previous() {
			if (ordinal() > 0) {
				return values()[ordinal() - 1];
			}
			return this;
		}

		public final double angle;
	}

	@Override
	public String getName() {
		return "Arm System";
	}

	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { buttons.getWatchable("target location"), armVal.getWatchable("arm position"),
				elevatorPID.getSetpointSetter().getWatchable("arm setpoint"),
				elevatorPID.getOutput().getWatchable("arm vOut") };
	}
}
