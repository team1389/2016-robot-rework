package com.team1389.hardware.watch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Watcher {
	List<Watchable> watchables;
	
	public Watcher() {
		watchables = new ArrayList<>();
	}
	
	public Watcher watch(Watchable... watchables){
		this.watchables.addAll(Arrays.asList(watchables));
		return this;
	}
	public List<Watchable> getWatchables(){
		return watchables;
	}
	public void display(){
		for(Watchable info:watchables){
			SmartDashboard.putString("Watchable: ", info.getName());
			for(Entry<String,String> e:info.getInfo().entrySet()){
				SmartDashboard.putString(e.getKey(), e.getValue());
			}
		}
	}
}
