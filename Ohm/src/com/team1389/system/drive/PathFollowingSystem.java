package com.team1389.system.drive;

import java.util.Set;

import com.team1389.hardware.value_types.Speed;
import com.team1389.trajectory.AdaptivePurePursuitController;
import com.team1389.trajectory.Kinematics;
import com.team1389.trajectory.Path;
import com.team1389.trajectory.RigidTransform2d;
import com.team1389.trajectory.RobotStateEstimator;

import edu.wpi.first.wpilibj.Timer;

public class PathFollowingSystem {
	public static double PATH_LOOKAHEAD = 24;// inches
	public static double UPDATE_DT = 1 / 50;
	private AdaptivePurePursuitController pathFollowingController_;
	private Kinematics kinematics;
	private double maxVel, maxAccel;
	private RobotStateEstimator state;
	private DriveOut<Speed> drive;

	/**
	 * 
	 * @param drive
	 * @param gyro
	 * @param maxVel in inches/sec
	 * @param maxAccel in inches/sec^2
	 * @param trackWidth in inches
	 * @param trackLength in inches
	 * @param scrub in inches
	 */
	public PathFollowingSystem(DriveOut<Speed> drive, RobotStateEstimator state, double maxVel, double maxAccel,
			double trackWidth, double trackLength, double scrub) {
		this.drive = drive;
		this.state = state;
		this.kinematics = new Kinematics(trackLength, trackWidth, scrub);
	}

	/**
	 * The robot follows a set path, which is defined by Waypoint objects.
	 * 
	 * @param Path to follow
	 * @param reversed
	 * @see com.team254.lib.util/Path.java
	 */
	public synchronized void followPath(Path path, boolean reversed) {
		pathFollowingController_ = new AdaptivePurePursuitController(PATH_LOOKAHEAD, maxAccel, UPDATE_DT, path,
				reversed, 0.25);
	}

	public synchronized Set<String> getPathMarkersCrossed() {
		return pathFollowingController_.getMarkersCrossed();
	}

	/**
	 * @return Returns if the robot mode is Path Following Control and the set path is complete.
	 */
	public synchronized boolean isFinishedPath() {
		return pathFollowingController_.isDone();
	}

	public void update() {
		RigidTransform2d robot_pose = state.get();
		RigidTransform2d.Delta command = pathFollowingController_.update(robot_pose, Timer.getFPGATimestamp());
		Kinematics.DriveVelocity setpoint = kinematics.inverseKinematics(command);

		// Scale the command to respect the max velocity limits
		double max_vel = 0.0;
		max_vel = Math.max(max_vel, Math.abs(setpoint.left));
		max_vel = Math.max(max_vel, Math.abs(setpoint.right));
		if (max_vel > maxVel) {
			double scaling = maxVel / max_vel;
			setpoint = new Kinematics.DriveVelocity(setpoint.left * scaling, setpoint.right * scaling);
		}
		drive.set(setpoint.left, setpoint.right);
	}

}
