package com.team1389.util;

/**
 * tracks a raw boolean value and generates a new boolean value that toggles every time the original switches from false to true
 * 
 * @author Kenneth
 *
 */
public class ToggleBoolean extends LatchedBoolean {
	boolean toggle;

	@Override
	protected boolean update(boolean newVal) {
		if (super.update(newVal)) {
			toggle = !toggle;
		}
		return toggle;
	}

	/**
	 * @return the state of the toggle
	 */
	@Override
	public boolean get(boolean newVal) {
		return update(newVal);
	}
}
