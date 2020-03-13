package com.p6majo.octtree;

/**
 * @author p6majo
 * @version 1.0
 * @date 2019-08-26
 */
public class Point3D {


    /**********************/
    /***   attributes   ***/
    /**********************/
    private double x;
    private double y;
    private double z;

    /*********************/
    /***  constructors ***/
    /*********************/

    public Point3D(double x,double y,double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /************************/
    /****       getter    ***/
    /************************/

    public double getX(){ return x; }
    public double getY(){ return y; }
    public double getZ(){ return z; }

    /************************/
    /****       setter    ***/
    /************************/


    /******************************/
    /****     public methods    ***/
    /******************************/

    public double getDistance(Point3D point){
        return Math.sqrt(directionTo(point).getNorm2());
    }

    public Point3D directionTo(Point3D point){
        return new Point3D(point.x-x,point.y-y,point.z-z);
    }

    public double getNorm2(){
        return this.x*this.x+this.y*this.y+this.z*this.z;
    }

    public static Point3D midPoint(Point3D one, Point3D two){
        return new Point3D((one.x+two.x)/2,(one.y+two.y)/2,(one.z+two.z)/2);
    }

    public Point3D shift(double x, double y, double z){
        return new Point3D(this.x+x,this.y+y,this.z+z);
    }

    public Point3D shift (Point3D shift){ return new Point3D(this.x+shift.x,this.y+shift.y,this.z+shift.z); }

    /******************************/
    /****     private methods   ***/
    /******************************/


    /******************************/
    /****     overrides         ***/
    /******************************/

    /******************************/
    /****     toString()        ***/
    /******************************/

    @Override
    public String toString() {
        return "("+String.format("%.2f",x)+"|"+String.format("%.2f",y)+"|"+String.format("%.2f",z)+")";
    }

}
