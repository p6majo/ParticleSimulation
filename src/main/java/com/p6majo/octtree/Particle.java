package com.p6majo.octtree;

import com.p6majo.linalg.Vector;
import com.p6majo.linalg.Vector;
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
    private Vector position;
    private Vector velocity;
    private Vector acceleration;
    private double mass;
    private double radius;
    private final boolean fixed;
    private boolean approaching;


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
    public Particle(Vector position){
        this(position,Vector.getZero(3),Math.random()*10);
    }


    public Particle(Vector position,Vector velocity){
        this(position,velocity,Math.random()*10);
    }


    public Particle(Vector position,Vector velocity,double mass){
        this(position,velocity,mass,false);
    }

    public Particle(Vector position,Vector velocity,double mass, boolean fixed) {
        this(position,velocity,mass,Math.pow(mass,1./3),fixed);
    }


        /**
         * Create a particle at the provided position and mass
         *
         * @param position
         * @param mass
         */
    public Particle(Vector position,Vector velocity,double mass,double radius, boolean fixed){
        this.position = position;
        this.velocity = velocity;
        this.acceleration = Vector.getZero(3);
        this.mass = mass;
        this.fixed=fixed;
        this.radius = radius;Math.pow(mass,1./3);
    }


    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public boolean isFixed() {
        return fixed;
    }
    public Vector getVelocity(){return this.velocity;}

    @Override
    public Vector getPosition() { return this.position; }
    @Override
    public double getMass() { return this.mass; }
    
    public Vector getMomentum(){return this.velocity.mul(this.mass);}
    
    public double getRadius(){return this.radius;}
    
    public double getv2(){return this.velocity.dot(this.velocity);}
    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */

    public void setAcceleration(Vector acceleration){
        this.acceleration = acceleration;
    }
    public void setPosition(Vector position){this.position = position;}
    public void setVelocity(Vector velocity){this.velocity = velocity;}
    /**
     * the momentum can be changed dynamically via interaction with other particles
     * @return
     */
    public void setMomentum(Vector p){
        this.velocity = p.mul(1./this.mass);
    }
    public void setApproaching(boolean approaching) { this.approaching = approaching; }

    /*
     ***********************************************
     ***           Public methods       ************
     ***********************************************
     */

     public void update(double dt){
            if (!fixed) {
                this.velocity = this.velocity.add(this.acceleration.mul(dt));
                this.position = this.position.add(this.velocity.mul(dt));
            }
    }

    /**
     * Perform a Galileian transformation on the momentum of the particle. The position of the particle remains unchanged <br>
     *     <b>p</b><sub>new</sub>=m (<b>v</b>-<b>u</b>)
     * @param u the velocity parameter of the Galileian transformation
     */
    public void galileiTransform(Vector u){
        this.setVelocity(this.getVelocity().sub(u));
    }

    /**
     * Rotate the momentum by a given angle and axis of rotation.
     * <br>
     *     <br>
     *         The following calculation is applied:<br>
     *             p<sub>new</sub> =p<sub>old</sub>cos(&phi;)+(a&#10799;p<sub>old</sub>)sin(&phi;)
     * @param angle
     * @param axis
     */
    public void rotateMomentum3d(double angle, Vector axis){
        Vector pNew = this.getMomentum().mul(Math.cos(angle)).add(axis.toVector3D().cross(this.getMomentum().toVector3D()).mul(Math.sin(angle)));
        this.setMomentum(pNew);
    }

    /**
     * Perform a simple two dimensional rotation of the momentum.
     * The sign of the angle determines, whether the rotation is
     * performed in clockwise or counterclockwise orientation
     * @param angle
     */
    public void rotateMomentum2d(double angle){
        Vector p = this.getMomentum();
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        Vector pNew = new Vector(p.getValue(0)*c-p.getValue(1)*s,p.getValue(0)*s+p.getValue(1)*c);
        this.setMomentum(pNew);
    }

    /*
     ***********************************************
     ***           Private methods      ************
     ***********************************************
     */

    private Vector randomPosition(Vector low, Vector high){
        double x = Utils.map(Math.random(),0,1,low.getX(),high.getX());
        double y = Utils.map(Math.random(),0,1,low.getY(),high.getY());
        double z = Utils.map(Math.random(),0,1,low.getZ(),high.getZ());
        return new Vector(x,y,z);
    }

    /*
     ***********************************************
     ***           Overrides            ************
     ***********************************************
     */

    
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
        return new Particle(range.randomPosition(),Vector.getZero(3),mass);
    }


}
