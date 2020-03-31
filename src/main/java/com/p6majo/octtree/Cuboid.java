package com.p6majo.octtree;

import com.p6majo.linalg.Vector3D;
import com.p6majo.logger.Logger;
import com.p6majo.logger.Logger.Level;

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

    private Vector3D low;
    private Vector3D high;
    private double averageSize;

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
    public Cuboid(Vector3D low, Vector3D high){

        //make sure, the low and high parameters are well defined
        if (high.getX()<low.getX()||high.getY()<low.getY()||high.getZ()<low.getZ())
            Logger.logging(Level.error,"Inconsistent data in "+this.getClass().getName()+" constructor.\n"+"Low: "+low+"\nHigh: "+high);

        this.high = high;
        this.low = low;

        this.averageSize = 1./3*(this.getWidth()+this.getHeight()+this.getLength());
    }

    /*
     ***********************
     ****       getter   ***
     ***********************
     */
    public Vector3D getLow(){
        return this.low;
    }

    public Vector3D getHigh(){
        return this.high;
    }

    public Vector3D getMiddle(){ return this.high.add(this.low).mul(0.5);}

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

    public double getAverageSize() {
        return this.averageSize;
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

    public Vector3D randomPosition(){
        return new Vector3D(low.getX()+Math.random()*getLength(),low.getY()+Math.random()*getWidth(),low.getZ()+Math.random()*getHeight());
    }


    /**
     * This method checks, whether a point is contained inside the Domain.
     * inklusiv nach links, exklusiv nach rechts, um Ueberlappung zu vermeiden (nicht sonderlich wichtig)
     *
     * @param point
     * @return True, if inside, false otherwise
     */
    public boolean contains(Vector3D point){
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
            return new Cuboid(new Vector3D(leftmax,frontmax,bottommax),new Vector3D(rightmin,backmin,topmin));
        else
            return null;
    }

    /**
     * Shift the cuboid by the given shift vector
     * @param shift
     * @return shifted cuboid
     */
    public Cuboid shift(Vector3D shift){
        return new Cuboid(low.add(shift),high.add(shift));
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
        return "cuboid: "+low.toString()+"-"+high.toString();
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
        return new Cuboid(new Vector3D(coords[0],coords[2],coords[4]),new Vector3D(coords[1],coords[3],coords[5]));
    }

}
