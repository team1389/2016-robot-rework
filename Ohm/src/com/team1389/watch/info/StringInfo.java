package com.team1389.watch.info;

import edu.wpi.first.wpilibj.tables.ITable;

public class StringInfo extends SimpleWatchable {
	/**
	 * Sets information (StringSource, Name) to class variables or super class
	 * 
	 * @param name the name of the hardware the source is from
	 * @param source2 the input stream used to read from
	 */
	StringSource source;

	public StringInfo(String name, StringSource source2) {
		super(name);
		this.source = source2;
	}

	public interface StringSource {
		public String get();
	}

	@Override
	public void publishUnderName(String name, ITable table) {
		table.putString(name, source.get());
	}

	@Override
	public String getPrintString() {
		return name + ": " + source.get();
	}

	@Override
	public double getLoggable() {
		return -1;
	}
}
