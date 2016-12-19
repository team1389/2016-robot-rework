package com.team1389.watch.info;

import edu.wpi.first.wpilibj.tables.ITable;

public class InputWatchable<T> extends SimpleWatchable {
	boolean init;
	ChangeHandler<T> onChange;

	public InputWatchable(String name, double defaultVal, ChangeHandler<T> onChange) {
		super(name);
		this.onChange = onChange;
		this.val = defaultVal;
	}

	double val;

	public double get() {
		return val;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void publishUnderName(String name, ITable table) {
		if (!table.containsKey(name)) {
			table.putNumber(name, val);
			table.addTableListener(name, (ITable t, String s, Object val, boolean changed) -> {
				onChange.changed((T) val);
			}, true);
		}
	}

	@Override
	public String getPrintString() {
		return name + ": " + val;
	}

	@Override
	public double getLoggable() {
		return val;
	}

	public interface ChangeHandler<T> {
		public void changed(T val);
	}
}
