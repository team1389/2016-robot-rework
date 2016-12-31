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

public class CANTalonGroup implements CompositeWatchable {

	private final CANTalonHardware main;
	private final List<CANTalonHardware> followers;

	public CANTalonGroup(CANTalonHardware main, CANTalonHardware... followers) {
		this.main = main;

		this.followers = Arrays.asList(followers);

	}

	private void setFollowers() {
		for (CANTalonHardware follower : followers) {
			follower.getFollower(main).follow();
		}
	}

	public PercentOut getVoltageOutput() {
		PercentOut mainOutput = main.getVoltageOutput();
		return new PercentOut((double voltage) -> {
			setFollowers();
			mainOutput.set(voltage);
		});
	}

	public RangeOut<Position> getPositionOutput(PIDConstants config) {
		RangeOut<Position> mainOutput = main.getPositionOutput(config);
		return new RangeOut<Position>((double position) -> {
			setFollowers();
			mainOutput.set(position);
		}, mainOutput.min(), mainOutput.max());
	}

	public RangeOut<Speed> getSpeedOutput(PIDConstants config) {
		RangeOut<Speed> mainOutput = main.getSpeedOutput(config);
		return new RangeOut<Speed>((double speed) -> {
			setFollowers();
			mainOutput.set(speed);
		}, mainOutput.min(), mainOutput.max());

	}

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
