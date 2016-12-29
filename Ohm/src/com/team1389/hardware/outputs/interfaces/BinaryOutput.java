package com.team1389.hardware.outputs.interfaces;

import java.util.function.Consumer;

/**
 * A component that can be turned on or off
 * 
 * @author Prinz
 */
public interface BinaryOutput extends Consumer<Boolean> {
	public void set(Boolean onOrOff);

	@Override
	public default void accept(Boolean val) {
		set(val);
	}

	public static BinaryOutput invert(BinaryOutput out) {
		return (Boolean val) -> {
			out.set(!val);
		};
	}

	public static BinaryOutput convert(Consumer<Boolean> consumer) {
		return consumer::accept;
	}

}
