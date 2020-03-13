package com.p6majo.physics.ising;

/**
 * The class CorrelationLengthCalculationThread
 *
 * @author p6majo
 * @version 2019-04-18
 */
public class CorrelationLengthCalculationThread extends Thread {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private Ising ising;
    private CorrelationLengthListener listener;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */
    public CorrelationLengthCalculationThread(Ising ising, CorrelationLengthListener listener){
        this.ising = ising;
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
        int N = ising.getSize();
        boolean[][] spin = ising.getSpin();
        /* go over diagonal spins only
        for (int l = 0; l < N/2; l++) {
            double cor = 0.;
            for (int i = 0; i < N; i++) {
                double s = spin[i][i]?1.:-1.;
                double c = 0.;
                int j = (i-l+N)%N;
                c+=s*(spin[i][j]?1.:-1.);
                c+=s*(spin[j][i]?1.:-1.);
                j=(i+l)%N;
                c+=s*(spin[i][j]?1.:-1.);
                c+=s*(spin[j][i]?1.:-1.);
                c/=4;
                cor+=c;
            }
            Data data = new Data();
            data.distance=l+0.0;
            data.correlation=cor;
            listener.updateFromCorrelationLengthThread(data);
        }
        */

        /*go over all spin pairs*/
        for (int l = 0; l < N/2; l++) {
            double cor = 0.;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    double s = spin[i][i] ? 1. : -1.;
                    int k1 = (i + l) % N;
                    int k2 = (j+l)%N;
                    cor += s * (spin[i][k1] ? 1. : -1.);
                    cor += s * (spin[k2][j] ? 1. : -1.);
                }
            }
            cor/=2*N*N;
            Data data = new Data();
            data.distance = l + 0.0;
            data.correlation = cor;
            listener.updateFromCorrelationLengthThread(data);
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
