package com.p6majo.transformation;

/**
 * The class Identity, is the trivial transformation, which returns the object itself
 *
 * @author com.p6majo
 * @version 2020-07-30
 */
public class Identity  extends Transformation {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */



    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public Identity(){
        setLabel("I");
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
    public TransformableObject applyTo(TransformableObject object){
         return object;
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
