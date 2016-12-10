package com.team1389.motion_profile;

public class Kinematics {
	public double Vo, V, t, a, S;

	public static void main(String[] args) {
		System.out.println(new Kinematics(0, 10, Double.NaN, Double.NaN, 5).solveSystem());
	}

	public Kinematics(double v0, double vf, double t, double a, double S) {
		this.a = a;
		this.Vo = v0;
		this.t = t;
		this.V = vf;
		this.S = S;
		solveSystem();
	}

	@Override
	public String toString() {
		return "Vo: " + Vo + " V: " + V + " a: " + a + " t: " + t + " S: " + S;
	}

	public Kinematics solveSystem() {
		int code = 0;
		code = Double.isNaN(S) ? code : code | 1;
		code = code << 1;
		code = Double.isNaN(t) ? code : code | 1;
		code = code << 1;
		code = Double.isNaN(a) ? code : code | 1;
		code = code << 1;
		code = Double.isNaN(V) ? code : code | 1;
		code = code << 1;
		code = Double.isNaN(Vo) ? code : code | 1;
		return solveSystem(code);
	}

	private Kinematics solveSystem(int code) {
		double tS = S;
		double tVo = Vo;
		double tV = V;
		double ta = a;
		double tt = t;
		// TODO check this one
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
				S = 0;
			} else {
				tt = (tV - tVo) / ta;
				t = tt;
				S = tVo * tt + ta * tt * tt / 2;
			}
		}
		if ((code & 11) == 11) {
			if (tt == 0) {
				a = 0;
				S = 0;
			} else {
				ta = (tV - tVo) / tt;
				a = ta;
				S = tVo * tt + ta * tt * tt / 2;
			}
		}
		if ((code & 14) == 14) {
			tVo = tV - ta * tt;
			Vo = tVo;
			S = tVo * tt + ta * tt * tt / 2;
		}
		if ((code & 26) == 26) {
			if (tt == 0) {
				Vo = tV;
				a = 0;
			} else {
				tVo = 2 * tS / tt - tV;
				Vo = tVo;
				a = (tV - tVo) / tt;
			}
		}
		// TODO this handles signs badly
		if ((code & 22) == 22) {
			if (ta == 0) {
				Vo = tV;
				t = S / Vo;
			} else {
				tVo = Math.sqrt(tV * tV - 2 * ta * tS);
				Vo = tVo;
				t = (tV - tVo) / ta;
			}
		}
		if ((code & 28) == 28) {
			if (tt == 0) {
				Vo = 0;
				V = 0;
			} else {
				tVo = (tS - ta * tt * tt / 2) / tt;
				Vo = tVo;
				V = tVo + ta * tt;
			}
		}
		if ((code & 13) == 13) {
			tV = tVo + ta * tt;
			V = tV;
			S = tVo * tt + ta * tt * tt / 2;
		}
		if ((code & 25) == 25) {
			if (tt == 0) {
				a = 0;
				V = tVo;
			} else {
				ta = 2 * (tS - tVo * tt) / (tt * tt);
				a = ta;
				V = tVo + ta * tt;
			}
		}
		if ((code & 21) == 21) {
			if (ta == 0) {
				V = tVo;
				t = S / V;
			} else {
				tV = Math.signum(a) * Math.sqrt(tVo * tVo + 2 * ta * tS);
				V = tV;
				t = (tV - tVo) / ta;
			}
		}
		return this;
	}

}