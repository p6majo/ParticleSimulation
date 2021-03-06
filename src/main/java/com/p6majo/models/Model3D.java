package com.p6majo.models;

import com.p6majo.octtree.Cuboid;
import com.p6majo.octtree.Octtree;
import com.p6majo.octtree.Particle;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * A container to keep all the parameters and initial data of the simulation
 * @author p6majo
 * @version 2020=03-11
 *
 */
public abstract class Model3D {

    public int particleNumber = 10000;
    public Function<Double,Double> gravityLaw = distance -> 1./distance/distance;
    /*
     **********************
     ***   attributes   ***
     **********************
     */

    protected Cuboid container;

    protected final Random rnd;
    public final double G;
    protected final double theta ; //acccuracy parameter for the Barnes-Hutt algorithm
    protected ArrayList<Particle> particles;
    public final double dt;

    public final double rmin = 0.001; //UV cutoff for gravity, this should be replaced later by collisions, once objects touch each other


    protected Octtree ot; //this octtree is needed to calculate the mass distribution, it is invoked without model

    /*
     *********************
     ***  constructors ***
     *********************
     */

    /**
     * Base class for the definition of a model
     * @param G the constant of gravity
     * @param theta the approximation parameter for the Barnes-Hutt-algorithm
     * @param dt
     */
    public Model3D( double G, double theta, double dt){

        this.G = G;
        this.theta = theta;
        this.dt = dt;

        this.container = this.defineContainer();

        rnd = new Random();
        particles = new ArrayList<>();

    }

    /*
     ***********************
     ****       getter   ***
     ***********************
     */

    public Cuboid getContainer(){ return this.container; }
    public double getTheta(){
        return this.theta;
    }
    public List<Particle> getParticles(){
        return this.particles;
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

    public double acceleration(double mass, double r){
        return G*mass*gravityLaw.apply(r);
    }


    /**
     * This method is called by the constructor of the Model3D
     * to set the container of the simulation. Particles that fly outside of the container are lost.
     * Make sure, that the container is large enough to hold all the interesting parts of the simulation.
     *
     * @return the container of the simulation
     */
    public abstract Cuboid defineContainer();
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
        String out = "Basic Model for a particle gravity simulation\n";
        out+="Number of particles: "+this.particles.size()+"\n";
        if (this.ot!=null) {
            out += "Total mass: " +ot.getMass()+"\n";
            out+= "centered at: "+ot.getCenterOfMass().toString()+"\n";
        }
        return out;
    }

}

