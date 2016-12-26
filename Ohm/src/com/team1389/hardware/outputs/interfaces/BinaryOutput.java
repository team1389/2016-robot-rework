package com.team1389.hardware.outputs.interfaces;

import java.util.function.Consumer;

/**
 * A component that can be turned on or off
 * 
 * @author Prinz
 */
public interface BinaryOutput extends Consumer<Boolean>{
	public void set(boolean onOrOff);

	public static BinaryOutput invert(BinaryOutput out) {
		return (boolean val) -> {
			out.set(!val);
		};
	}

}
