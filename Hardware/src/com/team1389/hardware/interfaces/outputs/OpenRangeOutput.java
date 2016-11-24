package com.team1389.hardware.interfaces.outputs;

import com.team1389.hardware.util.RangeUtil;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.NumberInfo;
import com.team1389.hardware.watch.Watchable;

public interface OpenRangeOutput {
	public void set(double val);

	public double min();

	public double max();

	public static OpenRangeOutput mapToOpenRange(OpenRangeOutput in, double min, double max) {
		return new OpenRangeOutput() {

			@Override
			public void set(double val) {
				in.set(RangeUtil.map(val, min, max, in.min(), in.max()));
			}

			@Override
			public double min() {
				return min;
			}

			@Override
			public double max() {
				return max;
			}

		};
	}
	public static OpenRangeOutput applyProfile(OpenRangeOutput o,double maxSpeed){
		return new ProfiledOpenRangeOutput(o,maxSpeed);
	}
	public static OpenRangeOutput mapToOpenRange(PercentRangeOutput in, double min, double max) {
		return mapToOpenRange(getAsOpenRange(in), min, max);
	}
	public abstract class WatchableOpenRangeOutput implements OpenRangeOutput,Watchable{
		
	}
	public static WatchableOpenRangeOutput generateWatchable(OpenRangeOutput out,String name){
		return new WatchableOpenRangeOutput(){
			double val;
			@Override
			public void set(double val) {
				out.set(val);
				this.val=val;
			}

			@Override
			public double min() {
				return out.min();
			}

			@Override
			public double max() {
				return out.max();
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public Info[] getInfo() {
				return new Info[]{new NumberInfo(getName(),()->{return val;})};
			}
			
		};
	}
	public static WatchableOpenRangeOutput generatePrintOutput(double min,double max,String name){
		return new WatchableOpenRangeOutput(){
			double val=0.0;
			@Override
			public void set(double val) {
				this.val=val;
			}

			@Override
			public double min() {
				return min;
			}

			@Override
			public double max() {
				return max;
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public Info[] getInfo() {
				return new Info[]{
						new NumberInfo(getName(),()->{return val;})
				};
			}
			
		};
	}
	public static OpenRangeOutput getAsOpenRange(PercentRangeOutput in) {
		return new OpenRangeOutput() {

			@Override
			public void set(double val) {
				in.set(val);
			}

			@Override
			public double min() {
				return -1;
			}

			@Override
			public double max() {
				return 1;
			}

		};
	}
	public static OpenRangeOutput invert(OpenRangeOutput in){
		return new OpenRangeOutput() {

			@Override
			public void set(double val) {
				in.set(-val);
			}

			@Override
			public double min() {
				return in.min();
			}

			@Override
			public double max() {
				return in.max();
			}
		};
	}

}
