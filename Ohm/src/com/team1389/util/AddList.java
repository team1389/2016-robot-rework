package com.team1389.util;

import java.util.ArrayList;
import java.util.Arrays;

public class AddList<T> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public AddList<T> put(T... ts) {
		addAll(Arrays.asList(ts));
		return this;
	}
	public AddList<T> put(T t){
		add(t);
		return this;
	}

}
