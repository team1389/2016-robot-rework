package com.team1389.hardware.outputs.hardware;

import java.util.ArrayList;
import java.util.List;

import com.team1389.hardware.control.PIDConfiguration;
import com.team1389.hardware.outputs.interfaces.CANTalonFollower;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.valueTypes.Position;
import com.team1389.hardware.valueTypes.Speed;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.Watchable;

public class CANTalonGroup implements Watchable {

	private final CANTalonHardware main;
	private final List<CANTalonFollower> followers;

	public CANTalonGroup(CANTalonHardware main, CANTalonHardware... followers) {
		this.main = main;

		this.followers = new ArrayList<CANTalonFollower>();
		for (CANTalonHardware talon : followers) {
			this.followers.add(talon.getFollower(main));
		}
	}

	private void setFollowers() {
		for (CANTalonFollower follower : followers) {
			follower.follow();
		}
	}

	public PercentOut getVoltageOutput() {
		PercentOut mainOutput = main.getVoltageOutput();
		return new PercentOut((double voltage) -> {
			setFollowers();
			mainOutput.set(voltage);
		});
	}

	public RangeOut<Position> getPositionOutput(PIDConfiguration config) {
		RangeOut<Position> mainOutput = main.getPositionOutput(config);
		return new RangeOut<Position>((double position) -> {
			setFollowers();
			mainOutput.set(position);
		} , mainOutput.min(), mainOutput.max());
	}

	public RangeOut<Speed> getSpeedOutput(PIDConfiguration config) {
		RangeOut<Speed> mainOutput = main.getSpeedOutput(config);
		return new RangeOut<Speed>((double speed) -> {
			setFollowers();
			mainOutput.set(speed);
		} , mainOutput.min(), mainOutput.max());

	}

	public CANTalonHardware getLeader() {
		return main;
	}

	@Override
	public String getName() {
		return "CANTalon Group: " + main.getName() + " ";
		// TODO add followers
	}

	@Override
	public Info[] getInfo() {
		// TODO Auto-generated method stub0
		return null;
	}
}
