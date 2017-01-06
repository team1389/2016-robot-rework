package com.team1389.hardware.outputs.software;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import com.team1389.hardware.outputs.interfaces.BinaryOutput;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.BooleanInfo;
/**
 * a stream of booleans with methods to manipulate it
 * @author Kenneth
 *
 */
public class DigitalOut implements BooleanSupplier {
	BinaryOutput out;
	private boolean last;
	/**
	 * 
	 * @param out stream of booleans
	 */
	public DigitalOut(BinaryOutput out) {
		this.out = out;
	}
	/**
	 * @param out the boolean consumer
	 */
	public DigitalOut(Consumer<Boolean> out){
		this(BinaryOutput.convert(out));
	}
	/**
	 * 
	 * @param onOrOff value to be passed down the stream
	 */
	public void set(boolean onOrOff) {
		this.last = onOrOff;
		out.set(onOrOff);
	}
	/**
	 * 
	 * @return this stream but inverted
	 */
	public DigitalOut invert() {
		this.out = BinaryOutput.invert(out);
		return this;
	}
	/**
	 * 
	 * @param name  the String identifier of the watchable
	 * @return a watchable that tracks the boolean stream 
	 */
	public Watchable getWatchable(String name) {
		return new BooleanInfo(name, this);
	}
	/**
	 * @return value of the stream
	 */
	@Override
	public boolean getAsBoolean() {
		return last;
	}
}
