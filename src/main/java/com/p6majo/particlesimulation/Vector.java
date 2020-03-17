package com.p6majo.particlesimulation;


/**
 * @author AlexK
 * @version 2020-01-18
 */
public class Vector {

    public static Vector ZERO = new Vector(0.,0.);

    /*
     **********************
     ***   attributes   ***
     **********************
     */

    private double x;
    private double y;
    
    /*
     *********************
     ***  constructors ***
     *********************
     */

    public Vector(double x, double y){
        this.x = x;
        this.y = y;
    }

    //p6majo: bug entfernt, da stand this.y=v.x;
    public Vector(Vector v){
        this.x =v.x;
        this.y =v.y;
    }
    public Vector(){
        this.x =0;
        this.y =0;
    }
    public Vector(Vector v, Vector w){
        this.x = v.x-w.x;
        this.y = v.y-w.y;
    }

    /*
     ***********************
     ****       getter   ***
     ***********************
     */
    public double getX(){return this.x;}
    public double getY(){return this.y;}
    /*
     ************************
     ****       setter    ***
     ************************
     */
    
    /*
     *****************************
     ***     public methods    ***
     *****************************
     */

    //p6majo: meine Operationen mit Rueckgabewert

    public Vector minus(Vector v){
        return new Vector(this.x-v.x,this.y-v.y);
    }

    public Vector mal(double r){
        return new Vector(this.x*r,this.y*r);
    }

    public Vector plus(Vector v){
        return new Vector(this.x+v.x,this.y+v.y);
    }

    //p6majo: Ende neue Operationen

    public void add(Vector v){
        this.x += v.x;
        this.y += v.y;
    }
    public void sub(Vector v){
        this.x -= v.x;
        this.y -= v.y;
    }



    private void normalize(){
        double a = Math.sqrt(x*x+y*y);
        this.x /= a;
        this.y /= a;
    }
    public void mult(double n){
        this.x *= n;
        this.y *= n;
    }
    public void setMag(double mag){
        normalize();
        mult(mag);
    }

    /**
     * Berechnet die Laenge des Vektors
     * @return
     */
    public double mag(){
        return Math.sqrt(x*x + y*y);
    }

    /**
     * Berechnet die Laenge quadriert
     * @return
     */
    public double magSq(){
        return (x*x + y*y);
    }

    /**
     * Rotiert um einen Winkel theta
     * @param theta
     */
    public void rotate(float theta){
        double x = Math.cos(theta)*this.x-Math.sin(theta)*this.y;
        double y= Math.cos(theta)*this.y+Math.sin(theta)*this.x;

        this.x = x;
        this.y = y;
    }

    public void set(double x,double y){
        this.x = x;
        this.y = y;
    }
    /*
     ******************************
     ****     private methods   ***
     ******************************
     */


    /*
     ******************************
     ****     overrides         ***
     ******************************
     */

    /*
     ******************************
     ****     toString()        ***
     ******************************
     */

    @Override
    public String toString() {
        return "<"+x+"|"+y+">";
    }

}
