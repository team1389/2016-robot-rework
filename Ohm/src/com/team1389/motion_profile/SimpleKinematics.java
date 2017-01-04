package com.team1389.motion_profile;

/**
 * This class represents a solved kinematics system with constant aeration. It is final, in that once it is constructed
 * each of the system's parameters will remain constant. To get any of the parameters one can access any of the final class
 * variables.
 *
 */
public class SimpleKinematics {
		
	/**
	 * The initial velocity of the system
	 */
	public final double vo;
	
	/**
	 * The final velocity of the system
	 */
	public final double vf;
	
	/**
	 * The t the motion takes
	 */
	public final double t;
	
	/**
	 * The constant aeration of the system
	 */
	public final double a;
	
	/**
	 * The distance traveled in the system
	 */
	public final double x;



	/**
	 * Creates and solves the kinematics system. At least three of the variable must be real, but the rest may be Double.NaN if they are unknown.
	 * No check is made to see if the motion is possible as described, and so invalid input may result in negative t or other
	 * unexpected results.
	 * @param v0 The initial velocity of the system
	 * @param vf The final velocity of the system
	 * @param t The total t the motion takes
	 * @param a The constant aeration of the system
	 * @param dx The distance traveled in the system
	 */
	public SimpleKinematics(double v0, double vf, double t, double a, double dx) {
		
		solveSystem(a, v0, t, vf, dx);
		
		this.vo = v0;
		this.vf = vf;
		this.t = t;
		this.a = a;
		this.x = dx;
	}

	@Override
	public String toString() {
		return "Vo: " + vo + " Vf: " +  vf + " a: " + a + " t: " + t + " x: " + x;
	}

	private void solveSystem(double a, double v0, double t, double vf, double dx) {
		int code = 0;
		code = Double.isNaN(dx) ? code : code | 1;
		code = code << 1;
		code = Double.isNaN(t) ? code : code | 1;
		code = code << 1;
		code = Double.isNaN(a) ? code : code | 1;
		code = code << 1;
		code = Double.isNaN(vf) ? code : code | 1;
		code = code << 1;
		code = Double.isNaN(v0) ? code : code | 1;
		solveSystem(code, a, v0, t, vf, dx);
	}

	private void solveSystem(int code, double a, double v0, double t, double vf, double dx) {
		double tS = dx;
		double tVo = v0;
		double tV = vf;
		double ta = a;
		double tt = t;

		if ((code & 19) == 19) {
			if (tt == 0) {
				t = 0;
				a = 0;
			} else {
				tt = 2 * tS / (tVo + tV);
				t = tt;
				a = (tV - tVo) / tt;
			}
		}
		if ((code & 7) == 7) {
			if (ta == 0) {
				t = 0;
				dx = 0;
			} else {
				tt = (tV - tVo) / ta;
				t = tt;
				dx = tVo * tt + ta * tt * tt / 2;
			}
		}
		if ((code & 11) == 11) {
			if (tt == 0) {
				a = 0;
				dx = 0;
			} else {
				ta = (tV - tVo) / tt;
				a = ta;
				dx = tVo * tt + ta * tt * tt / 2;
			}
		}
		if ((code & 14) == 14) {
			tVo = tV - ta * tt;
			v0 = tVo;
			dx = tVo * tt + ta * tt * tt / 2;
		}
		if ((code & 26) == 26) {
			if (tt == 0) {
				v0 = tV;
				a = 0;
			} else {
				tVo = 2 * tS / tt - tV;
				v0 = tVo;
				a = (tV - tVo) / tt;
			}
		}
		if ((code & 22) == 22) {
			if (ta == 0) {
				v0 = tV;
				t = dx / v0;
			} else {
				tt = -tV + Math.signum(tS)*Math.sqrt(tV * tV - 2 * ta * tS) / -a;
				v0 = tV - ta * tt;
				t = tt;
			}
		}
		if ((code & 28) == 28) {
			if (tt == 0) {
				v0 = 0;
				vf = 0;
			} else {
				tVo = (tS - ta * tt * tt / 2) / tt;
				v0 = tVo;
				vf = tVo + ta * tt;
			}
		}
		if ((code & 13) == 13) {
			tV = tVo + ta * tt;
			vf = tV;
			dx = tVo * tt + ta * tt * tt / 2;
		}
		if ((code & 25) == 25) {
			if (tt == 0) {
				a = 0;
				vf = tVo;
			} else {
				ta = 2 * (tS - tVo * tt) / (tt * tt);
				a = ta;
				vf = tVo + ta * tt;
			}
		}
		if ((code & 21) == 21) {
			if (ta == 0) {
				vf = tVo;
				t = dx / vf;
			} else {
				tV = Math.signum(a) * Math.sqrt(tVo * tVo + 2 * ta * tS);
				vf = tV;
				t = (tV - tVo) / ta;
			}
		}
	}
	

}
