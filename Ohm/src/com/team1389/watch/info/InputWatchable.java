package com.team1389.watch.info;

import java.util.function.Consumer;

import edu.wpi.first.wpilibj.tables.ITable;

public class InputWatchable<T> extends SimpleWatchable {
	boolean init;
	Consumer<T> onChange;

	public InputWatchable(String name, double defaultVal, Consumer<T> onChange) {
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
				onChange.accept((T) val);
			}, true);
		} else {
			val = table.getNumber(name, val);
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
}
