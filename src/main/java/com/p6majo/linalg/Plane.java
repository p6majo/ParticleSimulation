package com.p6majo.linalg;

import java.util.ArrayList;
import java.util.List;

/**
 * The class Plane
 *
 * @author p6majo
 * @version 2020-03-31
 */
public class Plane {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private final Vector3D normal;
    private final Vector3D support;
    private final double[] equation;
    private List<Vector3D> orthonormalFrame;


    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    /**
     * Construct a plane that is perpendicular to a normal vector and contains a given point
     *
     * @param normal normal vector
     * @param point supporting point
     */
    public Plane(Vector3D normal, Vector3D point){
        this.normal = normal;
        this.support = point;

        equation = new double[4];
        equation[0]=normal.getX();
        equation[1]=normal.getY();
        equation[2]=normal.getZ();
        equation[3]=normal.dot(point);


}
    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public Vector3D getNormal() {
        return normal;
    }

    public Vector3D getSupport() {
        return support;
    }

    /**
     * Return the equation that defines the plane.
     * @return
     */
    public double[] getEquation(){
        return this.equation;
    }

    /**
     * return three orthnormal vectors, the last of them is orthogonal to the plane,
     * the first two lie in the plane
     * @return
     */
    public List<Vector3D> getOrthonormalFrame(){
        if (orthonormalFrame!=null)
            return orthonormalFrame;
        else {
            Vector3D normalNorm = this.normal.mul(1. / Math.sqrt(normal.getNorm2()));
            //construct the first vector by considering special cases first and than the generic case.
            Vector3D normalSpan1 = null;
            if (normalNorm.getX() == 0)
                normalSpan1 = new Vector3D(1., 0, 0);
            else if (normalNorm.getY() == 0)
                normalSpan1 = new Vector3D(0, 1, 0);
            else if (normalNorm.getZ() == 0)
                normalSpan1 = new Vector3D(0, 0, 1);
            else {
                normalSpan1 = new Vector3D(normalNorm.getY(), -normalNorm.getX(), 0);
                normalSpan1 = normalSpan1.mul(1. / normalSpan1.length());
            }
            //the second vector is constructed with the cross product
            Vector3D normalSpan2 = normalNorm.cross(normalSpan1);
            this.orthonormalFrame = new ArrayList<Vector3D>();
            this.orthonormalFrame.add(normalSpan1);
            this.orthonormalFrame.add(normalSpan2);
            this.orthonormalFrame.add(normalNorm);
        }
        return this.orthonormalFrame;
    }

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
        String out = "";
        out+="Plane with the equation ("+equation[0]+")x+("+equation[1]+")y+("+equation[2]+")z="+equation[3]+"\n";
        out+="span vector 1: "+this.getOrthonormalFrame().get(0).toString()+"\n";
        out+="span vector 2: "+this.getOrthonormalFrame().get(1).toString()+"\n";
        out+="normal vector: "+this.getOrthonormalFrame().get(2).toString()+"\n";
        return out;
    }



}
