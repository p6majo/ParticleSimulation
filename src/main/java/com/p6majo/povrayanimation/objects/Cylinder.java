package com.p6majo.povrayanimation.objects;

import com.p6majo.linalg.Vector3D;
import com.p6majo.povrayanimation.appearances.Appearance;


/**
 * The class Cylinder
 *
 * @author com.p6majo
 * @version 2020-07-22
 */
public class Cylinder extends PovrayObject {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private double radius;
    private Vector3D start;
    private Vector3D end;


    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public Cylinder(double radius, Vector3D start, Vector3D end, Appearance appearance) {
        super(appearance);
        this.radius = radius;
        this.start = start;
        this.end = end;
    }



    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public double getLength(){
        return this.start.getDistance(end);
    }


    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */

    public void update(Vector3D start, Vector3D end){
        this.start = start;
        this.end = end;
    }

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
                .append("\t"+start.toPovString()+","+ this.radius+"\n")    // Center and radius of one end
                .append("\t"+end.toPovString()+","+this.radius+"\n")    // Center and radius of other end
                .append("\t"+super.appearance.getPovrayRepresentation(super.fadeInCounter,super.fadeInMax));
        if (!shadow)
            povString.append("\tno_shadow\n");
        povString.append("}\n");
        return povString.toString();
    }

    @Override
    public PovrayObject copy() {
        return new Cylinder(this.radius,this.start,this.end,this.appearance);
    }

    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    @Override
    public String toString() {
        return "Cylinder with start "+start.toString()+" and end "+end.toString()+" and radius "+radius+" and appearance "+super.appearance.toString();
    }


}
