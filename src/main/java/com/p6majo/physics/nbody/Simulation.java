package com.p6majo.physics.nbody;


import com.p6majo.utils.Boundary;
import com.p6majo.utils.Distance;
import com.p6majo.utils.Vector;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The abstract class for a simulation
 *
 * @author p6majo
 * @version 2019-05-14
 */
public abstract class Simulation {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    protected final int dim;
    protected int n;
    protected final double deltaT;
    protected boolean running = false;


    protected Boundary boundary;

    protected double t; //time parameter
    protected Timer timer;

    protected Particle[] particles;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */
    public Simulation(int dim,int n, double deltaT){
        this.dim = dim;
        this.n =n;
        this.t = 0.0;
        this.deltaT = deltaT;
        this.boundary =null;
    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */
    public Particle[] getParticles(){
        if (particles==null)
            createParticles();
        return this.particles;
    }

    public Boundary getBoundary(){
        return this.boundary;
    }

    public double getT(){
        return this.t;
    }

    public int getNumber(){
        return this.n;
    }

    public double getOrderParameter(){
        return 0.;
    }

    public abstract Distance[][] getDistances();

    public String[] getInfo(){
        return new String[]{""};
    }
    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */

    public void setBoundary(Boundary boundary){ this.boundary = boundary; }
    protected abstract void createParticles();
    protected abstract void timestep();
    public abstract void setNumber(int n);

    /*
     ***********************************************
     ***           Public methods       ************
     ***********************************************
     */



    /**
     * here it is possible to encode the action of some external field like gravity or magnetic field
     * @param particleIndex the index of the particle
     * @return
     */
    public Vector getForceField(int particleIndex){
        return null;
    }


    /**
     * here the interaction of the particles is calculated
     * @param particleIndex
     * @return
     */
    public Vector getInteraction(int particleIndex){
        return null;
    }

    public void start(){

        if (particles==null)
            createParticles();
        timer = new Timer();
        running = true;
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                t+=deltaT;
                timestep();
            }
        },0,(int) (deltaT*1000));
    }

    public void stop(){
       if (timer!=null)
           timer.cancel();
       running = false;
    }

    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */

    @Override
    public String toString(){
        return "simulation";
    }
}
