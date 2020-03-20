package com.p6majo.models;

import com.p6majo.physics.particlesimulation2D.Particle;
import com.p6majo.physics.particlesimulation2D.QuadTree;

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
public abstract class Model {

    public int particleNumber = 10000;

    public Function<Double,Double> gravityLaw =new Function<Double,Double>(){
        @Override
        public Double apply(Double distance) {
            return 1./distance;
        }
    };
    /*
     **********************
     ***   attributes   ***
     **********************
     */

    protected final int width;
    protected final int height;
    protected final Random rnd;
    protected final ArrayList<Particle> particles;
    protected  final double G ;
    protected final double theta; //acccuracy parameter for the Barnes-Hutt algorithm
    public final double dt;


    protected QuadTree qt ;


    /*
     *********************
     ***  constructors ***
     *********************
     */

    public Model(int width,int height, double G, double theta, double dt){
        this.width=width;
        this.height = height;
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

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

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
        if (this.qt!=null) {
            out += "Total mass: " +qt.getMass()+"\n";
            out+= "centered at: "+qt.getCenterOfMass().toString()+"\n";
        }
        return out;
    }

}

