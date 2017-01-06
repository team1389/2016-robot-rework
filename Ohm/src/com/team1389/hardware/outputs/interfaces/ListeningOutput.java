package com.team1389.hardware.outputs.interfaces;

import java.util.function.Consumer;

/**
 * tracks the values passed to a consumer and notifies a listener if a change to the value occurs
 * 
 * @author Kenneth
 *
 * @param <T> type of value that the double is representing
 */
public class ListeningOutput<T> implements Consumer<T> {
	private Consumer<T> out;
	private Runnable onChange;
	T oldVal;

	protected ListeningOutput(Consumer<T> out, Runnable onChange) {
		this.onChange = onChange;
		this.out = out;
	}

	@Override
	public void accept(T val) {
		out.accept(val);
		if (!val.equals(oldVal)) {
			onChange.run();
		}
		oldVal = val;
	}
}
