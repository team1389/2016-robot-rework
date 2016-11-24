package com.team1389.hardware.util;

import com.team1389.hardware.interfaces.outputs.OpenRangeOutput;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Timer.Interface;
import edu.wpi.first.wpilibj.Timer.StaticInterface;

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
		OpenRangeOutput printer=new OpenRangeOutput(){

			@Override
			public void set(double val) {
				System.out.println(val);
			}

			@Override
			public double min() {
				return 0;
			}

			@Override
			public double max() {
				return 8192;
			}
			
		};
	}
}
