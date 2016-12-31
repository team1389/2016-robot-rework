package com.team1389.watch.info;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.tables.ITable;

public class FlagInfo extends BooleanInfo {
	public FlagInfo(String name, BooleanSupplier isFlag) {
		super(name, isFlag);
	}

	@Override
	public void publishUnderName(String name, ITable table) {
		if (in.getAsBoolean()) {
			super.publishUnderName(name, table);
		} else {
			table.delete(name);
		}
	}

	@Override
	public String getPrintString() {
		return in.getAsBoolean() ? super.getPrintString() : "";
	}

}
