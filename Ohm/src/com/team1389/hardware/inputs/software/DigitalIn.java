package com.team1389.hardware.inputs.software;

import com.team1389.hardware.inputs.interfaces.BinaryInput;
import com.team1389.watch.BooleanInfo;

public class DigitalIn {
	private BinaryInput input;

	public DigitalIn(BinaryInput input) {
		this.input = input;
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
		BinaryInput[] inps = new BinaryInput[toCombine.length + 1];
		for (int i = 0; i < inps.length; i++) {
			inps[i] = toCombine[i].input;
		}
		inps[inps.length - 1] = this.input;
		this.input = BinaryInput.combineOR(inps);
		return this;
	}

	public DigitalIn invert() {
		this.input = BinaryInput.invert(input);
		return this;
	}
}
