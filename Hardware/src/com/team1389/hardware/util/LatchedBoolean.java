package com.team1389.hardware.util;

import com.team1389.hardware.inputs.interfaces.BooleanSource;

/**
 * An iterative boolean latch.
 * 
 * Returns true once if and only if the value of newValue changes from false to
 * true.
 */
public class LatchedBoolean {
    private boolean mLast = false;
    boolean val;

    public LatchedBoolean(boolean initialVal){
    	this.val=initialVal;
    }
    public LatchedBoolean(){
    	this.val=false;
    }
    protected boolean update(boolean newValue) {
        boolean ret = false;
        if (newValue && !mLast) {
            ret = true;
        }
        mLast = newValue;
        val=ret;
        return ret;
    }
    public boolean get(boolean newVal){
    	return update(newVal);
    }
	public static void main(String[] args){
		BooleanSource b=()->{return System.currentTimeMillis()%1000>500;};
		BooleanSource latchedB=new BooleanSource(){
			LatchedBoolean latched=new LatchedBoolean();
			boolean toggled;
			@Override
			public boolean get() {
				return latched.get(b.get());
			}
			
		};
		BooleanSource toggledB=new BooleanSource(){
			boolean toggled;
			@Override
			public boolean get() {
				if(latchedB.get()){
					toggled=!toggled;
				}
				return toggled;
			}
			
		};
		while(true){
			System.out.println(toggledB.get());
		}
	}

}