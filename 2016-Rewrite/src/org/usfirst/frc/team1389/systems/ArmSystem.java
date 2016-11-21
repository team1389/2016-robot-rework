package org.usfirst.frc.team1389.systems;

import java.util.HashMap;
import java.util.Map;

import com.team1389.hardware.interfaces.inputs.LatchedDigitalInput;
import com.team1389.hardware.interfaces.outputs.OpenRangeOutput;
import com.team1389.hardware.watch.Watchable;
import com.team1389.system.System;

public class ArmSystem implements System, Watchable{

	OpenRangeOutput elevator;
	ArmLocation armLocationTracker;
	LatchedDigitalInput incrementer;
	LatchedDigitalInput decrementer;

	public ArmSystem(OpenRangeOutput elevator, LatchedDigitalInput increment, LatchedDigitalInput decrement) {
		this.elevator = OpenRangeOutput.mapToOpenRange(elevator, 0d, 360d);
		this.incrementer=increment;
		this.decrementer=decrement;
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
		DOWN(0,"Down"), DEFENSE(45,"Defense"), LOW_GOAL(25,"Low Goal"), HIGH_GOAL(35,"High Goal");

		ArmLocation(double angle,String name) {
			this.angle = angle;
			this.name=name;
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
		public final String name;
		public final double angle;
	}

	@Override
	public String getName() {
		return "Arm System";
	}

	@Override
	public Map<String, String> getInfo() {
		Map<String, String> info = new HashMap<>();
		info.put("position", "" + armLocationTracker.name);
		return info;

	}
}
