package com.team1389.hardware.inputs.software;

import java.util.Arrays;
import java.util.stream.Stream;

import com.team1389.hardware.inputs.interfaces.BooleanSupplier;
import com.team1389.watch.info.BooleanInfo;

public class DigitalIn {
	private BooleanSupplier input;

	public DigitalIn(BooleanSupplier input) {
		this.input = input;
	}

	public DigitalIn(BooleanSupplier in, InputType type) {
		this(in);
		getSpecial(type);
	}

	public BooleanInfo getInfo(String name) {
		return new BooleanInfo(name, this.input);
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
		this.input = BooleanSupplier.getLatched(input);
		return this;
	}

	public DigitalIn getToggled() {
		this.input = BooleanSupplier.getToggled(input);
		return this;
	}

	public DigitalIn combineAND(DigitalIn... toCombine) {
		BooleanSupplier[] inps = new BooleanSupplier[toCombine.length + 1];
		for (int i = 0; i < inps.length; i++) {
			inps[i] = toCombine[i].input;
		}
		inps[inps.length - 1] = this.input;
		this.input = BooleanSupplier.combineAND(inps);
		return this;
	}

	public DigitalIn combineOR(DigitalIn... toCombine) {
		Stream<BooleanSupplier> inps=Arrays.stream(toCombine).map(it->it.input);
		this.input = BooleanSupplier.combineOR(Stream.concat(inps, Stream.of(input)).toArray(BooleanSupplier[]::new));
		return this;
	}

	public DigitalIn invert() {
		this.input = BooleanSupplier.invert(input);
		return this;
	}

	public boolean get() {
		return input.get();
	}
}
