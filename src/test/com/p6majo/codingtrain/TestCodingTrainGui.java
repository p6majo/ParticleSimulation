package com.p6majo.codingtrain;

import java.awt.*;
import java.util.Random;

/**
 * @author p6majo
 * @version 2019-10-28
 */
public class TestCodingTrainGui extends CodingTrainGui {



    /*
     **********************
     ***   attributes   ***
     **********************
     */

    private Random rnd;

    /*
     *********************
     ***  constructors ***
     *********************
     */


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

    public TestCodingTrainGui(int width, int height, int framerate){
        super(width,height,framerate);
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

    @Override
    public void setup() {
        rnd = new Random();
    }



    @Override
    public void draw() {
        canvas.setPenColor(new Color(rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256)));
        canvas.filledCircle(rnd.nextInt(width),rnd.nextInt(height),rnd.nextInt(100));
    }



    /*
     ******************************
     ****     toString()        ***
     ******************************
     */


    @Override
    public String toString() {
        return super.toString();
    }


    public static void main(String[] args) {
        new TestCodingTrainGui(300,300,100);
    }
}
