package com.p6majo.povrayanimation.appearances;

import java.awt.*;
import java.util.Locale;

/**
 * The class Appearance
 *
 * @author com.p6majo
 * @version 2020-07-22
 */
public class Appearance {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private Color color;
    private String texture;
    private double red;
    private double green;
    private double blue;
    private double alpha;

    protected double phong=1;
    protected int phongSize = 70;
    protected double ambient = 0.1;
    protected double diffuse = 0.9;
    protected double brilliance = 1.;
    protected double reflection = 0.;

    protected boolean metallic = false;
    private double tmpAlpha = 1.; //for fading, default is no fading

    protected Locale locale = Locale.US;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */


    public Appearance(Color color) {
        this.color = color;
        this.red = (double) color.getRed()/255;
        this.green = (double) color.getGreen()/255;
        this.blue = (double) color.getBlue()/255;
        this.alpha = (double) color.getAlpha()/255;
        this.texture = null;
    }

    public Appearance(String texture) {
        this.color = null;
        this.texture = texture;
    }
    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public Color getColor() {
        return color;
    }

    public String getTexture() {
        return texture;
    }

    /**
     * Used for light sources
     * @return
     */
    public String getColorLightString(){
        double r = (double) color.getRed()/255;
        double g = (double) color.getGreen()/255;
        double b= (double) color.getBlue()/255;

        return "color rgb <"+r+","+g+","+b+">";
    }

    public double getAlpha(){
        return tmpAlpha*this.alpha;
    }


    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */


    /**
     * Erzeugung von Glanzlichtern nach der phong-Methode
     *
     * @param phong (default 1)
     */
    public void setPhong(double phong){ this.phong = phong; }

    /**
     *  Glanzlicht-Größe bei phong-Glanzlichtern 1 =sehr matt; 40 =Plastik; 250 =hochpoliert!
     *
     * @param phongSize (default 70)
     */
    public void setPhongSize(int phongSize) { this.phongSize = phongSize; }

    /**
     *  Schattenhelligkeit der Farbe (Braucht nicht definiert zu werden! default: ambient 0.1)
     * Dies simuliert die durch indirekte Beleuchtung hervorgerufene Helligkeit der Schattenseite eines Objektes -
     * eine korrekte Berechnung von allen Lichtstrahlen, die zur indirekten Beleuchtung beitragen, ist praktisch unmöglich!
     * Eine Improvisation, welche aber diesbezüglich erstaunlich realistische Bilder liefert, wird durch die "radiosity"-Berechnung ermöglicht.)
     *
     * "ambient 1 diffuse 0" bedeutet, daß das beschriebene Objekt immer schattenlos mit voller Farbhelligkeit zu sehen ist (z.B. der blaue Himmel !)
     * @param ambient (default 0.1)
     */
    public void setAmbient(double ambient) { this.ambient = ambient; }

    /**
     * Der Anteil, der durch direkte Beleuchtung erzeugte Helligkeit (default: diffuse 0.6)
     * @param diffuse (default 0.9)
     */
    public void setDiffuse(double diffuse) { this.diffuse = diffuse; }

    /**
     * Oberflächenglanz, (default = 1) um so metallischer je höher dieser Wert ist
     * @param brilliance (default 1)
     */
    public void setBrilliance(double brilliance) { this.brilliance = brilliance; }

    /**
     * Läßt bei phong oder specular metallischen Glanz simulieren
     *
     * @param metallic (default false)
     */
    public void setMetallic(boolean metallic) { this.metallic = metallic; }

    /**
     * The value can range from 0.0 to 1.0. By default there is no reflection.
     * Adding reflection to a texture makes it take longer to render because an additional ray must be traced.
     *
     * @param reflection
     */
    public void setReflection(double reflection) {
        this.reflection = reflection;
    }

    /**
     * Value between 0 and 1 
     * @param alpha (default 0)
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /*
     ***********************************************
     ***           Public methods       ************
     ***********************************************
     */
    public String getPovrayRepresentation(int fadeInCounter, int fadeInMax) {
        if (fadeInMax>0)
            this.tmpAlpha = (double) fadeInCounter/fadeInMax;
        return getPovrayRepresentation();
    }

    public String getPovrayRepresentation(){
        StringBuilder out= new StringBuilder();
        if (texture!=null){
            out.append("\ttexture{"+texture+"}\n");
        }
        else {
            out.append(generateColorPigmentString()+"\n")
                    .append(getFinishRepresentation());
        }
        return out.toString();
    }

    public String getFinishRepresentation(){
        StringBuilder out= new StringBuilder();

        out.append("\tfinish {\n")
                .append(String.format(locale,"\t\tambient %.2f\n",ambient))
                .append(String.format(locale,"\t\tdiffuse %.2f\n",diffuse));
        if (reflection>0) {
            out.append("\t\treflection {\n")
                    .append(String.format(locale,"\t\t\t%.2f\n", reflection));
            if (metallic)
                out.append("\t\t\tmetallic\n");
            out.append("\t\t}\n");
        }
        out.append(String.format(locale,"\t\tbrilliance %.2f\n",brilliance))
                .append(String.format(locale,"\t\tphong %.2f\n",phong))
                .append(String.format("\t\tphong_size %d\n",phongSize))
                .append("\t}\n");

        return out.toString();
    }

    /*
     ***********************************************
     ***           Private methods      ************
     ***********************************************
     */

    /**
     * generates the color string and respects fading
     * @return
     */
    private String generateColorPigmentString(){
        double trans = 1. - this.getAlpha();
        return String.format(locale,"pigment {color rgb <%.2f,%.2f,%.2f> transmit %.2f }",this.red,this.green,this.blue,trans);
    }

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
        return "Appearance:\n"+getPovrayRepresentation();
    }



}
