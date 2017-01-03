package com.team1389.hardware.inputs.interfaces;

/**
 * a boolean stream that tracks changes and notifies a listener
 * @author amind
 *
 */
public class ListeningBinaryInput implements BinaryInput {
	private BinaryInput in;
	private Runnable onChange;
	private boolean oldVal;

	protected ListeningBinaryInput(BinaryInput in, Runnable onChange) {
		this.onChange = onChange;
		this.in = in;
	}

	@Override
	public Boolean get() {
		boolean newVal = in.get();
		if (oldVal != newVal) {
			onChange.run();
		}
		oldVal = newVal;
		return newVal;
	}
}
