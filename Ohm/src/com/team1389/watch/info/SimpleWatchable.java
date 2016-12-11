package com.team1389.watch.info;

import com.team1389.watch.Watchable;

public abstract class SimpleWatchable implements Watchable {
	protected String name;

	public SimpleWatchable(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
