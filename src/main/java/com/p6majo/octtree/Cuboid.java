package com.p6majo.octtree;

import com.p6majo.logger.Logger;
import com.p6majo.logger.Logger.Level;
import com.p6majo.particlesimulation.Circle;

/**
 * The class Cuboid is a representative of a subspace of the total space
 *
 *
 * @author p6majo
 * @version 2020-03-16
 *  */
public class Cuboid {



    /*
     **********************
     ***   attributes   ***
     **********************
     */

    private Point3D low;
    private Point3D high;

    /*
     *********************
     ***  constructors ***
     *********************
     */

    /**
     * Constructor for a cuboid
     * @param low lower, left, front corner
     * @param high upper, right, back corner
     */
    public Cuboid(Point3D low, Point3D high){

        //make sure, the low and high parameters are well defined
        if (high.getX()<low.getX()||high.getY()<low.getY()||high.getZ()<low.getZ())
            Logger.logging(Level.error,"Inconsistent data in "+this.getClass().getName()+" constructor.\n"+"Low: "+low+"\nHigh: "+high);

        this.high = high;
        this.low = low;
    }

    /*
     ***********************
     ****       getter   ***
     ***********************
     */
    public Point3D getLow(){
        return this.low;
    }

    public Point3D getHigh(){
        return this.high;
    }

    public double getLength(){
        return this.high.getX()-this.low.getX();
    }

    public double getWidth(){
        return this.high.getY()-this.low.getY();
    }

    public double getHeight(){
        return this.high.getZ()-this.low.getZ();
    }

    public double getBottom(){
        return low.getZ();
    }

    public double getTop(){
        return high.getZ();
    }

    public double getLeft(){
        return low.getX();
    }

    public double getRight(){
        return high.getX();
    }

    public double getFront(){
        return low.getY();
    }

    public double getBack(){
        return high.getY();
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

    /**
     * This method checks, whether a point is contained inside the Domain.
     *
     * inklusiv nach links, exklusiv nach rechts, um Ueberlappung zu vermeiden (nicht sonderlich wichtig)
     *
     * @param point
     * @return True, if inside, false otherwise
     */
    public boolean contains(Point3D point){
        return(point.getX() >= this.low.getX() &&
                point.getX() < this.high.getX() &&
                point.getY() >= this.low.getY() &&
                point.getY() < this.high.getY() &&
                point.getZ() >= this.low.getZ() &&
                point.getZ() < this.high.getZ()
        );
    }

    /**
     * Check, whether the domain other, has overlap with this domain.
     *
     * @param other cuboid to intersect this with
     * @return true, if overlap exists and false otherwise
     */
    public boolean intersects(Cuboid other){
        double leftmax = Math.max(this.getLeft(),other.getLeft());
        double frontmax = Math.max(this.getFront(),other.getFront());
        double bottommax = Math.max(this.getBottom(),other.getBottom());
        double rightmin = Math.min(this.getRight(),other.getRight());
        double topmin = Math.min(this.getTop(),other.getTop());
        double backmin = Math.min(this.getBack(),other.getBack());

        return (leftmax<rightmin && frontmax<backmin && bottommax<topmin);
    }

    /**
     * Returns the intersection Cuboid of this with another cuboid or null, if there is no intersection
     * @param other Cuboid tot intersect with
     * @return Cuboid of intersection or null
     */
    public Cuboid getIntersection(Cuboid other){
        double leftmax = Math.max(this.getLeft(),other.getLeft());
        double frontmax = Math.max(this.getFront(),other.getFront());
        double bottommax = Math.max(this.getBottom(),other.getBottom());
        double rightmin = Math.min(this.getRight(),other.getRight());
        double topmin = Math.min(this.getTop(),other.getTop());
        double backmin = Math.min(this.getBack(),other.getBack());

        if  (leftmax<rightmin && frontmax<backmin && bottommax<topmin)
            return new Cuboid(new Point3D(leftmax,frontmax,bottommax),new Point3D(rightmin,backmin,topmin));
        else
            return null;
    }


    /*
     ******************************
     ****     private methods   ***
     ******************************
     */

    private static void swap(double[] array, int one, int two){
        double tmp = array[two];
        array[two]=array[one];
        array[one]=tmp;
    }


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
        return "Cuboid: "+low.toString()+"-"+high.toString();
    }

    /*
     ******************************
     ****     static methods    ***
     ******************************
     */

    public static Cuboid random(double maxX, double maxY,double maxZ){
        double[] coords = new double[6];
        for (int i = 0; i < 3; i++) {
            coords[2*i] = Math.random() * maxX;
            coords[2*i+1] = Math.random() * maxX;
            if (coords[2*i] > coords[2*i+1]) swap(coords, 2*i, 2*i+1);
        }
        return new Cuboid(new Point3D(coords[0],coords[2],coords[4]),new Point3D(coords[1],coords[3],coords[5]));
    }
}
