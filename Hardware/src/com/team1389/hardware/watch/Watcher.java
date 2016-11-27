package com.team1389.hardware.watch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;


public class Watcher {
	public static ITable DASHBOARD;

	List<Watchable> watchables;
	
	public Watcher() {
		DASHBOARD=NetworkTable.getTable("SmartDashboard");
		watchables = new ArrayList<>();
	}
	
	public Watcher watch(Watchable... watchables){
		this.watchables.addAll(Arrays.asList(watchables));
		return this;
	}
	public Watcher watch(List<Watchable> watchables){
		this.watchables.addAll(watchables);
		return this;
	}
	public List<Watchable> getWatchables(){
		return watchables;
	}
	public void publish(ITable table){
		for(Watchable info:watchables){
			info.publish(table);
		}
	}
	public String getPrintString(){
		String s="";
		for(Watchable w:watchables){
			s=String.join(s,w.getPrintString()+"\n");
		}
		return s;
	}
}
