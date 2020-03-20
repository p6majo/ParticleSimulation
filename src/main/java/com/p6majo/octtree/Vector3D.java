package com.p6majo.octtree;

/**
 * @author p6majo
 * @version 1.0
 * @date 2019-08-26
 */
public class Vector3D {


    /**********************/
    /***   attributes   ***/
    /**********************/
    private double x;
    private double y;
    private double z;

    /*********************/
    /***  constructors ***/
    /*********************/

    public Vector3D(double x, double y, double z){
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

    public double getDistance(Vector3D point){
        return Math.sqrt(directionTo(point).getNorm2());
    }

    public Vector3D directionTo(Vector3D point){
        return new Vector3D(point.x-x,point.y-y,point.z-z);
    }

    public double getNorm2(){
        return this.x*this.x+this.y*this.y+this.z*this.z;
    }

    public static Vector3D midPoint(Vector3D one, Vector3D two){
        return new Vector3D((one.x+two.x)/2,(one.y+two.y)/2,(one.z+two.z)/2);
    }

    public Vector3D add(double x, double y, double z){
        return new Vector3D(this.x+x,this.y+y,this.z+z);
    }

    public Vector3D add(Vector3D shift){ return new Vector3D(this.x+shift.x,this.y+shift.y,this.z+shift.z); }

    public Vector3D sub(Vector3D shift){ return this.add(shift.mul(-1)); }

    public Vector3D mul(double scale){ return new Vector3D(this.x*scale,this.y*scale,this.z*scale); }

    public Vector3D cross(Vector3D factor) {return new Vector3D(
            this.getY()*factor.getZ()-this.getZ()*factor.getY(),
            this.getZ()*factor.getX()-this.getX()*factor.getZ(),
            this.getX()*factor.getY()-this.getY()*factor.getX());}


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


    /******************************/
    /****     statics           ***/
    /******************************/

    public static Vector3D getZERO() {
        return new Vector3D(0, 0, 0);
    }

    public static Vector3D random(double maxX, double maxY, double maxZ){
        return new Vector3D(Math.random()*maxX,Math.random()*maxY,Math.random()*maxZ);
    }
}
