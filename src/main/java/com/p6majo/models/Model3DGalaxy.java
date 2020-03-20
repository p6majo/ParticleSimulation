package com.p6majo.models;

import com.p6majo.octtree.Cuboid;
import com.p6majo.octtree.Particle;
import com.p6majo.octtree.Vector3D;

/**
 * A container to keep all the parameters and initial data of the simulation
 * @author p6majo
 * @version 2020=03-11
 *
 */
public class Model3DGalaxy extends Model3D{

    public int particleNumber = 100;


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
    public Model3DGalaxy(double G, double theta, double dt){
        super(G,theta,dt);

        double centralMass =10000;
        Vector3D low = this.container.getLow();
        Vector3D high = this.container.getHigh();

        Vector3D mid = this.container.getMiddle();
        this.particles.add(new Particle(mid,Vector3D.getZERO(),centralMass,true));

        for (int i = 1; i < particleNumber; i++) {


            double r = Math.sqrt(Math.random()) * 500 + 1000; //get more stars further out
            double phi = Math.random() * 2. * Math.PI;
            double z = Math.random()*50;

            Vector3D pos = new Vector3D(r * Math.cos(phi), r * Math.sin(phi), z);
            Vector3D omega = new Vector3D(0, 0, 1);
            Vector3D v = omega.cross(pos);
            v = v.mul(Math.sqrt(G * centralMass / r / v.getNorm2()));

            particles.add(new Particle(pos,v,Math.random()*3));
        }

        System.out.println(this.toString());


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
        Vector3D low = new Vector3D(-5000,-5000,-5000);
        Vector3D high = new Vector3D(5000,5000,5000);
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

