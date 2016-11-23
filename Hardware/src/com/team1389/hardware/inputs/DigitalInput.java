package com.team1389.hardware.inputs;

import com.team1389.hardware.interfaces.inputs.BooleanSource;
import com.team1389.hardware.watch.BooleanInfo;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.Watchable;

/**
 * Any input with two states, like a limit switch
 * 
 * @author Jacob Prinz
 */
public abstract class DigitalInput implements Watchable {
	BooleanSource in;
	String name;

	public abstract boolean get();

	public DigitalInput(BooleanSource in, String name) {
		this.name = name;
		this.in = in;
	}

	public String getName() {
		return toString();
	}

	public Info[] getInfo() {
		return new Info[] { new BooleanInfo(getName(), () -> {
			return get();
		})};
	}

	public DigitalInput(BooleanSource in) {
		this(in, null);
	}

	public void setBooleanSource(BooleanSource in) {
		this.in = in;
	}

	public enum InputStyle {
		RAW, LATCHED, TOGGLED;
	}

	public static DigitalInput createInput(BooleanSource in, InputStyle style) {
		switch (style) {
		case LATCHED:
			return new LatchedDigitalInput(in, "");
		case RAW:
			return new RawDigitalInput(in);
		case TOGGLED:
			return new ToggledDigitalInput(in);
		default:
			return new RawDigitalInput(in);
		}
	}

	public String toString() {
		return name != null ? name : super.toString();
	}

}
