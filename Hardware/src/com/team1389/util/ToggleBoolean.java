package com.team1389.util;

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
