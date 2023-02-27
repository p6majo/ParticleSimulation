package com.p6majo.povrayanimation.objects;


import com.p6majo.linalg.Vector3D;
import com.p6majo.povrayanimation.appearances.Appearance;

/**
 * The class Sphere
 *
 * @author com.p6majo
 * @version 2020-07-22
 */
public class Sphere extends PovrayObject{


    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private Vector3D initialPosition;
    private Vector3D position;
    private double radius=1.;

    private int id = 0;

    public Sphere(Vector3D initialPosition, double radius, Appearance appearance) {
        super(appearance);
        this.radius = radius;
        this.initialPosition = initialPosition;
        this.position = initialPosition;
        this.appearance = appearance;
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
    public Vector3D getPosition() { return position; }
    public double getRadius(){return this.radius;}


    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */

    public void setId(int id){
        this.id = id;
    }
    public void setRadius(double  radius){ this.radius = radius; }
    public void setPosition(Vector3D position){this.position = position;}




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

    public String draw() {
        StringBuilder out = new StringBuilder();
        Vector3D pos = getPosition();
        out.append("sphere{\n")
                .append("\t <"+pos.getX()+","+pos.getY()+","+pos.getZ()+">,"+this.radius+"\n")
                .append("\t"+appearance.getPovrayRepresentation(super.fadeInCounter,super.fadeInMax));
        if (!shadow)
            out.append("\tno_shadow\n");
        out.append("}\n");
        return out.toString();
    }


    @Override
    public PovrayObject copy() {
        return new Sphere(this.initialPosition,this.radius,this.appearance);
    }

    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    @Override
    public String toString(){
        return "sphere at "+position.toString()+" with radius "+radius+ " and "+appearance.toString();
    }



}
