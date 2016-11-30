package com.team1389.control;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class PIDSystemCreator {
	/**
	 * creates a PIDController that constantly moves output toward the giving
	 * setpoint, using source as feedback PIDController is best used for outputs
	 * that are constantly being set by PID DO NOT USE PIDController for any
	 * terminating PID operation! use {@link SynchronousPIDController} instead.
	 * 
	 * @param config
	 *            the setup data used to customize the PIDController
	 * @param source
	 *            the source of input, used for feedback
	 * @param output
	 *            the output to control using PID
	 * @return a PIDController that can be enabled to move output towards its
	 *         setpoint
	 */
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

	/**
	 * creates a SynchronousPIDController that moves the output toward the
	 * current setpoint when its update() method is called this is different
	 * from {@link PIDController}'s implementation because it does the pid
	 * calculation in the current thread, resolving issues with asynchronous
	 * calculations. Good for PID tasks that require the operating class to
	 * watch error.
	 * 
	 * @see SynchronousPID
	 * @see PIDController
	 * 
	 * @param config
	 *            the setup data used to customize the PIDController
	 * @param source
	 *            the source of input, used for feedback
	 * @param output
	 *            the output to control using PID
	 * @return a PIDController that can be enabled to move output towards its
	 *         setpoint
	 */
	public static SynchronousPIDController makeSynchronousController(PIDConfiguration config, PIDSource source,
			PIDOutput output) {
		PIDSource finalSource;
		if (config.isSensorReversed) {
			finalSource = new InvertPIDSource(source);
		} else {
			finalSource = source;
		}
		SynchronousPIDController controller = new SynchronousPIDController(config.pidConstants.p, config.pidConstants.i,
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
