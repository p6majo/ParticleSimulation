package com.p6majo.povrayanimation.appearances;

import java.awt.*;

/**
 * The class PlaneAppearance
 *
 * @author com.p6majo
 * @version 2020-07-29
 */
public class SphereAppearance extends Appearance {

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

    public SphereAppearance(Color color){
        super(color);
        super.setAlpha(1.);
        super.setPhong(1);
        super.setPhongSize(70);
        super.setBrilliance(4);
        super.setMetallic(true);
        super.setReflection(0.5);
    }

    /**
     * Compatability constructor for javafx colors
     * @param color
     */
    public SphereAppearance(javafx.scene.paint.Color color){
        this(new Color((int) Math.floor(color.getRed()*255),(int) Math.floor(color.getGreen()*255),(int) Math.floor(color.getBlue()*255),(int) Math.floor(color.getOpacity()*255)));
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


}
