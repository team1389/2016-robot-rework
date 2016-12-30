package com.team1389.system.drive;

import com.team1389.control.SynchronousPID;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.value_types.Speed;
import com.team1389.trajectory.Rotation2d;

public class VelocityHeadingSystem {
	//TODO figure out how to use this drive algorithm
	protected DriveOut<Speed> drive;
	private AngleIn gyro;

	private double mLastHeadingErrorDegrees;
	private VelocityHeadingSetpoint velocityHeadingSetpoint_;
	private SynchronousPID velocityHeadingPid_;

	public VelocityHeadingSystem(DriveOut<Speed> drive, AngleIn gyro) {
		this.drive = drive;
		this.gyro = gyro;
	}

	public void update() {
		Rotation2d actualGyroAngle = Rotation2d.fromDegrees(gyro.get());

		mLastHeadingErrorDegrees = velocityHeadingSetpoint_.getHeading().rotateBy(actualGyroAngle.inverse())
				.getDegrees();

		double deltaSpeed = velocityHeadingPid_.calculate(mLastHeadingErrorDegrees);
		drive.set(velocityHeadingSetpoint_.getLeftSpeed() + deltaSpeed / 2,
				velocityHeadingSetpoint_.getRightSpeed() - deltaSpeed / 2);
	}

	public void setVelocityHeadingSetpoint(double forward_inches_per_sec, Rotation2d headingSetpoint) {
		velocityHeadingPid_.reset();
		velocityHeadingSetpoint_ = new VelocityHeadingSetpoint(forward_inches_per_sec, forward_inches_per_sec,
				headingSetpoint);
	}
}
