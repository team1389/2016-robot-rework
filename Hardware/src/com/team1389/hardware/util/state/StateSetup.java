package com.team1389.hardware.util.state;

@FunctionalInterface
public interface StateSetup {
	void setup();
	default void end() { }
}
