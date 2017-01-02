package com.team1389.watch.info;

import java.util.HashMap;
import java.util.Map;

import com.team1389.util.Optional;
import com.team1389.watch.Watchable;

/**
 * an abstract class forcing the ability to return a name(key)
 * 
 * @author Kenneth
 *
 */
public abstract class SimpleWatchable implements Watchable {
	protected String name;

	public SimpleWatchable(String name) {
		this.name = name;
	}

	public abstract double getLoggable();

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Map<String, SimpleWatchable> getFlat(Optional<String> parent) {
		Map<String, SimpleWatchable> map = new HashMap<>();
		map.put(parent.ifPresent(getName(), this::getFullName).get(), this);
		return map;
	}
}
