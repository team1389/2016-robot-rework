package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.inputs.interfaces.BinaryInput;

import edu.wpi.first.wpilibj.tables.ITable;

public class DashboardBinaryInput extends DashboardInput<Boolean> implements BinaryInput {

	public DashboardBinaryInput(String key, ITable table, Boolean defaultVal) {
		super(key, table, defaultVal);
		if (!table.containsKey(key)) {
			table.putBoolean(key, defaultVal);
		}
	}

}