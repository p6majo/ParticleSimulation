package com.p6majo.transformation;

/**
 * The class ContinuousTransformation
 *
 * @author com.p6majo
 * @version 2020-07-22
 */
public class ContinuousTransformation extends Transformation {


    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    protected double finalState;
    private double initialState;
    protected double state;


    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public ContinuousTransformation( double finalState){
        this(finalState,0);
    }


    public ContinuousTransformation( double finalState, double initialState) {
        this.finalState=finalState;
        this.initialState = initialState;
        this.state = 1.; //set final state to the default state

        setLabel("Continuous transformation with continuous values in the range ["+initialState+"|"+finalState+"]");
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

    public void setState(double state){
        this.state = state;
    }

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




}
