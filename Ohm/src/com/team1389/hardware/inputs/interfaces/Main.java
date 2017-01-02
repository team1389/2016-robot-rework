package com.team1389.hardware.inputs.interfaces;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.value_types.Position;

public class Main {
	public static void main(String[] args) {
		RangeIn<Position> pos = new RangeIn<Position>(Position.class, () -> 2048d, 0, 8192);
		pos.mapToRange(0,360).mapToRange(0, 8192).scale(10).setRange(0, 5).mapToRange(0, 10);
		System.out.println(pos);
	}
}
