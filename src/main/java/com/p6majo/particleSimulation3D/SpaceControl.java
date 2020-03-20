package com.p6majo.particleSimulation3D;

import com.jogamp.opengl.util.FPSAnimator;
import com.p6majo.genericguis.Flag;
import com.p6majo.genericguis.ParameterGui;
import com.p6majo.genericguis.ParameterGuiListener;
import com.p6majo.parameterspace.Parameter;
import com.p6majo.physics.thomson.ThomsonSimulation;
import com.p6majo.physics.thomson.ThomsonView;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.p6majo.physics.thomson.ThomsonSimulation.ForceLaw.artificial2d;
import static com.p6majo.physics.thomson.ThomsonSimulation.ForceLaw.coulomb3d;

/**
 * The class ThomsonControl is used to combine the <br>
 *     {@link ThomsonSimulation} with a <br>
 *         {@link ThomsonView} and a <br>
 *             {@link ParameterGui}
 *
 * @author p6majo
 * @version 2019-05-24
 */
public class SpaceControl implements ParameterGuiListener {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */


    public static FPSAnimator animator;
    public static SpaceGui spaceGui;
    public SpaceSimulation simulation;
    public ParameterGui paramGui;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public SpaceControl(){

        //setup parameter gui
        paramGui = new ParameterGui(3,2,this);

        paramGui.addParameter(new Parameter("Zoom",135,10,135,1000));
        paramGui.addParameter(new Parameter("NearShift",3000,0,10000,10000));
        paramGui.addParameter(new Parameter("CameraOutPos",0,0,10000,10000));

        paramGui.display();

        //setup simulation
        simulation = new SpaceSimulation();

        spaceGui = new SpaceGui("Space Simulation",simulation);//The Gui that contains the GLCanvas

        animator = new FPSAnimator(spaceGui.getGLCanvas(),25);  //Create an animator that drives canvas' display() at the specified FPS

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

    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    @Override
    public String toString() {
        return super.toString();
    }


    /*
     ***********************************************
     ***           main                 ************
     ***********************************************
     */

    public static void main(String[] args) {


        java.awt.EventQueue.invokeLater(() -> {
            new SpaceControl();
            spaceGui.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosing(WindowEvent e){
                    //use a dedicated thread to run the stop() to ensure that the animator stops before the program exits.
                    new Thread(() -> {
                        if (animator.isStarted())
                            animator.stop();
                            System.exit(0);
                    }).start();
                }
            });
        });

    }


    @Override
    public void valueChange(int index, double value){

        switch(index){
            case 0: //Zoom
               spaceGui.getSpaceView().setZoom(paramGui.getValue(0));
                break;
            case 1: //nearShift
                spaceGui.getSpaceView().setNearShift(paramGui.getValue(1));
                break;
            case 2: //change the value of the largest bound length
                spaceGui.getSpaceView().setCameraOutPos(paramGui.getValue(2));
                break;
        }
    }

    @Override
    public void flagValueChange(int index, boolean selected) {
        if (selected) {
            switch (index) {
                /*
                case 0: //2d
                    simulation.setForceLaw(artificial2d);
                    break;
                case 1: //3d
                    simulation.setForceLaw(coulomb3d);
                    break;
                 */
            }
            ;
        }
    }


    @Override
    public void buttonClicked(ParameterGui.ButtonEvent buttonEvent) {
        switch(buttonEvent){
            case start:
                //start everything
                simulation.start();
                animator.start();
                break;
            case stop:
                simulation.stop();
                animator.start();
                break;
            case exit:
                simulation.stop();
                animator.stop();
                System.exit(0);
                break;
        }
    }

}
