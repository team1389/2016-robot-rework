package com.team1389.hardware.outputs.interfaces;

import java.util.function.Consumer;

/**
 * a boolean output stream
 * 
 * @author Prinz
 */
public interface BinaryOutput extends Consumer<Boolean> {
	/**
	 * @param val the value to pass down the stream
	 */
	public void set(Boolean val);

	@Override
	public default void accept(Boolean val) {
		set(val);
	}

	/**
	 * inverts the ouput of the stream
	 * @param out the stream to invert
	 * @return the inverted stream
	 */
	public static BinaryOutput invert(BinaryOutput out) {
		return (Boolean val) -> {
			out.set(!val);
		};
	}
	
	/**
	 * @param consumer the consumer to convert
	 * @return an output stream that passes its stream down to the given consumer
	 */
	public static BinaryOutput convert(Consumer<Boolean> consumer) {
		return consumer::accept;
	}

}
