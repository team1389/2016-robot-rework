package com.team1389.watch.info;

import com.team1389.hardware.inputs.interfaces.ScalarInput;

import edu.wpi.first.wpilibj.tables.ITable;

public class NumberInfo extends SimpleWatchable {
	ScalarInput<?> source;

	public NumberInfo(String name, ScalarInput<?> source) {
		super(name);
		this.source = source;
	}

	public interface NumberSource {
		public double get();
	}

	@Override
	public void publishUnderName(String name, ITable table) {
		table.putNumber(name, source.get());
	}

	@Override
	public String getPrintString() {
		return name + ": " + source.get();
	}

	@Override
	public double getLoggable() {
		return source.get();
	}
}
