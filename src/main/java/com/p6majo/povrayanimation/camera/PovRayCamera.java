package com.p6majo.povrayanimation.camera;

import com.p6majo.linalg.Vector3D;
import com.p6majo.povrayanimation.objects.PovrayObject;

/**
 * The class Camera
 *
 * @author p6majo
 * @version 2020-05-26
 *
 * @version 2020-08-08
 * converted to PovrayObject
 */
public abstract class PovRayCamera extends PovrayObject {


    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    protected double state=0.;
    protected Vector3D position = null;
    protected Vector3D lookAt = null;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    /**
     */
    protected PovRayCamera() {
        super(null);
        this.position = Vector3D.getUnitX();
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

    public void setState(double state){
        this.state = state;
    }
    public void setPosition(Vector3D position){this.position = position;    }
    public void setLookAt(Vector3D lookAt){this.lookAt = lookAt;}

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


}
