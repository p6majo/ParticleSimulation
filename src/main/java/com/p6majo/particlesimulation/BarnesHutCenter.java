package com.p6majo.particlesimulation;


public class BarnesHutCenter {

    /*
     **********************
     ***   attributes   ***
     **********************
     */

    private double x;
    private double y;
    private int mass;

    /*
     *********************
     ***  constructors ***
     *********************
     */

    public BarnesHutCenter(double x,double y, int mass){
        this.x = x;
        this.y = y;
       this.mass =mass;
    }

    /*
     ***********************
     ****       getter   ***
     ***********************
     */
    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public int getMass(){
        return this.mass;
    }



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
        return "("+this.x+"|"+this.y+"): m="+this.mass;
    }


}
