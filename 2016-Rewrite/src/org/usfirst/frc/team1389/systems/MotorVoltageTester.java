package org.usfirst.frc.team1389.systems;

import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.system.System;
import com.team1389.watch.Watchable;

public class MotorVoltageTester extends System {
	PercentOut out;
	PercentIn in;

	public MotorVoltageTester(PercentOut out, PercentIn in, boolean inverted) {
		this.out = out;
		this.in = inverted ? in.invert() : in;
	}

	@Override
	public Watchable[] getSubWatchables() {
		return new Watchable[] { in.getWatchable("joyVal"), out.getWatchable("motor Val") };
	}

	@Override
	public String getName() {
		return "inversion test";
	}

	@Override
	public void init() {

	}

	@Override
	public void defaultUpdate() {
		out.set(in.get());
	}

	@Override
	public void getInput() {
		// TODO Auto-generated method stub

	}

}
