package com.team1389.watch;

import edu.wpi.first.wpilibj.tables.ITable;

public class StringInfo extends Info{
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
