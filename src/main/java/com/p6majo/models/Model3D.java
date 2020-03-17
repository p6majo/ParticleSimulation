package com.p6majo.models;

import com.p6majo.octtree.Cuboid;
import com.p6majo.octtree.Octtree;
import com.p6majo.particlesimulation.Particle;
import com.p6majo.particlesimulation.QuadTree;

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

    public Function<Double,Double> gravityLaw =new Function<Double,Double>(){
        @Override
        public Double apply(Double distance) {
            return 1./distance/distance;
        }
    };
    /*
     **********************
     ***   attributes   ***
     **********************
     */

    protected Cuboid container;

    protected final Random rnd;
    protected final double G;
    protected final double theta ; //acccuracy parameter for the Barnes-Hutt algorithm
    protected ArrayList<Particle> particles;
    public final double dt;


    protected Octtree ot; //this octtree is needed to calculate the mass distribution, it is invoked without model

    /*
     *********************
     ***  constructors ***
     *********************
     */

    /**
     * Base class for the definition of a model
     * @param container the size of the universe
     * @param G the constant of gravity
     * @param theta the approximation parameter for the Barnes-Hutt-algorithm
     * @param dt
     */
    public Model3D(Cuboid container, double G, double theta, double dt){
        this.container = container;
        this.G = G;
        this.theta = theta;
        this.dt = dt;

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

    public double acceleration(int mass, double r){
        return mass*gravityLaw.apply(r);
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
        String out = "Basic Model for a particle gravity simulation\n";
        out+="Number of particles: "+this.particles.size()+"\n";
        if (this.ot!=null) {
            out += "Total mass: " +ot.getMass()+"\n";
            out+= "centered at: "+ot.getCenterOfMass().toString()+"\n";
        }
        return out;
    }

}

