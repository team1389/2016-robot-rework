package com.team1389.control;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.value_types.PIDTunableValue;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class PIDRangeIn<T extends PIDTunableValue> implements PIDSource {
		RangeIn<T> input;

		public PIDRangeIn(RangeIn<T> input) {
			this.input = input;
		}

		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {

		}

		@Override
		public PIDSourceType getPIDSourceType() {
			try {
				return input.type.newInstance().getValueType();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return PIDSourceType.kDisplacement;
		}

		@Override
		public double pidGet() {
			return input.get();
		}
		public static <T extends PIDTunableValue> PIDRangeIn<T> get(RangeIn<T> in){
			return new PIDRangeIn<>(in);
		}

	}