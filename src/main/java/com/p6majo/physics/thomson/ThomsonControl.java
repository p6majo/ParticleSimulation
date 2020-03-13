package com.p6majo.physics.thomson;

import com.jogamp.opengl.util.FPSAnimator;
import com.p6majo.genericguis.Flag;
import com.p6majo.genericguis.ParameterGui;
import com.p6majo.genericguis.ParameterGuiListener;
import com.p6majo.parameterspace.Parameter;

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
public class ThomsonControl implements ParameterGuiListener {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */


    public FPSAnimator animator;
    public ThomsonView thomsonView;
    public ThomsonSimulation simulation;
    public ParameterGui paramGui;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public ThomsonControl(){

        //setup parameter gui
        paramGui = new ParameterGui(3,2,this);

        paramGui.addParameter(new Parameter("Number of Charges",3,2,102,100));
        paramGui.addParameter(new Parameter("Smallest Bound",10,0,100,100));
        paramGui.addParameter(new Parameter("Largest Bound",90,0,100,100));

        paramGui.addFlag(new Flag("2D force",true));
        paramGui.addFlag(new Flag("3D force",false));

        paramGui.display();

        //setup simulation
        simulation = new ThomsonSimulation(3,3,0.03);

        //setup glview

        thomsonView = new ThomsonView("Thomson Problem Simulation",simulation);
        animator = new FPSAnimator(thomsonView.getGLCanvas(),20);

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
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ThomsonControl();
            }
        });

    }


    @Override
    public void valueChange(int index, double value){

        switch(index){
            case 0: //number of charges
                int n =(int) paramGui.getValue(0);
                if (simulation.getNumber()!=n)
                    simulation.setNumber(n);
                break;
            case 1: //change the value of the smallest bound length
                simulation.setSmallestLength(paramGui.getValue(1)/100);
                break;
            case 2: //change the value of the largest bound length
                simulation.setLargestLength(paramGui.getValue(2)/100);
                break;
        }
    }

    @Override
    public void flagValueChange(int index, boolean selected) {
        if (selected) {
            switch (index) {
                case 0: //2d
                    simulation.setForceLaw(artificial2d);
                    break;
                case 1: //3d
                    simulation.setForceLaw(coulomb3d);
                    break;
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
