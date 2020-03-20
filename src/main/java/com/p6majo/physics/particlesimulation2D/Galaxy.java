package com.p6majo.physics.particlesimulation2D;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Galaxy {

    /*
     **********************
     ***   attributes   ***
     **********************
     */

    private Vector pos;
    private Vector vel;
    private double radius;
    private Color color;
    private int centralMass;
    private List<Particle> particles;
    private Random rnd;

    /*
     *********************
     ***  constructors ***
     *********************
     */

    public Galaxy(Vector pos, Vector velocity, double radius, int numberOfStars, int centralMass, Color color,int orientation){
        this.pos = pos;
        this.vel = velocity;
        this.radius = radius;
        this.color = color;
       this.centralMass = centralMass;
       this.rnd = new Random();


       this.particles = new ArrayList<>();
        for (int i = 0; i < numberOfStars; i++) {
            this.particles.add(createNewParticle());
        }

        //central object
        this.particles.add(new Particle(pos.getX(),pos.getY(),centralMass,false, color));

        //calculate velocities

        Rect boundary = new Rect(pos.getX(),pos.getY(),radius,radius);

        QuadTree qt = new QuadTree(null,boundary, 1,null,0);
        for(int i = 0; i< particles.size(); i++)
            qt.insert(particles.get(i));

        //Calculate the mass distribution through the quad tree
        qt.ComputeMassDistribution();

        //setup Geschwindigkeit : v^2 = GM/r

        for (Particle particle : particles) {
            particle.setVel(qt.getMass(),qt.getCenterOfMass(),orientation);
            particle.addVel(this.vel);
        }

    }

    /*
     ***********************
     ****       getter   ***
     ***********************
     */
    public Vector getPos() {
        return pos;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public Vector getVel() {
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
    private Particle createNewParticle(){
            double r = rnd.nextDouble()*this.radius;
            double phi =  rnd.nextDouble()*2.*Math.PI;
            int mass = 1+rnd.nextInt(5);
            return new Particle(this.pos.getX()+ r*Math.cos(phi),this.pos.getY()+r*Math.sin(phi),mass,false,color);
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
        return "galaxy at "+this.pos.toString()+
                " with velocity "+this.vel.toString()+
                " central mass: m="+this.centralMass+
                " and "+this.particles.size()+" stars.";
    }



}
