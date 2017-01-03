package com.team1389.hardware.inputs.interfaces;

import com.team1389.util.ToggleBoolean;
/**
 * a stream of booleans which are toggled
 * @author Kenneth
 *
 */
public class ToggledBinaryInput implements BinaryInput {
	ToggleBoolean toggleIn;
	BinaryInput input;
	/**
	 * 
	 * @param input stream to operate on
	 */
	public ToggledBinaryInput(BinaryInput input) {
		toggleIn = new ToggleBoolean();
		this.input = input;
	}

	@Override
	public Boolean get() {
		return toggleIn.get(input.get());
	}

}
