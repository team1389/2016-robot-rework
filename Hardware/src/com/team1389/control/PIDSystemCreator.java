package com.team1389.control;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class PIDSystemCreator {
	public static PIDController makeController(PIDConfiguration config, PIDSource source, PIDOutput output) {
		PIDSource finalSource;
		if (config.isSensorReversed) {
			finalSource = new InvertPIDSource(source);
		} else {
			finalSource = source;
		}
		PIDController controller = new PIDController(config.pidConstants.p, config.pidConstants.i,
				config.pidConstants.d, finalSource, output);
		controller.setContinuous(config.isContinuous);
		return controller;
	}
	public static PIDOutput combine(PIDOutput... outputs) {
		return (double val) -> {
			for (PIDOutput output : outputs) {
				output.pidWrite(val);
			}
		};
	}

}
