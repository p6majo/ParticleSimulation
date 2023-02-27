package com.p6majo.povrayanimation.objects;


import com.p6majo.linalg.Vector3D;
import com.p6majo.povrayanimation.appearances.Appearance;

/**
 * The class Cone
 *
 * @author com.p6majo
 * @version 2020-07-22
 */
public class Cone extends PovrayObject{

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private double radius;
    private Vector3D initialStart;
    private Vector3D initialEnd;



    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public Cone(double radius, Vector3D initialStart, Vector3D end, Appearance appearance) {
        super(appearance);
        this.radius = radius;
        this.initialStart = initialStart;
        this.initialEnd = end;
        this.appearance = appearance;
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
    public String draw() {
        StringBuilder povString = new StringBuilder();
        povString.append("cone {\n")
                .append("\t"+ initialStart.toPovString()+","+ this.radius+"\n")    // Center and radius of one end
                .append("\t"+initialEnd.toPovString()+", 0.\n")    // Center and radius of other end
                .append("\t"+appearance.getPovrayRepresentation())
                .append("}\n");
        return povString.toString();
    }

    @Override
    public PovrayObject copy() {
        return new Cone(this.radius,this.initialStart,this.initialEnd,this.appearance);
    }


    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    @Override
    public String toString() {
        return "Cone with start "+ initialStart.toString()+" and end "+this.initialEnd.toString()+" and radius "+radius+" and appearance "+appearance.toString();
    }



}
