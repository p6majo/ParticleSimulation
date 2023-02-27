package com.p6majo.transformation;

import com.p6majo.linalg.Vector;
import com.p6majo.linalg.Vector3D;

/**
 * The class Translation
 *
 * @author com.p6majo
 * @version 2020-07-21
 */
public class Translation extends ContinuousTransformation {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private Vector translation;


    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public Translation(Vector translation){
        super(0,1);
        this.translation = translation;

        setLabel("translation by "+translation.toString());
    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */



    public Vector3D getTranslation(){ return translation.mul(super.state).toVector3D(); }

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



}
