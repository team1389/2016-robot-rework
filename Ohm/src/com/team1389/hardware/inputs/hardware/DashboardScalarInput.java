package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.inputs.interfaces.ScalarInput;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class DashboardScalarInput implements ScalarInput{
double value;
	
	public DashboardScalarInput(String key, ITable table, double defaultVal){
		value = defaultVal;
		table.addTableListener((ITable changeTable, String changeKey, Object val, boolean changed)->{
			if(changeKey.equals(key)){
				this.value=(double)val;
			}
		});
	}
	public DashboardScalarInput(String key, ITable table){
		this(key,table,0.0);
	}
	
	@Override
	public double get() {
		return value;
		
	}
}




