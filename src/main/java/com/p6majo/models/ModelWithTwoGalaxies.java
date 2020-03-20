package com.p6majo.models;

import com.p6majo.physics.particlesimulation2D.Galaxy;
import com.p6majo.physics.particlesimulation2D.Vector;

import java.awt.*;
import java.util.function.Function;

/**
 * A container to keep all the parameters and initial data of the simulation
 * @author p6majo
 * @version 2020=03-11
 *
 */
public class ModelWithTwoGalaxies extends Model{

    public double G = 1.;
    public double theta = 0.5; //acccuracy parameter for the Barnes-Hutt algorithm
    public double dt = 0.01;

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

    public ModelWithTwoGalaxies(int width, int height,double G,double theta,double dt){
        super(width,height,G,theta,dt);



        Galaxy galaxy1 = new Galaxy(new Vector(width/4,height/3),new Vector(-100,0),height/4,5000,1000, Color.CYAN,-1);
        Galaxy galaxy2 = new Galaxy(new Vector(3*width/4,2*height/3),new Vector(100,0),height/4,5000,1000, Color.ORANGE,1);
        this.particles.addAll(galaxy1.getParticles());
        this.particles.addAll(galaxy2.getParticles());

        System.out.println(galaxy1.toString());
        System.out.println(galaxy2.toString());

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

