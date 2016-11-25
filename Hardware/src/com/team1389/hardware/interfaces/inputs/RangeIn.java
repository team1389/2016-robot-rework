package com.team1389.hardware.interfaces.inputs;

import com.team1389.hardware.util.RangeUtil;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.NumberInfo;
import com.team1389.hardware.watch.Watchable;

public interface RangeIn {
	public double get();

	public double min();

	public double max();

	public static WatchableOpenRangeInput getWatchableVersion(RangeIn in, String name) {
		return new WatchableOpenRangeInput() {
			@Override
			public String getName() {
				return name;
			}

			@Override
			public Info[] getInfo() {
				return new Info[] { new NumberInfo(getName(), () -> {
					return in.get();
				}) };
			}

			@Override
			public double get() {
				return in.get();
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

	public abstract class WatchableOpenRangeInput implements Watchable, RangeIn {
	}

	public static RangeIn mapToOpenRange(RangeIn in, double min, double max) {
		return new RangeIn() {

			@Override
			public double get() {
				return RangeUtil.map(in.get(), in.min(), in.max(), min, max);
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

	public static RangeIn mapToOpenRange(PercentIn in, double min, double max) {
		return mapToOpenRange(getAsOpenRange(in), min, max);
	}

	public static RangeIn getAsOpenRange(PercentIn in) {
		return new RangeIn() {

			@Override
			public double get() {
				return in.get();
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

	public static RangeIn invert(RangeIn in) {
		return new RangeIn() {

			@Override
			public double get() {
				return -in.get();
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
