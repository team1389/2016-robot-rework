package com.team1389.hardware.outputs.hardware;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.team1389.configuration.PIDConstants;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.util.AddList;
import com.team1389.watch.CompositeWatchable;
import com.team1389.watch.Watchable;

/**
 * represents a group of talons composed of a master talon and a set of slaves that follow the master in any mode
 * 
 * @author amind
 *
 */
public class CANTalonGroup implements CompositeWatchable {

	private final CANTalonHardware main;
	private final List<CANTalonHardware> followers;

	/**
	 * @param main the master talon
	 * @param followers a list of slave talons
	 */
	public CANTalonGroup(CANTalonHardware main, CANTalonHardware... followers) {
		this.main = main;

		this.followers = Arrays.asList(followers);

	}

	private void setFollowers() {
		for (CANTalonHardware follower : followers) {
			follower.getFollower(main).follow();
		}
	}

	/**
	 * 
	 * @return a percent output stream that controls the voltage of each talon in the group
	 */
	public PercentOut getVoltageOutput() {
		PercentOut mainOutput = main.getVoltageOutput();
		return new PercentOut((double voltage) -> {
			setFollowers();
			mainOutput.set(voltage);
		});
	}

	/**
	 * @param config the P,I, and D gains
	 * @return a position output stream that controls the position of the talon group
	 */
	public RangeOut<Position> getPositionOutput(PIDConstants config) {
		RangeOut<Position> mainOutput = main.getPositionOutput(config);
		return new RangeOut<Position>((double position) -> {
			setFollowers();
			mainOutput.set(position);
		}, mainOutput.min(), mainOutput.max());
	}

	/**
	 * 
	 * @param config the P, I, and D gains
	 * @param feedForward the F gain
	 * @return a speed output stream that controls the speed of the talon group
	 */
	public RangeOut<Speed> getSpeedOutput(PIDConstants config, double feedForward) {
		RangeOut<Speed> mainOutput = main.getSpeedOutput(config, feedForward);
		return new RangeOut<Speed>((double speed) -> {
			setFollowers();
			mainOutput.set(speed);
		}, mainOutput.min(), mainOutput.max());

	}

	/**
	 * 
	 * @return the master talon <br>
	 *         *Take me to your leader!*</br>
	 */
	public CANTalonHardware getLeader() {
		return main;
	}

	@Override
	public String getName() {
		return "CANTalon Group: " + main.getName() + " -> "
				+ followers.stream().map(follower -> new String(follower.getName())).collect(Collectors.joining(", "));
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		stem.addAll(followers);
		return stem.put(main);
	}
}
