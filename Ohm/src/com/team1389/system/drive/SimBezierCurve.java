package com.team1389.system.drive;

public class SimBezierCurve {

	public static final SimBezierCurve LINEAR;
	
	public static final SimBezierCurve EASE;
	
	public static final SimBezierCurve EASE_IN;
	
	public static final SimBezierCurve EASE_IN_OUT;
	
	public static final SimBezierCurve EASE_OUT;
	
	static {
		final SimPoint zero = new SimPoint(0,0);
		final SimPoint one = new SimPoint(1,1);
		
		final SimPoint a = new SimPoint(0.25,0.1);
		final SimPoint b = new SimPoint(0.25,1);
		final SimPoint c = new SimPoint(0.42,0);
		final SimPoint d = new SimPoint(0.58,1);
		
		LINEAR = new SimBezierCurve(zero,one);
		EASE = new SimBezierCurve(zero,a,b,one);
		EASE_IN = new SimBezierCurve(zero,c,one,one);
		EASE_IN_OUT = new SimBezierCurve(zero,c,d,one);
		EASE_OUT = new SimBezierCurve(zero,zero,d,one);
	}
	
	private final SimPoint[] p;
	
	public SimBezierCurve(SimPoint... p){
		if(p.length <2)
			throw new IllegalArgumentException("Minimun number of interpolation points is 2, you have: " + p.length);
			
		this.p=p;
		
	}
		
	public SimPoint getPoint(double t){
		return SimBezierCurve.getPoint(t,this.p);
	}
	
	public static SimPoint getPoint(double t,SimPoint...p){
		final double x[] = new double[p.length];
		final double y[] = new double[p.length];
		
		for(int i=0; i < p.length; i++){
			x[i] = p[i].getX();
			y[i] = p[i].getY();
		}
		
		x[0] = getPoint(t,x);
		y[0] = getPoint(t,y);
		
		return new SimPoint(x[0],y[0]);
	}
		
		
	public static double getPoint(double t, double... p){
		double oldT = t;
		if(p.length ==2)
			return p[0] + (p[1] - p[0])*t;
		
		if(0 > t || t > 1)
			t = -t;
		if(p.length <2)
			throw new IllegalArgumentException("Minimum number of interpolation points is 2, you have: " + p.length);
		
		final double[] d = new double[p.length];
		for (int i =0; i < d.length; i++)
			d[i] = p[i];
		
		for(int i = d.length; i > 0; i--)
			for (int j = 1; j < i; j++)
				d[j-1] = getPoint(t,d[j-1], d[j]);
		
		if(oldT < 0){
			return -d[0];
		}else{
			return d[0];
		}
		
	}
	
}
