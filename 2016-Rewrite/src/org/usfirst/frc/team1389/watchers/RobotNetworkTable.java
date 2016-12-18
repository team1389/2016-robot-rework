package org.usfirst.frc.team1389.watchers;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class RobotNetworkTable{
	private NetworkTable table;
	private static RobotNetworkTable nInstance = new RobotNetworkTable();
	public static RobotNetworkTable getInstance(){
	return nInstance;
	}
	public void init(String key, double inputVal){//make an interface for all the things with input, pass them in for the putNumberArray?
		while(true){
				table.putNumber(key, inputVal);
		}
	}
}
