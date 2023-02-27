package com.p6majo.povrayanimation.appearances;

import java.awt.*;

/**
 * The class PlaneAppearance
 *
 * @author com.p6majo
 * @version 2020-07-29
 */
public class PlaneAppearance extends Appearance {

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

    public PlaneAppearance(Color color){
        this(color,0.3);
    }

    /**
     * Compatability constructor for javafx colors
     * @param color
     */
    public PlaneAppearance(javafx.scene.paint.Color color){
        this(new Color((int) Math.floor(color.getRed()*255),(int) Math.floor(color.getGreen()*255),(int) Math.floor(color.getBlue()*255),(int) Math.floor(color.getOpacity()*255)));
    }

    public PlaneAppearance(Color color, double alpha){
        super(color);
        super.setAlpha(alpha);
        super.setPhong(1);
        super.setPhongSize(700);
        super.setReflection(0.);
    }

    /**
     * Compatability constructor for javafx colors
     * @param color
     */
    public PlaneAppearance(javafx.scene.paint.Color color,double alpha){
        this(new Color((int) Math.floor(color.getRed()*255),(int) Math.floor(color.getGreen()*255),(int) Math.floor(color.getBlue()*255),(int) Math.floor(color.getOpacity()*255)),alpha);
    }

    public PlaneAppearance(String texture){
        super(texture);
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
