package com.p6majo.gravityengines;

import com.p6majo.models.Model3D;
import com.p6majo.octtree.Cuboid;
import com.p6majo.octtree.Octtree;
import com.p6majo.octtree.Particle;


import java.util.List;

/**
 * The physics engine that contains the dynamics of the system
 *
 *
 * @author p6majo
 * @version 2020-03-11
 *
 */
public class GravityEngine3D {

    /*
     **********************
     ***   attributes   ***
     **********************
     */

    private Model3D model;
    private Octtree<Particle> octtree;
    private Cuboid boundary;

    private List<Particle> particles ;
     private double time;
    private int count;


    /*
     *********************
     ***  constructors ***
     *********************
     */

    public GravityEngine3D(Model3D model){
        this.model = model;
        this.particles = model.getParticles();
        this.time = 0;
        this.boundary = model.getContainer();
       // testing();
    }
    /*
     ***********************
     ****       getter   ***
     ***********************
     */


    public List<Particle> getParticles() {
        return particles;
    }
    public double getTime(){return time;}


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


    public void timestep(){

        double dt = model.dt;
        time +=dt;
        //strukturiere Teilchen im Octtree
         this.octtree = new Octtree(model,1,boundary);
        for(int i = 0; i< particles.size(); i++)
            octtree.addObject(this.particles.get(i));

        //Calculate the mass distribution through the quad tree
        this.octtree.computeMassDistribution();


        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).setAcceleration(octtree.calculateAcceleration(particles.get(i)));
        }
        particles.stream().forEach(x->x.update(dt));

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
    public String toString(){
        return "3D Gravity engine supplied with the model "+model.toString();
    }

}
