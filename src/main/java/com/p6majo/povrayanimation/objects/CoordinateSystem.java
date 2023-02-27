package com.p6majo.povrayanimation.objects;


import com.p6majo.linalg.Vector3D;
import com.p6majo.povrayanimation.appearances.Appearance;

import java.awt.*;

/**
 * The class CoordinateSystem
 *
 * @author com.p6majo
 * @version 2020-07-22
 */
public class CoordinateSystem extends  PovrayObject {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private Ray rayX;
    private Ray rayY;
    private Ray rayZ;

    private double size;


    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public CoordinateSystem(double size){
        super(null);
        this.size = size;
        rayZ = new Ray(size/100., Vector3D.getZERO(),Vector3D.getUnitZ().mul(1.1*size), new Appearance(Color.blue));
        rayY = new Ray(size/100.,Vector3D.getZERO(),Vector3D.getUnitY().mul(1.1*size),new Appearance(Color.green));
        rayX = new Ray(size/100.,Vector3D.getZERO(),Vector3D.getUnitX().mul(1.1*size),new Appearance(Color.red));
    }

    public CoordinateSystem(Ray rayX, Ray rayY, Ray rayZ){
        super(rayX.appearance);
        this.rayZ = rayZ;
        this.rayY = rayY;
        this.rayX = rayX;
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
        StringBuilder out = new StringBuilder();
        out.append(this.rayX.draw())
        .append(this.rayY.draw())
        .append(this.rayZ.draw());

        return out.toString();

    }

    @Override
    public PovrayObject copy() {
        return new CoordinateSystem((Ray) this.rayX.copy(),(Ray) this.rayY.copy(),(Ray) this.rayZ.copy());
    }


    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    @Override
    public String toString(){
        return "Coordinate syste of size "+size;
    }
}
