package com.team1389.hardware.inputs.hardware;

import com.team1389.hardware.inputs.interfaces.ScalarInput;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class DashboardInput implements ScalarInput{
	public ITable table;
	final double originalVal = 0.0; 
	NetworkTable netTable = NetworkTable.getTable("SmartDashboard");
	Object value;
	boolean changed;
	public class tempTableListener implements ITableListener {
		


		@Override
		public void valueChanged(ITable arg0, String arg1, Object arg2, boolean arg3) {
			if(arg3){
				value = arg2; 
			}
			
			
			
		}
	}
//get something to listen for changes in smartdashboard, and then change the value based on that
	@Override
	public double get() {
		tempTableListener listener = new tempTableListener();
		listener.valueChanged(table, "SmartDashboard", value, changed);
		return (double)value;
		//make a class in here that impliments , changed);
	}
}




