package com.p6majo.povrayanimation.objects;


import com.p6majo.linalg.Vector3D;
import com.p6majo.povrayanimation.appearances.Appearance;

/**
 * The class Ray
 *
 * @author com.p6majo
 * @version 2020-07-22
 *
 */
public class Ray extends PovrayObject {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private Cone cone;
    private Cylinder cylinder;


    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public Ray(double radius, Vector3D start, Vector3D end, Appearance appearance) {
        super(appearance);
        this.cylinder = new Cylinder(radius,start,end,appearance);
        this.cone = new Cone(radius*3,end,start.add(start.directionTo(end).mul(1.1)),appearance);
    }

    public Ray(Cone cone, Cylinder cylinder){
        super(cone.appearance);
        this.cone = cone;
        this.cylinder = cylinder;
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
        out.append(this.cylinder.draw())
                .append(this.cone.draw());
        return out.toString();
    }

    @Override
    public PovrayObject copy(){
        return new Ray((Cone) cone.copy(),(Cylinder) cylinder.copy());
    }

    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    @Override
    public String toString() {
        return "Ray with cylinder "+cylinder.toString()+" and cone "+cone.toString();
    }





}
