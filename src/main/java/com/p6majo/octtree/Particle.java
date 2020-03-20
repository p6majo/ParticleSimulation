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
    private Vector3D velocity;
    private Vector3D acceleration;
    private double mass;
    private final boolean fixed;


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
        this(position,Vector3D.getZERO(),Math.random()*10);
    }


    public Particle(Vector3D position,Vector3D velocity){
        this(position,velocity,Math.random()*10);
    }


    public Particle(Vector3D position,Vector3D velocity,double mass){
        this(position,velocity,mass,false);
    }
    /**
     * Create a particle at the provided position and mass
     *
     * @param position
     * @param mass
     */
    public Particle(Vector3D position,Vector3D velocity,double mass,boolean fixed){
        this.position = position;
        this.velocity = velocity;
        this.acceleration = Vector3D.getZERO();
        this.mass = mass;
        this.fixed=fixed;
    }


    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public boolean isFixed() {
        return fixed;
    }

    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */

    public void setAcceleration(Vector3D acceleration){
        this.acceleration = acceleration;
    }

    /*
     ***********************************************
     ***           Public methods       ************
     ***********************************************
     */

    public void changePosition(Vector3D newPosition){
        this.position = newPosition;
    }

    public void update(double dt){
            if (!fixed) {
                this.velocity = this.velocity.add(this.acceleration.mul(dt));
                this.position = this.position.add(this.velocity.mul(dt));
            }
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
        return new Particle(range.randomPosition(),Vector3D.getZERO(),mass);
    }


}
