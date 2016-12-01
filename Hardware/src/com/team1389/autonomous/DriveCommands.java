package com.team1389.autonomous;

import com.team1389.commands.CommandUtil;
import com.team1389.commands.FollowProfileCommand;
import com.team1389.commands.command_base.Command;
import com.team1389.control.PIDConfiguration;
import com.team1389.control.SynchronousPIDController;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Angle;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.motion_profile.TrapezoidalMotionProfile;

public class DriveCommands {
	static final double inchesToMeters = .0245;
	double wheelCircumference; // meters
	double maxAcceleration;
	double topSpeed;

	public DriveCommands(double wheelDiameter, PIDConfiguration turnPID, double maxAcceleration, double topSpeed) {
		this.wheelCircumference = wheelDiameter * Math.PI * inchesToMeters;
		this.topSpeed = topSpeed;
		this.maxAcceleration = maxAcceleration;
	}

	public Command driveMetersCommand(double meters, SynchronousPIDController<?, Position> left,
			SynchronousPIDController<?, Position> right) {
		Command followProfile = driveMetersCommand(meters, left.getSetpointSetter(), right.getSetpointSetter(),
				left.getSource(), right.getSource());
		Command leftPidDoCommand = left.getPIDDoCommand(() -> {
			return followProfile.isFinished();
		});
		Command rightPidDoCommand = left.getPIDDoCommand(() -> {
			return followProfile.isFinished();
		});
		return CommandUtil.combineSimultaneous(followProfile, leftPidDoCommand, rightPidDoCommand);
	}

	public Command driveMetersCommand(double meters, RangeOut<Position> left, RangeOut<Position> right,
			RangeIn<Position> leftIn, RangeIn<Position> rightIn) {
		TrapezoidalMotionProfile profile = new TrapezoidalMotionProfile(meters, maxAcceleration, maxAcceleration,
				topSpeed);
		Command leftFollowCommand = new FollowProfileCommand(profile, left, leftIn);
		Command rightFollowCommand = new FollowProfileCommand(profile, right, rightIn);
		return CommandUtil.combineSimultaneous(leftFollowCommand, rightFollowCommand);
	}

	public Command turnAngleCommand(double angle, double tolerance, RangeIn<Angle> gyro, RangeOut<Speed> left,
			RangeOut<Speed> right, PIDConfiguration turnPID) {
		SynchronousPIDController<Speed, Angle> pid = new SynchronousPIDController<Speed, Angle>(turnPID, gyro,
				left.addFollowers(right.invert()));
		return new Command() {
			@Override
			public void initialize() {
				pid.setSetpoint(angle);
			}

			@Override
			public boolean execute() {
				pid.update();
				return pid.onTarget(tolerance);
			}

		};
	}
}
