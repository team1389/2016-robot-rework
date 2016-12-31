package com.team1389.motion_profile;

public class SimpleKinematics {
	public double Vo, V, t, a, X;

	public SimpleKinematics(double v0, double vf, double t, double a, double S) {
		this.a = a;
		this.Vo = v0;
		this.t = t;
		this.V = vf;
		this.X = S;
		solveSystem();
	}

	@Override
	public String toString() {
		return "Vo: " + Vo + " Vf: " + V + " a: " + a + " t: " + t + " S: " + X;
	}

	public SimpleKinematics solveSystem() {
		int code = 0;
		code = Double.isNaN(X) ? code : code | 1;
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

	private SimpleKinematics solveSystem(int code) {
		double tS = X;
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
				X = 0;
			} else {
				tt = (tV - tVo) / ta;
				//System.out.println((tV - tVo) + " ::: " + ta);
				t = tt;
				X = tVo * tt + ta * tt * tt / 2;
			}
		}
		if ((code & 11) == 11) {
			if (tt == 0) {
				a = 0;
				X = 0;
			} else {
				ta = (tV - tVo) / tt;
				a = ta;
				X = tVo * tt + ta * tt * tt / 2;
			}
		}
		if ((code & 14) == 14) {
			tVo = tV - ta * tt;
			Vo = tVo;
			X = tVo * tt + ta * tt * tt / 2;
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
		if ((code & 22) == 22) {
			if (ta == 0) {
				Vo = tV;
				t = X / Vo;
			} else {
				tt = -tV + Math.signum(tS)*Math.sqrt(tV * tV - 2 * ta * tS) / -a;
				Vo = tV - ta * tt;
				t = tt;
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
			X = tVo * tt + ta * tt * tt / 2;
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
				t = X / V;
			} else {
				tV = Math.signum(a) * Math.sqrt(tVo * tVo + 2 * ta * tS);
				V = tV;
				t = (tV - tVo) / ta;
			}
		}
		/*if(tV != Double.NaN && tVo != Double.NaN){
			if(Math.signum(ta) != Math.signum(tV - tVo)){
				a *= -1;
			}
			if(Math.signum(tS) != Math.signum(tV + tVo)){
				tS *= -1;
			}
		}*/
		
		
		return this;
	}
	

}
