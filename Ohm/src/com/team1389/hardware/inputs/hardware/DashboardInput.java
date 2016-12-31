package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.inputs.interfaces.Input;

import edu.wpi.first.wpilibj.tables.ITable;

public class DashboardInput<T> implements Input<T> {
	protected T value;

	@SuppressWarnings("unchecked")
	public DashboardInput(String key, ITable table, T defaultVal) {
		value = defaultVal;
		table.addTableListener((ITable changeTable, String changeKey, Object val, boolean changed) -> {
			if (changeKey.equals(key)) {
				this.value = (T) val;
			}
		});
	}

	@Override
	public T get() {
		return value;

	}

}
