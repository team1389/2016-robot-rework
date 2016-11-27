package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.inputs.interfaces.BooleanSource;
import com.team1389.hardware.inputs.software.DigitalInput;
import com.team1389.hardware.inputs.software.DigitalInput.InputStyle;
import com.team1389.hardware.registry.Registry;
import com.team1389.watch.BooleanInfo;
import com.team1389.watch.Info;
import com.team1389.watch.Watchable;

public class SwitchHardware implements Watchable {
	boolean inverted;

	edu.wpi.first.wpilibj.DigitalInput wpiSwitch;

	public SwitchHardware(int dioPort, Registry registry) {
		registry.claimDIOPort(dioPort);
		registry.registerWatcher(this);
		wpiSwitch = new edu.wpi.first.wpilibj.DigitalInput(dioPort);
	}

	private boolean get() {
		return inverted ? !wpiSwitch.get() : wpiSwitch.get();
	}

	public BooleanSource getRawSwitch() {
		return () -> {
			return get();
		};
	}

	public static BooleanSource combineSwitchAND(SwitchHardware... switches) {
		return () -> {
			boolean stillTrue = true;
			for (SwitchHardware switchHardware : switches) {
				stillTrue = stillTrue && switchHardware.get();
			}
			return stillTrue;
		};
	}

	public static BooleanSource combineSwitchOR(SwitchHardware... switches) {
		return () -> {
			boolean stillTrue = false;
			for (SwitchHardware switchHardware : switches) {
				stillTrue = stillTrue || switchHardware.get();
			}
			return stillTrue;
		};
	}

	public DigitalInput getSwitchAsInput(InputStyle style) {
		return DigitalInput.createInput(getRawSwitch(), style);
	}

	@Override
	public String getName() {
		return wpiSwitch.getSmartDashboardType() + wpiSwitch.getChannel();
	}

	public void invert(boolean inverted) {
		this.inverted = inverted;
	}

	@Override
	public Info[] getInfo() {
		return new Info[] { new BooleanInfo(getName(), () -> {
			return wpiSwitch.get();
		}) };
	}

}
