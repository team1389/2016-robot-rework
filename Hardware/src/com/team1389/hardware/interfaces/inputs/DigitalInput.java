package com.team1389.hardware.interfaces.inputs;

/**
 * Any input with two states, like a limit switch 
 * @author Jacob Prinz
 */
public abstract class DigitalInput {
	BooleanSource in;
	public abstract boolean get();
	public DigitalInput(BooleanSource in){
		this.in=in;
	}
	public void setBooleanSource(BooleanSource in){
		this.in=in;
	}
	public enum InputStyle{
		RAW,
		LATCHED,
		TOGGLED;
	}
	public static DigitalInput createInput(BooleanSource in,InputStyle style){
		switch(style){
		case LATCHED:
			return new LatchedDigitalInput(in);
		case RAW:
			return new RawDigitalInput(in);
		case TOGGLED:
			return new ToggledDigitalInput(in);
		default:
			return new RawDigitalInput(in);
		}
	}

}

