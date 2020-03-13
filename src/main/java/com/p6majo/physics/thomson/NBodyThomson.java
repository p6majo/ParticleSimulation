package com.p6majo.physics.thomson;

import com.jogamp.opengl.util.FPSAnimator;
import com.p6majo.opengl.GLView2;

import javax.swing.*;

/**
 * @author p6majo
 * @version 2019-04-04
 */
public class NBodyThomson extends JFrame {

    private GLView2 glView;

    /*
     **********************
     ***   attributes   ***
     **********************
     */

    public void createUIComponents() {

    }

    /*
     *********************
     ***  constructors ***
     *********************
     */

    public NBodyThomson() {
        this("Thomson simulation");
    }

    public NBodyThomson(String name) {
        super();


        ThomsonSimulation thomson = new ThomsonSimulation(3,8,0.03);


        glView  = new GLView2(600,600, thomson);

        this.setContentPane(glView);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1600, 1600);
        this.setLocation(0, 0);
        this.setVisible(true);


        FPSAnimator animator = new FPSAnimator(glView.getCanvas(),20);

        thomson.start();
        animator.start();

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


    public static void main(String[] args) {
        new NBodyThomson();
    }

    @Override
    public String toString() {
        return super.toString();
    }


}
