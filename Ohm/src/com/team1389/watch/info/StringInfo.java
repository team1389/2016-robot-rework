package com.team1389.watch.info;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.tables.ITable;

/**
 * an info type that tracks the value of a string
 * 
 * @author amind
 *
 */
public class StringInfo extends SimpleWatchable {

	Supplier<String> source;

	/**
	 * @param name the name of this info
	 * @param source the string source to track
	 */
	public StringInfo(String name, Supplier<String> source) {
		super(name);
		this.source = source;
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
