package com.team1389.hardware.inputs.interfaces;

import java.util.function.Supplier;

public interface Input<T> extends Supplier<T>{
	@Override
	public T get();
}
