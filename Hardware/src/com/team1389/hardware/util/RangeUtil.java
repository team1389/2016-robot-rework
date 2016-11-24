package com.team1389.hardware.util;

public class RangeUtil {
	public static double map(double x,double in_min,double in_max,double out_min,double out_max){
		  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	public static double limit(double v,double limit){
        return (Math.abs(v) < limit) ? v : limit * (v < 0 ? -1 : 1);
	}
	public static double applyDeadband(double val, double deadband){
        return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
	}
	public static void main(String[] args){
		System.out.println(map(45,0,360,0,4096));
	}
}
