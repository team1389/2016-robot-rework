package com.team1389.hardware.inputs.interfaces;

import com.team1389.hardware.value_types.Value;

/**
 * A double stream that listens for a change in the value and executes a runnable on change.
 * Note that this class only checks for a change when the get method is called.
 * @author Josh
 *
 * @param <T> The stream's type  
 */
public class ListeningScalarInput<T extends Value> implements ScalarInput<T>{
	private ScalarInput<T> in;
	private Runnable onChange;
	private double oldVal;

	/**
	 * @param in The stream to listen for changes in
	 * @param onChange The runnable to execute when a change is detected. 
	 */
	protected ListeningScalarInput(ScalarInput<T> in, Runnable onChange) {
		this.onChange = onChange;
		this.in = in;
	}

	/**
	 * Note again that this is the method that checks for a change, between the current value and the value from the last time this method was called.
	 * If there is a change it calls the runnable passed in through the constructor.
	 * 
	 */
	public double get() {
		double newVal = in.get();
		if (oldVal != newVal) {
			onChange.run();
		}
		oldVal = newVal;
		return newVal;
	}
}
