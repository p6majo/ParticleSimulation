package com.p6majo.physics.ising;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * The class Ising
 * implements the two-dimensional Ising model.
 *
 * @author p6majo
 * @version 2019-04-01
 */
public class Ising {


    private int N;                  // N-by-N grid of sites
    private double J = 1.0;         // strength of spin-spin interaction (feromagentic when J is positive)
    private double kT;              // temperature (say between 1 and 4)
    private boolean[][] spin;       // up (true) or down (false)

    private final IsingGui gui;
    private Timer timer;  //timer for a regular run

    private double t=0;

    private java.util.List<IsingListener> listeners;
    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */



    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */


    /**
     * Constructor of the Ising model class
     *
     * @param gui graphical user interface
     * @param N number of sides of the N-by-N grid
     * @param kT temperature
     * @param p probability of up spin
     */
    public Ising(IsingGui gui,int N, double kT, double p) {
        this.gui = gui;
        this.N    = N;
        this.kT   = kT;
        this.spin = new boolean[N][N];

        // initialize at random
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                spin[i][j] = (Math.random() < p);


            listeners = new ArrayList<>();
    }


    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */


    public void addListener(IsingListener listener){
        this.listeners.add(listener);
    }

    public boolean[][] getSpin(){
        return this.spin;
    }

    public int getSize(){
        return N;
    }

    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */

    public void setTemperature(double kT){
        this.kT = kT;
    }

    /*
     ***********************************************
     ***           Public methods       ************
     ***********************************************
     */


    /**
     * calculates the total energy of the system
     * @return energy
     */
    public double energy() {
        double E = 0.0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                E += halfEnergy(i, j); //use halfEnergy to avoid double counting
        return E;
    }

    /**
     * calculates the total energy square of the system
     * @return energy
     */
    public double energy2() {
        double E = energy();
        return E*E;
    }



    /**
     * calculates the average magnetization
     * between -1 and 1
     * @return magnetization
     */
    public double magnetization() {
        int M = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (spin[i][j]) M++;
                else            M--;
            }
        }
        return 1.0 * M / (N * N);
    }

    /**
     * return the time of the simulation
     * @return time
     */
    public double getTime(){
        return t;
    }

    /**
     * perform a single time step for the lattice site (i,j)
     * @param i
     * @param j
     */
    public void step(int i, int j) {
        double deltaE = -2 * energy(i, j);
        // flip if energy decreases or get lucky
        if ((deltaE <= 0) || (Math.random() <= Math.exp(-deltaE / kT)))
            spin[i][j] = !spin[i][j];
    }

    /**
     * perform the time step for N^2 randomly selected lattice sites
     */
    public void phase() {
        for (int steps = 0; steps < N*N; steps++) {
            int i = (int) (Math.random() * N);
            int j = (int) (Math.random() * N);
            step(i, j);
        }
    }


    /**
     * start the simulation
     */
    public void start(){
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask(){

            @Override
            public void run() {
                t+=0.050;
                step();
                for (IsingListener listener : listeners) {
                    Data data = new Data();
                    data.time = t;
                    data.temperature = kT;
                    data.magnetization = magnetization();
                    listener.updateValues(data);
                }
            }
        },0,50);
    }

    /**
     * stop the simulation
     */
    public void stop(){
        timer.cancel();
    }


    /**
     * Start from total order to an equilibrium state for the given temperature
     */
    public void equilibriate(double temp){
        orderedState();
        kT = temp;
        for (int i = 0; i < 1000; i++) {
            silentStep();
        }
    }

    /**
     * Start from total order to an equilibrium state for the given temperature
     */
    public void equilibriate(){
        orderedState();
        for (int i = 0; i < 1000; i++) {
            silentStep();
        }
    }

    /*
     ***********************************************
     ***           Private methods      ************
     ***********************************************
     */



    /**
     * total energy of site (i, j), using periodic boundary conditions
     * assumes 0 <= i, j < N
     * @param i
     * @param j
     * @return energy
     */
    private double energy(int i, int j) {
        double E = 0.0;
        if (spin[i][j] == spin[(i+1)%N][j])   E++;
        else                                  E--;
        if (spin[i][j] == spin[i][(j+1)%N])   E++;
        else                                  E--;
        if (spin[i][j] == spin[(i-1+N)%N][j]) E++;
        else                                  E--;
        if (spin[i][j] == spin[i][(j-1+N)%N]) E++;
        else                                  E--;
        return -J * E;
    }

    /**
     * here only the interaction to the left and down is considered, this way, there is no overcounting
     * this method can be used for faster computing the total energy
     * @param i
     * @param j
     * @return
     */
    private double halfEnergy(int i,int j){
        double E=0.0;
        if (spin[i][j] == spin[(i+1)%N][j])   E++;
        else                                  E--;
        if (spin[i][j] == spin[i][(j+1)%N])   E++;
        else                                  E--;
        return E;
    }

    private void orderedState(){
        // initialize at random
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                spin[i][j] = true;

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

    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        String s = "";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (spin[i][j]) s += "< ";
                else            s += "> ";
            }
            s += NEWLINE;
        }
        return s;
    }


    /**
     * perform a simulation step
     */
    public void step() {
            phase();
            gui.update(spin);
    }

    /**
     * perform a simulation step without calling a guiupdate
     */
    public void silentStep(){
        phase();
    }

}
