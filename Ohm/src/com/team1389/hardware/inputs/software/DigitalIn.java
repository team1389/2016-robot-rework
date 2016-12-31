package com.team1389.hardware.inputs.software;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.team1389.hardware.inputs.interfaces.BinaryInput;
import com.team1389.watch.info.BooleanInfo;

public class DigitalIn {
	private BinaryInput input;

	public DigitalIn(Supplier<Boolean> input) {
		this.input = input::get;
	}

	public DigitalIn(BinaryInput in, InputType type) {
		this(in);
		getSpecial(type);
	}

	public BooleanInfo getWatchable(String name) {
		return new BooleanInfo(name, input::get);
	}

	public DigitalIn getSpecial(InputType type) {
		switch (type) {
		case LATCHED:
			return getLatched();
		case TOGGLED:
			return getToggled();
		default:
			return this;
		}
	}

	public enum InputType {
		LATCHED, TOGGLED;
	}

	public DigitalIn getLatched() {
		this.input = BinaryInput.getLatched(input);
		return this;
	}

	public DigitalIn getToggled() {
		this.input = BinaryInput.getToggled(input);
		return this;
	}

	public DigitalIn combineAND(DigitalIn... toCombine) {
		BinaryInput[] inps = new BinaryInput[toCombine.length + 1];
		for (int i = 0; i < inps.length; i++) {
			inps[i] = toCombine[i].input;
		}
		inps[inps.length - 1] = this.input;
		this.input = BinaryInput.combineAND(inps);
		return this;
	}

	public DigitalIn combineOR(DigitalIn... toCombine) {
		Stream<BinaryInput> inps = Arrays.stream(toCombine).map(it -> it.input);
		this.input = BinaryInput.combineOR(Stream.concat(inps, Stream.of(input)).toArray(BinaryInput[]::new));
		return this;
	}

	public DigitalIn invert() {
		this.input = BinaryInput.invert(input);
		return this;
	}

	public boolean get() {
		return input.get();
	}
}
