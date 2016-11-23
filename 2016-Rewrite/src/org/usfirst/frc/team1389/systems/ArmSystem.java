package org.usfirst.frc.team1389.systems;

import com.team1389.hardware.inputs.LatchedDigitalInput;
import com.team1389.hardware.interfaces.outputs.OpenRangeOutput;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.StringInfo;
import com.team1389.hardware.watch.Watchable;
import com.team1389.system.System;

public class ArmSystem implements System, Watchable {

	OpenRangeOutput elevator;
	ArmLocation armLocationTracker;
	LatchedDigitalInput incrementer;
	LatchedDigitalInput decrementer;

	public ArmSystem(OpenRangeOutput elevator, LatchedDigitalInput increment, LatchedDigitalInput decrement) {
		this.elevator = OpenRangeOutput.mapToOpenRange(elevator, 0d, 360d);
		this.incrementer = increment;
		this.decrementer = decrement;
	}

	public void init() {
		elevator.set(0);
		armLocationTracker = ArmLocation.DOWN;
	}

	@Override
	public void update() {
		if (incrementer.get()) {
			armLocationTracker = armLocationTracker.next();
		} else if (decrementer.get()) {
			armLocationTracker = armLocationTracker.previous();
		}
		elevator.set(armLocationTracker.angle);
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
		return new Info[]{new StringInfo("Position",()->{return armLocationTracker.name();})};
	}
}
