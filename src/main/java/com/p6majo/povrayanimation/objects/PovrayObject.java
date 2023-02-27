package com.p6majo.povrayanimation.objects;


import com.p6majo.povrayanimation.appearances.Appearance;

/**
 * The class PovrayObject
 *
 * @author com.p6majo
 * @version 2020-07-22
 */
public abstract class PovrayObject  {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    protected Appearance appearance;
    protected boolean shadow = false;
    protected int fadeInCounter = 0;
    protected int fadeInMax = 0;


    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */



    public PovrayObject(Appearance appearance){
        this.appearance = appearance;
    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public Appearance getAppearance(){
        return this.appearance;
    }



    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */


    public void setAppearance(Appearance appearance){this.appearance = appearance;}
    public void setShadow(boolean shadow){this.shadow = shadow; }
    /**
     * number of frames until the full opacity is reached.
     * @param max
     */
    public void setFadeInLength(int max){ this.fadeInMax = max; }
    public void incFadeIn(){if (this.fadeInCounter<this.fadeInMax) fadeInCounter++;}

    /*
     ***********************************************
     ***           Public methods       ************
     ***********************************************
     */



    public abstract String draw();

   public abstract PovrayObject copy();

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
