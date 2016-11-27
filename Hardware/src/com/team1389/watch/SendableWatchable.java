package com.team1389.watch;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.tables.ITable;

public class SendableWatchable implements Watchable,Sendable{
	ITable table;
	@Override
	public void initTable(ITable subtable) {
		table=subtable;
        table.putString("~TYPE~", getSmartDashboardType());
		this.publish(subtable);
	}

	@Override
	public ITable getTable() {
		return table;
	}

	@Override
	public String getSmartDashboardType() {
		return "Drive System";
	}

	@Override
	public String getName() {
		return "driveSystem";
	}

	@Override
	public Info[] getInfo() {
		return new Info[]{
				new NumberInfo("leftWheels",()->{return -1.0;}),
				new NumberInfo("rightWheels",()->{return 1.0;}),
				new BooleanInfo("isQuickTurn",()->{return false;})
		};
	}

}
