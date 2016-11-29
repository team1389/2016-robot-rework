package com.team1389.hardware.util;

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

}