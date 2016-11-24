package com.team1389.hardware.interfaces.inputs;

import com.team1389.hardware.util.RangeUtil;
import com.team1389.hardware.watch.Info;
import com.team1389.hardware.watch.NumberInfo;
import com.team1389.hardware.watch.Watchable;

public interface OpenRangeInput {
	public double get();

	public double min();

	public double max();

	public static WatchableOpenRangeInput getWatchableVersion(OpenRangeInput in, String name) {
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

	public abstract class WatchableOpenRangeInput implements Watchable, OpenRangeInput {
	}

	public static OpenRangeInput mapToOpenRange(OpenRangeInput in, double min, double max) {
		return new OpenRangeInput() {

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

	public static OpenRangeInput mapToOpenRange(PercentRangeInput in, double min, double max) {
		return mapToOpenRange(getAsOpenRange(in), min, max);
	}

	public static OpenRangeInput getAsOpenRange(PercentRangeInput in) {
		return new OpenRangeInput() {

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

	public static OpenRangeInput invert(OpenRangeInput in) {
		return new OpenRangeInput() {

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
