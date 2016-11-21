package com.team1389.hardware.control;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class InvertPIDSource implements PIDSource{
	
	final PIDSource source;
	
	public InvertPIDSource(PIDSource pidSource) {
		this.source = pidSource;
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		source.setPIDSourceType(pidSource);
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return source.getPIDSourceType();
	}

	@Override
	public double pidGet() {
		return -source.pidGet();
	}

}
