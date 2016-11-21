package com.team1389.hardware.registry;

import com.team1389.hardware.watch.Watchable;

/**
 * Represents the constructor for hardware, is passed to the registry
 * @author jacob
 *
 * @param <Prerequisite>
 * @param <Result>
 */
public interface Constructor<Prerequisite, Result extends Watchable>{
	public Result construct(Prerequisite preq);
}
