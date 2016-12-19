package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.inputs.interfaces.ScalarInput;
import com.team1389.hardware.value_types.Value;

import edu.wpi.first.wpilibj.tables.ITable;

public class DashboardScalarInput extends DashboardInput<Double> implements ScalarInput<Value> {

	public DashboardScalarInput(String key, ITable table, Double defaultVal) {
		super(key, table, defaultVal);
		if (!table.containsKey(key)) {
			table.putNumber(key, defaultVal);
		} else {
			value = table.getNumber(key, defaultVal);
		}
	}

}
