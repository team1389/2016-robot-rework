package org.usfirst.frc.team1389.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.team1389.hardware.inputs.software.DigitalInput;

@SuppressWarnings("rawtypes")
public class ButtonEnumMap<E extends Enum> {
	List<ButtonEnum> mappings;

	public ButtonEnumMap(E defaultValue) {
		currentVal = defaultValue;
		this.mappings = new ArrayList<ButtonEnum>();
	}

	@SafeVarargs
	public final void setMappings(ButtonEnum... mappings) {
		this.mappings.addAll(Arrays.asList(mappings));
	}

	E currentVal;

	public E getVal() {
		for (ButtonEnum mapping : mappings) {
			if (mapping.button.get()) {
				currentVal = mapping.val;
			}
		}
		return currentVal;
	}

	public class ButtonEnum {
		DigitalInput button;
		E val;

		public ButtonEnum(DigitalInput button, E val) {
			this.button = button;
			this.val = val;
		}
	}
}
