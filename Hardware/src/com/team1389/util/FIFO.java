package com.team1389.util;

import java.util.LinkedList;

@SuppressWarnings("serial")
public class FIFO<Type> extends LinkedList<Type>{
	int queSize;
	public FIFO(int maxSize){
		this.queSize=maxSize;
	}
	public void push(Type val){
		if(size()>=queSize){
			remove();
		}
		add(val);
	}
	public static double average(FIFO<Double> val){
		return val.stream().mapToDouble(a->a).average().getAsDouble();
	}
	public void setQueSize(int queSize){
		this.queSize=queSize;
	}

}
