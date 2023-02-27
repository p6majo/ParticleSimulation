package com.p6majo.povrayanimation.lights;



import com.p6majo.linalg.Vector3D;
import com.p6majo.povrayanimation.appearances.Appearance;
import com.p6majo.povrayanimation.objects.PovrayObject;
import java.awt.*;

/**
 * The class PovRayLight
 *
 * @author p6majo
 * @version 2020-05-26
 */
public class PovRayLight extends PovrayObject {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    protected Color color;
    protected Vector3D position;

    public PovRayLight(Vector3D position, Color color) {
        super(new Appearance(color));
        this.position = position;
        this.color = color;
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
                .append("\tparallel\n")
                .append("}\n");
                return lightOut.toString();
    }

    @Override
    public PovrayObject copy() {
        return new PovRayLight(this.position,this.color);
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
