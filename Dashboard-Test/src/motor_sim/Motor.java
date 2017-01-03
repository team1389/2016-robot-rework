package motor_sim;

public class Motor {
	public static final Motor CIM = new Motor(2.41, 5330);
	public static final Motor m775_PRO = new Motor(.71, 18730);
	public static final Motor MINI_CIM = new Motor(1.4, 5840);
	public static final Motor BAG_MOTOR = new Motor(.4, 13180);

	public final double stallTorque;
	public final double freeSpeed;
	private double voltage;

	public Motor(double stallTorque, double freeSpeedRPM) {
		this.stallTorque = stallTorque;
		this.freeSpeed = freeSpeedRPM * 2 * Math.PI / 60;
	}

	/**
	 * Calculate the torque contributed from the motor.
	 * 
	 * @param omega current angular velocity
	 * @return torque from motor
	 */
	public double getTorque(double omega) {
		return stallTorque * (voltage - (omega / freeSpeed));
	}

	public void setVoltage(double voltage) {
		this.voltage = 12*voltage;
	}
}