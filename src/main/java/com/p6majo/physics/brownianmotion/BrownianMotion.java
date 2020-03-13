package com.p6majo.physics.brownianmotion;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The class BrownianMotion provides a simulation of brownian motion as a stochastical process
 *
 * The world is a two-dimensional lattice with periodic boundary conditions. We start with an
 * initial delta-function distribution in the center of the world and let this distribution evolve
 * in discrete time steps with random nearest neighbour interaction.
 *
 * The interaction is simulated with a matrix kernel that the world is convoluted with at every time step
 *
 * @author p6majo
 * @version 2019-05-09
 */
public class BrownianMotion {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */
    private final List<BrownianMotionListener> listeners;

    private final int n;
    private double[][] world;
    private double[][] interactionKernel;

    private Timer timer;
    private double t = 0.;


    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    /**
     * Construct a brownian motion torus
     * @param n lattice size per dimension
     * @param s the total probablility to move away
     */
    public BrownianMotion(int n,double s){

        listeners = new ArrayList<>();
        world = new double[n][n];
        world[n/2][n/2]=1.; //set initial distribution
        this.n = n;

        //createNearestNeighbourKernel(s);
        //createRandomKernel(s,15);
        //createSparseKernel(s,15);
        createCircleKernel(s,20);

        printKernel();


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
    public void addListener(BrownianMotionListener listener){
        this.listeners.add(listener);
    }

    public void setProbability(double s){
        interactionKernel = new double[][]{{0.,s/4,0.},{s/4,1.-s,s/4},{0.,s/4,0.}};
    }


    /*
     ***********************************************
     ***           Public methods       ************
     ***********************************************
     */

    /**
     * start the simulation
     */
    public void start(){
        timer = new Timer();
        for (BrownianMotionListener listener : listeners) {
            Data data = new Data();
            data.world = world;
            listener.updateValues(data);
        }

        timer.scheduleAtFixedRate(new TimerTask(){

            @Override
            public void run() {
                t+=0.050;
                timeStep();
                Data data = new Data();
                data.world = world;
                for (BrownianMotionListener listener : listeners) {
                    listener.updateValues(data);
                }
            }
        },50,10);
    }

    /**
     * stop the simulation
     */
    public void stop(){
        timer.cancel();
    }


    /*
     ***********************************************
     ***           Private methods      ************
     ***********************************************
     */


    private void createCircleKernel(double s,int size){
        double sum = 0.;
        interactionKernel = new double[size][size];
        //put random values at all positions excluding the middle position
        int m = size/2;
        for (int i = 0; i < size ; i++) {
            for (int j = 0; j < size; j++) {
                double r = Math.sqrt((i-m)*(i-m)+(j-m)*(j-m));
                if (r>size/2 && r<size/1.9){
                    double p = Math.random();
                    sum+=p;
                    interactionKernel[i][j]=p;
                }
            }
        }

        double norm = s/sum;

        for (int i = 0; i < size ; i++)
            for (int j = 0; j < size; j++)
                interactionKernel[i][j]*=norm;

        interactionKernel[m][m]=1.-s;
    }

    private void createRandomKernel(double s, int size){
        double sum = 0.;
        interactionKernel = new double[size][size];
        //put random values at all positions excluding the middle position
        int m = size/2;
        for (int i = 0; i < size ; i++) {
            for (int j = 0; j < size; j++) {
                if (i!=m|| j!=m){
                    double p = Math.random();
                    sum+=p;
                    interactionKernel[i][j]=p;
                }
            }
        }

        double norm = s/sum;

        for (int i = 0; i < size ; i++)
            for (int j = 0; j < size; j++)
                interactionKernel[i][j]*=norm;

        interactionKernel[m][m]=1.-s;
    }

    private void createSparseKernel(double s, int size){
        //sparse kernel
        double sum = 0.;
        interactionKernel = new double[size][size];
        //put random values at all positions excluding the middle position
        int m = size/2;
        for (int i = 0; i < size ; i++) {
            for (int j = 0; j < size; j++) {
                if (i!=m|| j!=m){
                    double p = Math.random();
                    if (p<0.05) { //only every tenth pixel of the interaction kernel will be non-zero
                        sum += p;
                        interactionKernel[i][j] = p;
                    }
                }
            }
        }

        double norm = s/sum;

        for (int i = 0; i < size ; i++)
            for (int j = 0; j < size; j++)
                interactionKernel[i][j]*=norm;

        interactionKernel[m][m]=1.-s;
    }

    private void createNearestNeighbourKernel(double s){
        //symmetric nearest neighbour kernel
        interactionKernel = new double[][]{{0.,s/4,0.},{s/4,1.-s,s/4},{0.,s/4,0.}};


    }


    /**
     * in this method the convolution is performed
     */
    private void timeStep(){
        double[][] image = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double v = world[i][j];
                if (v!=0.){
                    //apply kernel
                    for (int k = 0; k < interactionKernel.length; k++) {
                        int mk = interactionKernel.length/2;
                        for (int l = 0; l < interactionKernel[k].length; l++) {
                            int ml =interactionKernel[k].length/2;
                            int posk = (i+(k-mk))%n;
                            if (posk<0)
                                posk+=n;
                            int posl = (j+(l-ml))%n;
                            if (posl<0)
                                posl+=n;
                            image[posk][posl]+=v*interactionKernel[k][l];
                        }
                    }
                }
            }
        }
        this.world=image;
    }

    private void printKernel(){
        for (int i = 0; i < interactionKernel.length; i++) {
            String out = "";
            for (int j = 0; j < interactionKernel[i].length; j++) {
                out+=String.format("%.2f ",interactionKernel[i][j]);
            }
            System.out.println(out);
        }
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
        return super.toString();
    }


}
