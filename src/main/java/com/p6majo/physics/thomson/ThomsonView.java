package com.p6majo.physics.thomson;


import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.p6majo.opengl.GLView2;

import javax.swing.*;

/**
 * @author p6majo
 * @version 2019-04-04
 */
public class ThomsonView extends JFrame {

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


    public ThomsonView(String name, ThomsonSimulation simulation) {
        super();


        glView  = new GLView2(600,600, simulation);
        glView.setFocusable(true);
        this.setContentPane(glView);
        glView.requestFocus();

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1600, 1600);
        this.setLocation(0, 0);
        this.setVisible(true);


    }
    /*
     ************************
     ****       getter    ***
     ************************
     */

    /**
     * return the {@link GLCanvas} that can be used in the {@link FPSAnimator} of the controlling class
     * @return
     */
    public GLCanvas getGLCanvas(){
        return glView.getCanvas();
    }
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



    @Override
    public String toString() {
        return super.toString();
    }


}
