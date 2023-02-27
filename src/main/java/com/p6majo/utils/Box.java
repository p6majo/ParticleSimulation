package com.p6majo.utils;


/**
 * A box is a rectangular domain of the complex plane
 * @author jmartin
 *
 */
public class Box {

	//default is unit box
	public double lowleftx = -1;
	public double lowlefty = -1;
	public double uprightx = 1;
	public double uprighty = 1;
	public double scalex = 1;
	public double scaley = 1;
	public double scale = 1;
	
	/**
	 * constructor for the unit box
	 */
	public Box() {
	}
	
	public Box(double scale) {
		this.scale = scale;
		this.scalex = scale;
		this.scaley = scale;
		this.lowleftx*=scale;
		this.lowlefty*=scale;
		this.uprightx*=scale;
		this.uprighty*=scale;
	}
	
	public Box(double scalex, double scaley) {
		this.scalex = scalex;
		this.scaley = scaley;
		this.scale = Math.max(Math.abs(scalex), Math.abs(scaley));
		this.lowleftx*=scalex;
		this.lowlefty*=scaley;
		this.uprightx*=scalex;
		this.uprighty*=scaley;
	}
	
	public Box(double lowleftx, double lowlefty, double uprightx, double uprighty) {
		this.lowleftx = lowleftx;
		this.lowlefty = lowlefty;
		this.uprightx = uprightx;
		this.uprighty = uprighty;
		this.scalex = uprightx-lowleftx;
		this.scaley = uprighty-lowlefty;
		this.scale = Math.sqrt(Math.abs(scalex*scaley));
	}
	
	
	public void rescale(double scale) {
		this.lowleftx*=scale;
		this.lowlefty*=scale;
		this.uprightx*=scale;
		this.uprighty*=scale;
	}

}

