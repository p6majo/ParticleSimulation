package com.p6majo.models;

import com.p6majo.octtree.Cuboid;
import com.p6majo.octtree.Particle;
import com.p6majo.octtree.Vector3D;

import java.awt.*;

/**
 * A container to keep all the parameters and initial data of the simulation
 * @author p6majo
 * @version 2020=03-11
 *
 */
public class Model3DTwoGalaxies extends Model3D{


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

    /**
     * Base class for the definition of a model
     * <b> don't forget to set the size of the universe in the method defineContainer()</b>
     * @param G the constant of gravity
     * @param theta the approximation parameter for the Barnes-Hutt-algorithm
     * @param dt
     */
    public Model3DTwoGalaxies(double G, double theta, double dt){
        super(G,theta,dt);

        Vector3D pos = new Vector3D(-8000,-8000,-8000);
        Vector3D vel = new Vector3D(100,100,10);
        Vector3D omega = new Vector3D(0,0,1);
        Galaxy3D galaxy1 = new Galaxy3D(G,100000,10,100,1000,pos,vel,omega, Color.WHITE);

        Vector3D pos2 = new Vector3D(8000,8000,8000);
        Vector3D vel2 = new Vector3D(-100,-100,-10);
        Vector3D omega2 = new Vector3D(0,1,0);
        Galaxy3D galaxy2 = new Galaxy3D(G,100000,10,200,1000,pos2,vel2,omega2, Color.GREEN);

        super.particles.addAll(galaxy1.getParticles());
        super.particles.addAll(galaxy2.getParticles());
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

    public double acceleration(double mass, double r){
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

    /**
     * This method is called by the constructor of the Model3D
     * to set the container of the simulation. Particles that fly outside of the container are lost.
     * Make sure, that the container is large enough to hold all the interesting parts of the simulation.
     *
     * @return the container of the simulation
     */
    @Override
    public Cuboid defineContainer() {
        Vector3D low = new Vector3D(-10000,-10000,-10000);
        Vector3D high = new Vector3D(10000,10000,10000);
        return new Cuboid(low,high);
    }

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

