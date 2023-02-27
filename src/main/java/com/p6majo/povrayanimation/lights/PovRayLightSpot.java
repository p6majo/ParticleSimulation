package com.p6majo.povrayanimation.lights;

import com.p6majo.linalg.Vector3D;
import com.p6majo.povrayanimation.objects.PovrayObject;

import java.awt.*;

/**
 * The class PovRayLight
 *
 * @author p6majo
 * @version 2020-05-26
 */
public class PovRayLightSpot extends PovRayLight {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */


    public PovRayLightSpot(Vector3D position, Color color) {
        super(position,color);
    }

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */


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
    public String draw() {
        StringBuilder lightOut = new StringBuilder();
        lightOut.append("light_source {\n")
                .append("\t"+this.position.toPovString()+"\n")
                .append("\t"+appearance.getColorLightString()+"\n")
                .append("\tspotlight\n")
                .append("}\n");
                return lightOut.toString();
    }

    @Override
    public PovrayObject copy() {
        return new PovRayLightSpot(this.position,this.color);
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
