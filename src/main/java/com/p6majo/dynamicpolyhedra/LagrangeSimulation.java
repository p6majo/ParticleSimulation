package com.p6majo.dynamicpolyhedra;

import com.p6majo.octtree.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * The LagrangeSimulation runs a Lagrange model.
 *
 * @author p6majo
 * @version 2019-03-05
 */
public class LagrangeSimulation {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    protected boolean running = false;
    protected Timer timer;
    private LagrangeModel model;
    private List<Particle> particles;
    private List<SimulationView> views;

    double time;
    int milliSeconds;
    int resolution = 30;  //Value between 1 and 100 to control the amount of additional time steps taken inbetween to frames
    //the smaller the faster but the less accurate. If the ball jumps higher after the bounce,
    //it's a good indication that the resolution has to be increased.

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public LagrangeSimulation(LagrangeModel model) {
        this.model = model;
        this.time = 0.;
        this.milliSeconds = 10;

        this.particles = new ArrayList<>();
        model.setupInitialData(particles);
        this.views = new ArrayList<>();
    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */


    public double getTime(){
        return time;
    }
    public List<Particle> getParticles(){
        return particles;
    }
    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */


    public void setMilliSeconds(int milliSeconds){
        this.milliSeconds = milliSeconds;
    }

    public void addView(SimulationView view){
        this.views.add(view);
    }

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
                for (int i = 0; i < resolution; i++) {
                    timestep();
                }
                time+= milliSeconds;
                for (SimulationView view : views) {
                    view.draw(time,particles);
                }
            }
        },0, milliSeconds);
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
     * Evolve the model
     */
    private void timestep(){
        model.evolve( (double) milliSeconds/1000./resolution);
    }


    /*
     ***********************************************
     ***           Overrides            ************
     ***********************************************
     */



    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    @Override
    public String toString() {
        return
                "Simulation of a Lagrange model: \n"+
                        "________________________________\n"+
                        model.toString();
    }



}
