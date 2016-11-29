package com.team1389.hardware.inputs.interfaces;

import com.team1389.hardware.util.LatchedBoolean;

public interface BooleanSource {
	public boolean get();

	public static ListeningBooleanSource getListeningSource(BooleanSource in, Runnable runner) {
		return new ListeningBooleanSource(in, runner);
	}
}
