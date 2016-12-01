package com.team1389.hardware.outputs.interfaces;

import com.team1389.hardware.value_types.Angle;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Value;
import com.team1389.util.RangeUtil;

public interface ScalarOutput<T extends Value> {
	public void set(double val);

	public static <T extends Value> ScalarOutput<T> mapToRange(ScalarOutput<T> out, double inMin, double inMax,
			double outMin, double outMax) {
		return (double val) -> {
			out.set(RangeUtil.map(val, inMin, inMax, outMin, outMax));
		};
	}

	public static ScalarOutput<Angle> mapToAngle(ScalarOutput<?> out, double outMin, double outMax) {
		return new ScalarOutput<Angle>() {
			@Override
			public void set(double val) {
				out.set(RangeUtil.map(val, 0, 360, outMin, outMax));
			}

		};
	}

	public static ScalarOutput<Percent> mapToPercent(ScalarOutput<?> out, double outMin, double outMax) {
		return (double val) -> {
			out.set(RangeUtil.map(val, -1, 1, outMin, outMax));
		};

	}

	public static <T extends Value> ScalarOutput<T> scale(ScalarOutput<T> out, double scale) {
		return (double val) -> {
			out.set(val / scale);
		};

	}

	public static <T extends Value> ScalarOutput<T> invert(ScalarOutput<T> out) {
		return (double val) -> {
			out.set(-val);
		};
	}

	public static ScalarOutput<Percent> applyDeadband(ScalarOutput<Percent> out, double deadband) {
		return (double val) -> {
			out.set(RangeUtil.applyDeadband(val, deadband));
		};
	}

	public static <T extends Value> ScalarOutput<T> limitRange(ScalarOutput<T> out, double limit) {
		return (double val) -> {
			out.set(RangeUtil.limit(val, limit));
		};
	}

}
