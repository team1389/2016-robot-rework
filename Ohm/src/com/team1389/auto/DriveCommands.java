package com.team1389.auto;

import com.team1389.auto.command.FollowProfileCommand;
import com.team1389.auto.command.TurnAngleCommand;
import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConfiguration;
import com.team1389.control.SynchronousPIDController;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Angle;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.motion_profile.TrapezoidalMotionProfile;

/**
 * This class stores a set of drive commands that can be run during auto
 * 
 * @author amind
 * 
 */
public class DriveCommands {
	double wheelCircumference; // meters
	double maxAcceleration;
	double maxDeceleration;
	double maxVelocity;

	/**
	 * initializes the DriveCommands object with the parameters of the drivetrain to control
	 * 
	 * @param wheelCircumference the circumference of the drivetrain's wheels in meters
	 * @param maxAcceleration the drivetrain's maximum acceleration in meters per second squared
	 * @param maxDeceleration the drivetrain's maximum deceleration in meters per second squared
	 * @param topSpeed the drivetrain's top speed in meters per second
	 */
	public DriveCommands(double wheelCircumference, double maxAcceleration, double maxDeceleration, double topSpeed) {
		this.wheelCircumference = wheelCircumference;
		this.maxVelocity = topSpeed;
		this.maxAcceleration = maxAcceleration;
		this.maxDeceleration = maxDeceleration;
	}

	/**
	 * follows a trapezoidal motion profile to drive the given distance<br>
	 * combines a followProfileCommand and the pidDo commands of the left and right {@link SynchronousPIDController} objects
	 * 
	 * @param meters the distance to drive in meters
	 * @param left the PID controller for the left wheels
	 * @param right the PID controller for the right wheels
	 * @return the drive command
	 */
	public Command driveMetersCommand(double meters, SynchronousPIDController<?, Position> left,
			SynchronousPIDController<?, Position> right,RangeIn<Speed> rightSpd,RangeIn<Speed> leftSpd) {
		Command followProfile = driveMetersCommand(meters, left.getSetpointSetter(), right.getSetpointSetter(),
				left.getSource(), right.getSource(),rightSpd,leftSpd);
		Command leftPidDoCommand = left.getPIDDoCommand(() -> {
			return followProfile.isFinished();
		});
		Command rightPidDoCommand = left.getPIDDoCommand(() -> {
			return followProfile.isFinished();
		});
		return CommandUtil.combineSimultaneous(followProfile, leftPidDoCommand, rightPidDoCommand);
	}

	/**
	 * follows a trapezoidal motion profile to drive the given distance
	 * 
	 * @param meters the distance to drive in meters
	 * @param left a position output for the left wheels
	 * @param right a position output for the right wheels
	 * @param leftIn a position input for the left wheels
	 * @param rightIn a position input for the right wheels
	 * @return the drive command
	 */
	public Command driveMetersCommand(double meters, RangeOut<Position> left, RangeOut<Position> right,
			RangeIn<Position> leftIn, RangeIn<Position> rightIn, RangeIn<Speed> leftSpd, RangeIn<Speed> rightSpd) {
		Command leftFollowCommand = new Command() {
			FollowProfileCommand followProfileCommand;

			@Override
			protected void initialize() {
				TrapezoidalMotionProfile profileLeft = new TrapezoidalMotionProfile(leftSpd.get(), meters,
						maxAcceleration, maxAcceleration, maxVelocity);
				followProfileCommand = new FollowProfileCommand(profileLeft, left, leftIn);
				followProfileCommand.initialize();
			}

			@Override
			protected boolean execute() {
				return followProfileCommand.execute();
			}
		};
		Command rightFollowCommand = new Command() {
			FollowProfileCommand followProfileCommand;

			@Override
			protected void initialize() {
				TrapezoidalMotionProfile profileRight = new TrapezoidalMotionProfile(rightSpd.get(), meters,
						maxAcceleration, maxAcceleration, maxVelocity);
				followProfileCommand = new FollowProfileCommand(profileRight, right, rightIn);
				followProfileCommand.initialize();
			}

			@Override
			protected boolean execute() {
				return followProfileCommand.execute();
			}
		};
		return CommandUtil.combineSimultaneous(leftFollowCommand, rightFollowCommand);
	}

	/**
	 * turns the given angle
	 * 
	 * @param angle the angle to turn
	 * @param tolerance the tolerance around the target angle
	 * @param gyro an angle input representing the robot's angle
	 * @param left a speed output for the left wheels
	 * @param right a speed output for the right wheels
	 * @param turnPID the pidconstants for the turn's PIDcontroller
	 * @return the turn command
	 */
	public Command turnAngleCommand(double angle, double tolerance, RangeIn<Angle> gyro, RangeOut<Speed> left,
			RangeOut<Speed> right, PIDConfiguration turnPID) {
		return new TurnAngleCommand<Speed>(0, true, 2, gyro, left.addFollowers(right.invert()), turnPID);
	}
}
