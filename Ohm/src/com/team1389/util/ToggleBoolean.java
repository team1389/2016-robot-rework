package com.team1389.util;
/**
 * returns the same value repeatedly until the boolean value being passed in changes, which inverts the value being returned repeatedly
 * @author Kenneth
 *
 */
public class ToggleBoolean extends LatchedBoolean{
	boolean toggle;
	@Override protected boolean update(boolean newVal){
		if(super.update(newVal)){
			toggle=!toggle;
		}
		return toggle;
	}
	@Override public boolean get(boolean newVal){
		return update(newVal);
	}
}
