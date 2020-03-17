package com.p6majo.octtree;

import com.p6majo.utils.Utils;

/**
 * The class Particle
 *
 * @author p6majo
 * @version 2019-08-26
 */
public class Particle implements ObjectIn3DSpace {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */
    private Vector3D position;
    private double mass;


    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    /**
     * Create a particle at the provided position with a random mass between 0 .. 10
     *
     * @param position
     */
    public Particle(Vector3D position){
        this(position,Math.random()*10);
    }

    /**
     * Create a particle at the provided position and mass
     *
     * @param position
     * @param mass
     */
    public Particle(Vector3D position,double mass){
        this.position = position;
        this.mass = mass;
    }


    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */



    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */



    /*
     ***********************************************
     ***           Public methods       ************
     ***********************************************
     */

    public void changePosition(Vector3D newPosition){
        this.position = newPosition;
    }

    /*
     ***********************************************
     ***           Private methods      ************
     ***********************************************
     */

    private Vector3D randomPosition(Vector3D low, Vector3D high){
        double x = Utils.map(Math.random(),0,1,low.getX(),high.getX());
        double y = Utils.map(Math.random(),0,1,low.getY(),high.getY());
        double z = Utils.map(Math.random(),0,1,low.getZ(),high.getZ());
        return new Vector3D(x,y,z);
    }

    /*
     ***********************************************
     ***           Overrides            ************
     ***********************************************
     */

    @Override
    public Vector3D getPosition() {
        return this.position;
    }

    @Override
    public double getMass() { return this.mass; }
    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    @Override
    public String toString() {
        return "P"+position.toString();
    }


    /**
     * Return a Particle at a random position inside a given cuboid with a random mass between 0 .. 10
     * @param range
     * @return
     */
    public static Particle random(Cuboid range){
        return new Particle(range.randomPosition());
    }

    /**
     * Return a Particle at a random position inside a given cuboid with a given mass
     * @param range
     * @return
     */
    public static Particle random(Cuboid range,double mass){
        return new Particle(range.randomPosition(),mass);
    }

}
