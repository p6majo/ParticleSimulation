package com.p6majo.models;

import com.p6majo.particlesimulation.Galaxy;
import com.p6majo.particlesimulation.Vector;

import java.awt.*;
import java.util.function.Function;

/**
 * A container to keep all the parameters and initial data of the simulation
 * @author p6majo
 * @version 2020=03-11
 *
 */
public class ModelWithGalaxy extends Model{

    public double G = 1.;
    public double theta = 2.; //acccuracy parameter for the Barnes-Hutt algorithm
    public double dt = 0.1;
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


    /*
     *********************
     ***  constructors ***
     *********************
     */

    public ModelWithGalaxy(int width, int height,double G,double theta, double dt){
        super(width,height,G ,theta,dt);



        Galaxy galaxy = new Galaxy(new Vector(width/2,height/2),new Vector(0,0),height/2,1000,1000, Color.CYAN,-1);
        this.particles.addAll(galaxy.getParticles());

    }

    /*
     ***********************
     ****       getter   ***
     ***********************
     */


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
        String out = "Galaxy model \n";
        out+="Number of particles: "+this.particles.size()+"\n";
        if (this.qt!=null) {
            out += "Total mass: " +qt.getMass()+"\n";
            out+= "centered at: "+qt.getCenterOfMass().toString()+"\n";
        }
        return out;
    }

}

