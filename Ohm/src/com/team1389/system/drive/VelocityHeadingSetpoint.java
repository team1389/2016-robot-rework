package com.team1389.system.drive;

import com.team1389.trajectory.Rotation2d;

/**
 * VelocityHeadingSetpoints are used to calculate the robot's path given the speed of the robot in each wheel and the polar coordinates. Especially useful if the robot is negotiating a turn and to forecast the robot's location.
 */
public class VelocityHeadingSetpoint {
	private final double leftSpeed_;
	private final double rightSpeed_;
	private final Rotation2d headingSetpoint_;

	// Constructor for straight line motion
	public VelocityHeadingSetpoint(double leftSpeed, double rightSpeed, Rotation2d headingSetpoint) {
		leftSpeed_ = leftSpeed;
		rightSpeed_ = rightSpeed;
		headingSetpoint_ = headingSetpoint;
	}

	public double getLeftSpeed() {
		return leftSpeed_;
	}

	public double getRightSpeed() {
		return rightSpeed_;
	}

	public Rotation2d getHeading() {
		return headingSetpoint_;
	}
}