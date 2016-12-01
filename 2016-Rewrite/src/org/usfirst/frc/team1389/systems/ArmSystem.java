package org.usfirst.frc.team1389.systems;

import com.team1389.commands.CommandUtil;
import com.team1389.hardware.inputs.software.ButtonEnumMap;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Angle;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.System;
import com.team1389.watch.Info;
import com.team1389.watch.NumberInfo;
import com.team1389.watch.StringInfo;

public class ArmSystem extends System {

	RangeOut<Angle> elevator;
	ButtonEnumMap<ArmLocation> buttons;
	RangeIn<Position> armVal;

	double inputAngle;

	public ArmSystem(RangeOut<Position> elevator, ButtonEnumMap<ArmLocation> map, RangeIn<Position> armVal) {
		this.elevator = elevator.invert().getProfiledOut(72,0).mapToAngle();
		this.buttons = map;
		this.armVal = armVal;
		this.inputAngle = 0;
	}

	public void init() {
		elevator.set(inputAngle);
		buttons.addChangeListener(defaultModeListener);
	}

	public void getInput() {
		inputAngle = buttons.getVal().angle;
	}

	@Override
	public void defaultUpdate() {
		elevator.set(inputAngle);
	}

	public void setArm(double angle) {
		schedule(CommandUtil.createCommand(() -> {
			elevator.set(angle);
			return armVal.get() == angle;
		}));
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
		}), new NumberInfo("Current Position", () -> {
			return armVal.get();
		}) };
	}
}
