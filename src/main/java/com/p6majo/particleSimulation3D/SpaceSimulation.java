package com.p6majo.particleSimulation3D;

import com.p6majo.gravityengines.GravityEngine3D;
import com.p6majo.models.Model3D;
import com.p6majo.models.Model3DGalaxy;
import com.p6majo.models.Model3DTwoGalaxies;
import com.p6majo.octtree.Cuboid;
import com.p6majo.octtree.Particle;


import java.util.*;


/**
 * The class NBodySimulation
 *
 * @author p6majo
 * @version 2019-03-05
 */
public class SpaceSimulation {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    protected boolean running = false;
    protected Timer timer;
    private Cuboid container;
    Model3D model;
    GravityEngine3D engine;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public SpaceSimulation() {

       //model = new Model3DTwoBodies(20000,1,0.05);
       // model = new Model3DGalaxy(10000,1,0.005);
       model = new Model3DTwoGalaxies(1000000,1,0.01);
        this.container = model.getContainer();
        engine = new GravityEngine3D(model);
    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public Cuboid getContainer(){
        return this.container;
    }

    public List<Particle> getParticles(){
        return engine.getParticles();
    }

    public double getTime(){
        return engine.getTime();
    }
    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */



    /*
     ***********************************************
     ***           Public methods       ************
     ***********************************************
     */

    public void start(){

        timer = new Timer();
        running = true;
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                engine.timestep();
            }
        },0,(int) (10));
    }

    public void stop(){
        if (timer!=null)
            timer.cancel();
        running = false;
    }

    /*
     ***********************************************
     ***           Private methods      ************
     ***********************************************
     */




    /**
     * create n random particles at the surface of the sphere
     */


    /*
     ***********************************************
     ***           Overrides            ************
     ***********************************************
     */

    /**
     * this method is the heart of the simulation
     * first, the new cubed distance between all particles is calculated, which is necessary to speed up the calculation of the interactions
     * second, the timestep for each particle is performed
     */


    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    @Override
    public String toString() {
        return "Gravity simulation with "+this.getParticles().size()+" particles:";
    }



}
