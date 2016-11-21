package com.team1389.hardware.watch;

import java.util.ArrayList;
import java.util.List;

public class Watcher {
	List<Watchable> watchables;
	
	public Watcher() {
		watchables = new ArrayList<>();
	}
	
	public Watchable newWatchable(Watchable watchable){
		watchables.add(watchable);
		return watchable;
	}
	
	public List<Watchable> getWatchables(){
		return watchables;
	}
}
