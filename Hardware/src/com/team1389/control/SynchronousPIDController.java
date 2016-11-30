package com.team1389.control;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class SynchronousPIDController extends SynchronousPID {
	private PIDOutput output;
	private PIDSource source;
	public SynchronousPIDController(double kP,double kI,double kD,PIDSource source,PIDOutput output){
		super(kP,kI,kD);
		this.source=source;
		this.output=output;
	}
	public void update(){
		output.pidWrite(calculate(source.pidGet()));
	}
}
