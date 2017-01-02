package com.team1389.system.drive;

import java.util.Set;

import com.team1389.hardware.value_types.Speed;
import com.team1389.trajectory.AdaptivePurePursuitController;
import com.team1389.trajectory.Kinematics;
import com.team1389.trajectory.Path;
import com.team1389.trajectory.RigidTransform2d;
import com.team1389.trajectory.RobotStateEstimator;

import edu.wpi.first.wpilibj.Timer;

/**
 * drive system that follows paths autonomously using the Adaptive Pure Pursuit Controller
 * 
 * @see AdaptivePurePursuitController
 * @author amind
 *
 */
public class PathFollowingSystem {
	/**
	 * the default distance to look down the path for the Adaptive Pure Pursuit Controller
	 * 
	 * @see AdaptivePurePursuitController
	 */
	public static double PATH_LOOKAHEAD = 24;// inches
	/**
	 * the default time between updates
	 */
	public static double UPDATE_DT = 1 / 50;
	private AdaptivePurePursuitController pathFollowingController;
	private Kinematics kinematics;
	private double maxVel, maxAccel;
	private RobotStateEstimator state;
	private DriveOut<Speed> drive;

	/**
	 * 
	 * @param drive a speed controlled drive stream
	 * @param state a state stream for the robot
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
	 * @param path the path to follow
	 * @param reversed whether to follow the path in reverse
	 * @see Path
	 */
	public synchronized void followPath(Path path, boolean reversed) {
		pathFollowingController = new AdaptivePurePursuitController(PATH_LOOKAHEAD, maxAccel, UPDATE_DT, path, reversed,
				0.25);
	}

	/**
	 * @return a list of identifiers for the waypoints that have been crossed so far
	 */
	public synchronized Set<String> getPathMarkersCrossed() {
		return pathFollowingController.getMarkersCrossed();
	}

	/**
	 * @return Returns if the robot mode is Path Following Control and the set path is complete.
	 */
	public synchronized boolean isFinishedPath() {
		return pathFollowingController.isDone();
	}

	/**
	 * updates the pathFollower, setting the speeds of the wheels based on the robot's current pose
	 */
	public void update() {
		RigidTransform2d robotPose = state.get();
		RigidTransform2d.Delta desiredRobotVelocity = pathFollowingController.calculate(robotPose,
				Timer.getFPGATimestamp());
		Kinematics.DriveVelocity desiredWheelSpeeds = kinematics.inverseKinematics(desiredRobotVelocity);

		// Scale the command to respect the max velocity limits
		double max_vel = 0.0;
		max_vel = Math.max(max_vel, Math.abs(desiredWheelSpeeds.left));
		max_vel = Math.max(max_vel, Math.abs(desiredWheelSpeeds.right));
		if (max_vel > maxVel) {
			double scaling = maxVel / max_vel;
			desiredWheelSpeeds = new Kinematics.DriveVelocity(desiredWheelSpeeds.left * scaling,
					desiredWheelSpeeds.right * scaling);
		}
		drive.set(desiredWheelSpeeds.left, desiredWheelSpeeds.right);
	}

}
