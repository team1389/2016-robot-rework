package com.team1389.hardware.inputs.interfaces;

import java.util.function.BooleanSupplier;

public class BooleanSupplierUtil{

	public static BooleanSupplier invert(BooleanSupplier in) {
		return () -> {
			return !in.getAsBoolean();
		};
	}

	public static LatchedBooleanSupplier getLatched(BooleanSupplier in) {
		return new LatchedBooleanSupplier(in);
	}

	public static ToggledBooleanSupplier getToggled(BooleanSupplier in) {
		LatchedBooleanSupplier input;
		if (in instanceof LatchedBooleanSupplier) {
			input = (LatchedBooleanSupplier) in;
		} else {
			input = getLatched(in);
		}
		return new ToggledBooleanSupplier(input);
	}

	public static BooleanSupplier combineAND(BooleanSupplier... inputs) {
		return () -> {
			boolean stillTrue = true;
			for (BooleanSupplier input : inputs) {
				stillTrue = stillTrue && input.getAsBoolean();
			}
			return stillTrue;
		};
	}

	public static BooleanSupplier combineOR(BooleanSupplier... inputs) {
		return () -> {
			boolean stillTrue = false;
			for (BooleanSupplier input : inputs) {
				stillTrue = stillTrue || input.getAsBoolean();
			}
			return stillTrue;
		};
	}

	public static ListeningBooleanSupplier getListeningSource(BooleanSupplier in, Runnable runner) {
		return new ListeningBooleanSupplier(in, runner);
	}
}
