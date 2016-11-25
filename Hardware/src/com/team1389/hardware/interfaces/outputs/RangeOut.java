package com.team1389.hardware.interfaces.outputs;

import com.team1389.hardware.util.RangeUtil;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.NumberInfo;
import com.team1389.hardware.watch.Watchable;

public interface RangeOut {
	public void set(double val);

	public double min();

	public double max();

	public static RangeOut mapToOpenRange(RangeOut in, double min, double max) {
		return new RangeOut() {

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
	public static RangeOut applyProfile(RangeOut o,double maxSpeed){
		return new ProfiledRangeOut(o,maxSpeed);
	}
	public static RangeOut mapToOpenRange(PercentOut in, double min, double max) {
		return mapToOpenRange(getAsOpenRange(in), min, max);
	}
	public abstract class WatchableOpenRangeOutput implements RangeOut,Watchable{
		
	}
	public static WatchableOpenRangeOutput generateWatchable(RangeOut out,String name){
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
	public static RangeOut getAsOpenRange(PercentOut in) {
		return new RangeOut() {

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
	public static RangeOut invert(RangeOut in){
		return new RangeOut() {

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
