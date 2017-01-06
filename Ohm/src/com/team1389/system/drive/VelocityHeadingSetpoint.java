package com.team1389.system.drive;

import com.team1389.trajectory.Rotation2d;

/**
 * represents a desired set of wheel speeds and heading of the robot. Especially useful if the robot is negotiating a turn and to forecast the robot's location.
 */
public class VelocityHeadingSetpoint {
	private final double leftSpeed_;
	private final double rightSpeed_;
	private final Rotation2d headingSetpoint_;

	/**
	 * @param leftSpeed the desired speed of the left wheels
	 * @param rightSpeed the desired speed of the right wheels
	 * @param headingSetpoint the desired heading of the robot
	 */
	public VelocityHeadingSetpoint(double leftSpeed, double rightSpeed, Rotation2d headingSetpoint) {
		leftSpeed_ = leftSpeed;
		rightSpeed_ = rightSpeed;
		headingSetpoint_ = headingSetpoint;
	}

	/**
	 * @return the desired speed of the left wheels
	 */
	public double getLeftSpeed() {
		return leftSpeed_;
	}

	/**
	 * @return the desired speed of the right wheels
	 */
	public double getRightSpeed() {
		return rightSpeed_;
	}

	/**
	 * @return the desired heading of the robot
	 */
	public Rotation2d getHeading() {
		return headingSetpoint_;
	}
}