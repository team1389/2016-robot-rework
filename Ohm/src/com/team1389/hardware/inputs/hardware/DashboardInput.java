package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.inputs.interfaces.ScalarInput;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;



public class DashboardInput implements ScalarInput{
public ITable table;
private boolean changed;
NetworkTable netTable = NetworkTable.getTable("SmartDashboard");



//get something to listen for changes in smartdashboard, and then change the value based on that
	@Override
	public double get() {
		netTable.addTableListener((ITable source,String key, Object value, boolean isNew)->{ valueChanged(source,  key, value, isNew);}, changed);
		if
		return 0.0;
	}
 
}
