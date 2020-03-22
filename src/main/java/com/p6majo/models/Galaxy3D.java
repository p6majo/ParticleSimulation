package com.p6majo.models;


import com.p6majo.octtree.Particle;
import com.p6majo.octtree.Vector3D;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Galaxy3D {

    /*
     **********************
     ***   attributes   ***
     **********************
     */

    private Vector3D pos;
    private Vector3D vel;
    private Vector3D omega;
    private double radius;
    private int numberOfStars;
    private Color color;
    private double centralMass;
    private List<Particle> particles;
    private Random rnd;


    /*
     *********************
     ***  constructors ***
     *********************
     */

    /**
     * Construct a galaxy with a central mass (the mass of the stars is negligible). The extension of the galaxy is
     * given by the parameter radius. Moreover is the galaxy specified by its position and its peculiar velocity.
     * The orientation of the galaxy in space is provided by the axial vector omega.
     * @param G the gravitational constant is needed to set the galaxy with the correct angular velocity
     * @param centralMass mass of the central black hole
     * @param maxStarMass maximal mass of a single star
     * @param numberOfStars number of orbiting stars
     * @param radius radius of the galaxy
     * @param pos position of the galaxy
     * @param velocity peculiar velocity of the galaxy, if null the galaxy is fixed
     * @param omega axial vector of rotation (the magnitude is arbitrary)
     * @param color a color
     */
    public Galaxy3D(double G,double centralMass, double maxStarMass, int numberOfStars, double radius, Vector3D pos, Vector3D velocity, Vector3D omega, Color color){
        this.pos = pos;
        this.vel = velocity;
        this.radius = radius;
        this.color = color;
        this.centralMass = centralMass;
        this.rnd = new Random();

        this.particles = new ArrayList<>();

        //central star
        if (vel!=null)
            particles.add(new Particle(pos,vel,centralMass));
        else
            particles.add(new Particle(pos,Vector3D.getZERO(),centralMass,true)); //fixed center

        for (int i = 1; i < numberOfStars; i++) {

            double coreRadius = Math.pow(centralMass,1./3);

            double r = Math.sqrt(Math.random()) * (radius-coreRadius) + coreRadius; //get more stars further out
            double phi = Math.random() * 2. * Math.PI;
            double z = Math.random()*50;

            Vector3D position = new Vector3D(r * Math.cos(phi), r * Math.sin(phi), z);
            Vector3D v = omega.cross(pos);
            v = v.mul(Math.sqrt(G * centralMass / r / v.getNorm2())); //division by v^2 to normalize v initially

            particles.add(new Particle(pos.add(position),v.add(vel),Math.random()*maxStarMass));
        }

    }

    /*
     ***********************
     ****       getter   ***
     ***********************
     */
    public Vector3D getPos() {
        return pos;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public Vector3D getVel() {
        return vel;
    }

    public double getRadius() {
        return radius;
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
        String out = "galaxy at "+this.pos.toString()+"\n";
        if (vel==null)
            out+=" with velocity "+this.vel.toString()+"\n";
        else
            out+=" fixed in space\n";
        out+=" central mass: m="+this.centralMass;
        out+=" and "+this.particles.size()+" stars.";
        return out;
    }



}
