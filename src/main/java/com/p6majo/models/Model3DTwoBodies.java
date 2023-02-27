package com.p6majo.models;

import com.p6majo.linalg.Vector;
import com.p6majo.linalg.Vector3D;
import com.p6majo.octtree.Cuboid;
import com.p6majo.octtree.Particle;

/**
 * A container to keep all the parameters and initial data of the simulation
 * @author p6majo
 * @version 2020=03-11
 *
 */
public class Model3DTwoBodies extends Model3D{

    public int particleNumber = 2;


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
     * @param G the constant of gravity
     * @param theta the approximation parameter for the Barnes-Hutt-algorithm
     * @param dt
     */
    public Model3DTwoBodies(double G, double theta, double dt){
       super(G,theta,dt);

        Vector low = this.container.getLow();
        Vector high = this.container.getHigh();

        Vector diag = high.sub(low);
        Vector pos1 = low.add(diag.mul(2./5));
        Vector pos2 = low.add(diag.mul(3./5));
        Vector v1 = new Vector3D(110,0,0);
        Vector v2 = new Vector3D(-110,0,0);
       this.particles.add(new Particle(pos1,v1,500.));
       this.particles.add(new Particle(pos2,v2,500.));



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
        Vector low = new Vector3D(-500,-500,-500);
        Vector high = new Vector3D(500,500,500);
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

