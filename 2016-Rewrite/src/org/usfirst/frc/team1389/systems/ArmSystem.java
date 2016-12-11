package org.usfirst.frc.team1389.systems;

import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConstants;
import com.team1389.control.SynchronousPIDController;
import com.team1389.hardware.inputs.software.ButtonEnumMap;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.System;
import com.team1389.watch.Info;
import com.team1389.watch.NumberInfo;
import com.team1389.watch.StringInfo;

public class ArmSystem extends System {

	public RangeOut<Position> elevator;
	SynchronousPIDController<Percent, Position> elevatorPID;
	ButtonEnumMap<ArmLocation> buttons;
	RangeIn<Position> armVal;
	Command profileMover;
	double inputAngle;

	public ArmSystem(RangeOut<Percent> elevator, ButtonEnumMap<ArmLocation> map, RangeIn<Position> armVal) {
		this.buttons = map;
		this.armVal = armVal;
		elevatorPID = new SynchronousPIDController<Percent, Position>(new PIDConstants(.06, 0, 0), armVal, elevator);
		this.elevator = elevatorPID.getSetpointSetter().invert();
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
	public Info[] getInfo() {
		return new Info[] { new StringInfo("Target Location", () -> {
			return buttons.getVal().name();
		}), new NumberInfo("Arm Position", () -> {
			return armVal.get();
		}) };
	}
}
