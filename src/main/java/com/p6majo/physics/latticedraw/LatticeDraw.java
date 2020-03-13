package com.p6majo.physics.latticedraw;


import java.awt.*;

/**
 * An extension of the class {@link Canvas} to visualize lattice data
 * <br><br>
 * The data can be provided as a two-dimensional array of boolean values
 * or as a two-dimensional array of double values which is converted into corresponding
 * colour coding
 *
 *
 * @author p6majo
 * @version 2019-04-04
 */
public class LatticeDraw extends Canvas {

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

public LatticeDraw(){
    super();
}
    /*
     ************************
     ****       getter    ***
     ************************
     */

    /*
     ************************
     ****       setter    ***
     ************************
     */

    /*
     ******************************
     ****     public methods    ***
     ******************************
     */

    public void drawBooleanField(boolean[][] spin){
        int N = spin.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (spin[i][j])
                    this.setPenColor(WHITE);
                else
                    this.setPenColor(BLUE);
                this.filledSquare(i + 0.5, j + 0.5, .5);
            }
        }

        // drawBooleanField lines

        this.setPenColor(BLACK);
        for (int i = 0; i < N; i++) {
            this.line(i, 0, i, N);
            this.line(0, i, N, i);
        }

        this.update();
    }

    public void drawDouble01Field(double[][] values){
        double max = findMaximum(values);
        int N = values.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.setPenColor(new Color(Math.min(1,(float) (values[i][j]/max)),0f,0f));
                this.filledSquare(i + 0.5, j + 0.5, .5);
            }
        }

        /*
        this.setPenColor(GREEN);
        for (int i = 0; i < N; i++) {
            this.line(i, 0, i, N);
            this.line(0, i, N, i);
        }
        */

        this.update();
    }

    /*
     ******************************
     ****     private methods   ***
     ******************************
     */

    private double findMaximum(double[][] values){
        double max = 0.;
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                if (values[i][j]>max)
                    max = values[i][j];
            }
        }
        return max;
    }
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
    public String toString() {
        return super.toString();
    }

}
