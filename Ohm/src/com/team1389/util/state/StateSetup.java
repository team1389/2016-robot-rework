package com.team1389.util.state;

@FunctionalInterface
public interface StateSetup {
	void setup();
	default void end() { }
}
