package org.usfirst.frc.team1389.robot;

import com.team1389.hardware.watch.SendableWatchable;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;

public class Tester {
	private static NetworkTable table;

	public static void main(String[] args) {
		NetworkTable.setIPAddress("127.0.0.1");
		NetworkTable.setServerMode();
		table=NetworkTable.getTable("SmartDashboard");

		while (true) {
			putData("hi",new SendableWatchable());
		}

	}

	public static void putData(String key, Sendable data) {
		table.getSubTables().add(key);
		ITable dataTable = table.getSubTable(key);
		dataTable.putString("~TYPE~", data.getSmartDashboardType());
		data.initTable(dataTable);
	}
}
