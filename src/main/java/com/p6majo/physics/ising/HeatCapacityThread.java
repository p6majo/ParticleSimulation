package com.p6majo.physics.ising;

/**
 * The class HeatCapacityThread
 *
 * @author p6majo
 * @version 2019-04-16
 */
public class HeatCapacityThread extends Thread {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private Ising ising;
    private double temperature;
    private int steps;
    private HeatCapacityListener listener;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */
    public HeatCapacityThread(Ising ising, double temperature, int steps, HeatCapacityListener listener){
        this.ising = ising;
        this.temperature = temperature;
        this.steps=steps;
        this.listener = listener;
    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */



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


    /*
     ***********************************************
     ***           Private methods      ************
     ***********************************************
     */


    /*
     ***********************************************
     ***           Overrides            ************
     ***********************************************
     */


    @Override
    public void run() {
        System.out.println("Heat capacity thread for " + this.temperature + " started!");
        double e= 0;
        double e2 = 0;
        //use 10 percent of the steps for callibration
        int calibSteps = (int) (steps*0.5);
        for (int i = 0; i < calibSteps; i++) {
            ising.silentStep();
        }
        //steps for temporal average
        int averagingSteps = steps-calibSteps;
        for (int i=0;i<averagingSteps;i++){
            ising.silentStep();
            e+=ising.energy();
            e2+=ising.energy2();
        }
        e/=averagingSteps;
        e2/=averagingSteps;

        if (listener!=null) {
            Data data = new Data();
            data.temperature = temperature;
            data.heatCapacity = (e2-e*e)/temperature/temperature;
            listener.updateFromHeatCapacityThread(data);
        }
    }


    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    @Override
    public String toString() {
        return super.toString();
    }


}
