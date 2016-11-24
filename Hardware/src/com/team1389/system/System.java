package com.team1389.system;

import com.team1389.hardware.watch.Watchable;

public interface System extends Watchable{
	public void update();
	public void init();
}
