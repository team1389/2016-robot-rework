package com.team1389.hardware.watch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Watcher {
	List<Watchable> watchables;
	
	public Watcher() {
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
	public void display(){
		for(Watchable info:watchables){
			info.display();
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
