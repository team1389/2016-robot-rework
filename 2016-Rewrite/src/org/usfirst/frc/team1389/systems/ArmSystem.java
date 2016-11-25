package org.usfirst.frc.team1389.systems;

import org.usfirst.frc.team1389.util.ButtonEnumMap;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.NumberInfo;
import com.team1389.hardware.watch.StringInfo;
import com.team1389.hardware.watch.Watchable;
import com.team1389.system.System;

public class ArmSystem implements System, Watchable {

	RangeOut elevator;
	ButtonEnumMap<ArmLocation> buttons;
	RangeIn armVal;

	public ArmSystem(RangeOut elevator, ButtonEnumMap<ArmLocation> map,RangeIn armVal) {
		this.elevator = elevator.invert().getProfiledOut(1638).mapToRange(0d, 360d);
		this.buttons=map;
		this.armVal=armVal;
	}

	public void init() {
		elevator.set(0);
	}

	@Override
	public void update() {
		elevator.set(buttons.getVal().angle);
	}

	public enum ArmLocation {
		DOWN(0), DEFENSE(45), LOW_GOAL(25), HIGH_GOAL(35);

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
		return new Info[]{
					new StringInfo("Target Location",()->{return buttons.getVal().name();}),
					new NumberInfo("Current Position",()->{return armVal.get();})
				};
	}
}
