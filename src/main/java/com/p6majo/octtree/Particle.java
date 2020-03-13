package com.p6majo.octtree;

import com.p6majo.utils.Utils;

/**
 * The class Particle
 *
 * @author p6majo
 * @version 2019-08-26
 */
public class Particle implements ObjectIn3DSpace {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */
    private Point3D position;


    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public Particle(Point3D low,Point3D high){
        this.position = this.randomPosition(low,high);
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

    public void changePosition(Point3D newPosition){
        this.position = newPosition;
    }

    /*
     ***********************************************
     ***           Private methods      ************
     ***********************************************
     */

    private Point3D randomPosition(Point3D low, Point3D high){
        double x = Utils.map(Math.random(),0,1,low.getX(),high.getX());
        double y = Utils.map(Math.random(),0,1,low.getY(),high.getY());
        double z = Utils.map(Math.random(),0,1,low.getZ(),high.getZ());
        return new Point3D(x,y,z);
    }

    /*
     ***********************************************
     ***           Overrides            ************
     ***********************************************
     */

    @Override
    public Point3D getPosition() {
        return this.position;
    }
    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    @Override
    public String toString() {
        return "P"+position.toString();
    }

}