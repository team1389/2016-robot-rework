package com.team1389.watch;

import edu.wpi.first.wpilibj.tables.ITable;

public class StringInfo extends Info{
	/**
	 * Sets information (StringSource, Name) to class variables or super class
	 * @param name the name of the hardware the source is from
	 * @param source2 the input stream used to read from
	 */
	StringSource source;
	public StringInfo(String name, StringSource source2) {
		super(name);
		this.source=source2;
	}
	public interface StringSource{
		public String get();
	}
	@Override
	public void publish(ITable table) {
		table.putString(name, source.get());
	}
	@Override
	public String toString() {
		return name+": "+source.get();
	}
	@Override
	public double loggable() {
		return -1;
	}
}
