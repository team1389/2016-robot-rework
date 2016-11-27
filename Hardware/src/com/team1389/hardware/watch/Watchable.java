package com.team1389.hardware.watch;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;

public interface Watchable {

	public String getName();
	public Info[] getInfo();
	public default void publish(ITable table){
		for(Info e:getInfo()){
			e.publish(table);
		}
	}
	public default String getPrintString(){
		String s="";
		for(Info e:getInfo()){
			s=String.join(s,e.toString()+" ");
		}
		return s;
	}
	public default String loggable(){
		String s="";
		for(Info e:getInfo()){
			s=String.join(s,e.loggable()+"\t");
		}
		return s;
	}
}
