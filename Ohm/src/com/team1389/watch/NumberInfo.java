package com.team1389.watch;

import com.team1389.hardware.inputs.interfaces.ScalarInput;

import edu.wpi.first.wpilibj.tables.ITable;

public class NumberInfo extends Info {
	ScalarInput<?> source;

	public NumberInfo(String name, ScalarInput<?> source) {
		super(name);
		this.source = source;
	}

	public interface NumberSource {
		public double get();
	}

	@Override
	public void publish(ITable table) {
		table.putNumber(name, source.get());
	}

	@Override
	public String toString() {
		return name + ": " + source.get();
	}

	@Override
	public double loggable() {
		return source.get();
	}
}
