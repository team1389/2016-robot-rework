package com.team1389.hardware.inputs.software;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ButtonEnumMap<E extends Enum> {
	List<ButtonEnum> mappings;
	List<Runnable> changes;
	public void addChangeListener(Runnable onChange) {
		changes.add(onChange);
	}

	public ButtonEnumMap(E defaultValue) {
		currentVal = defaultValue;
		changes=new ArrayList<>();
		this.mappings = new ArrayList<>();
	}

	@SafeVarargs
	public final void setMappings(ButtonEnum... mappings) {
		this.mappings.addAll(Arrays.asList(mappings));
	}

	E currentVal;
	E lastVal;
	public E getVal() {
		Iterator<ButtonEnum> i = mappings.iterator();
		while(i.hasNext()){
			ButtonEnum mapping=i.next();
			if (mapping.button.get()) {
				currentVal = mapping.val;
			}
		}
		if(lastVal!=currentVal){
			for(Runnable r:changes){
				r.run();
			}
		}
		lastVal=currentVal;
		return currentVal;
	}
	public E getCurrentVal(){
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
