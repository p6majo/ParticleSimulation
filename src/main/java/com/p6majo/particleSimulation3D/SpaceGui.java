package com.p6majo.particleSimulation3D;


import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.p6majo.opengl.SpaceView;

import javax.swing.*;

/**
 * @author p6majo
 * @version 2019-04-04
 */
public class SpaceGui extends JFrame {

    private SpaceView spaceView;

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


    public SpaceGui(String name, SpaceSimulation simulation) {
        super();


        spaceView = new SpaceView(2000,1000, simulation);
        spaceView.setFocusable(true);
        this.setContentPane(spaceView);
        spaceView.requestFocus();

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(2250, 1250);
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
        return spaceView.getCanvas();
    }

    public SpaceView getSpaceView(){return this.spaceView;}
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
