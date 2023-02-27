package com.p6majo.fieldextensions;


import com.p6majo.linalg.Vector3D;

import java.util.List;

/**
 * The class Quaternion
 *
 * A simple class that implements the quaternions and its basic operations
 *
 * @author com.p6majo
 * @version 2020-05-22
 */
public class Quaternion {


    public static final Quaternion ONE = new Quaternion(Complex.ONE,Complex.NULL);
    public static final Quaternion NULL = new Quaternion(Complex.NULL,Complex.NULL);
    public static final Quaternion I = new Quaternion(Complex.I,Complex.NULL);
    public static final Quaternion J = new Quaternion(Complex.NULL,Complex.ONE);
    public static final Quaternion K = new Quaternion(Complex.NULL,Complex.I);

    public static Quaternion random(double bound){
        return new Quaternion(Complex.random(bound),Complex.random(bound));
    }


    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

        private Complex a;
        private Complex b;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public Quaternion(Complex a, Complex b) {
        this.a = a;
        this.b = b;
    }

    public Quaternion(double real, Vector3D vector){
        this(new Complex(real,vector.getX()),new Complex(vector.getY(),vector.getZ()));
    }

    public Quaternion(Vector3D vector){
        this(0,vector);
    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public Complex getFirstComplex(){
        return this.a;
    }


    public Complex getSecondComplex(){
        return this.b;
    }

    /**
     * Of the quaternion q=(a,b,c,d) the vector part is returned, i.e. new Vector3D(b,c,d);
     * @return
     */
    public Vector3D getVector(){
        return new Vector3D(this.a.im(),this.b.re(),this.b.im());
    }

    public double re(){
        return this.a.re();
    }

    public Vector3D getEulerAngles(){
        double phi,theta,psi;
        double q0 = a.re();
        double q1 = a.im();
        double q2 = b.re();
        double q3 = b.im();
        phi = Math.atan2(2.*(q0*q1+q2*q3),1.-2.*(q1*q1+q2*q2));
        theta = Math.asin(2.*(q0*q2-q3*q1));
        psi = Math.atan2(2.*(q0*q3+q1*q2),1.-2.*(q2*q2+q3*q3));
        return new Vector3D(phi,theta,psi);
    }

    public Vector3D getEulerAnglesDegrees(){
        return getEulerAngles().mul(180./Math.PI);
    }

    /**
     * Return all four components of the quaternion as a list.
     *
     * @return
     */
    public List<Double> toList(){
        List<Double> components = this.getVector().toList();
        components.add(0,this.re());
        return components;
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

    /**
     * (a,b)*(c,d) = (ac-d<sup>&#8902;</sup>b,da+bc<sup>&#8902;</sup>)
     * @param factor
     * @return
     */
    public Quaternion mul(Quaternion factor){
        Complex c = factor.a;
        Complex d = factor.b;
        return new Quaternion(a.times(c).sub(b.times(d.conjugate())),d.times(a).plus(b.times(c.conjugate())));
    }

    public Quaternion add(Quaternion summand){
        return new Quaternion(this.a.plus(summand.a), this.b.plus(summand.b));
    }

    public Quaternion sub(Quaternion subtrahend){
        return this.add(subtrahend.neg());
    }

    public Quaternion conjugate(){
        return new Quaternion(this.a.conjugate(),this.b.neg());
    }

    public Quaternion neg(){
        return new Quaternion(this.a.neg(),this.b.neg());
    }

    public Quaternion scalarMul(double lambda){
        return new Quaternion(this.a.scale(lambda),this.b.scale(lambda));
    }

    public double abs(){
        return Math.sqrt(this.norm());
    }

    public double norm(){
        return a.norm()+b.norm();
    }

    public Quaternion inverse(){
        return this.conjugate().scalarMul(1./this.norm());
    }

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
        double a = this.a.re();
        double b = this.a.im();
        double c = this.b.re();
        double d = this.b.im();
        StringBuilder out = new StringBuilder();
        out.append("(");
        if (a!=0)
            out.append(String.format("%.3f",a));
        if (b!=0){
            if (Math.abs(b)==1)
                out.append((b<0?"-":"+")+"i");
            else
                out.append((b<0?"":"+")+String.format("%.3fi",b));
        }
        if (c!=0){
            if (Math.abs(c)==1)
                out.append((c<0?"-":"+")+"j");
            else
                out.append((c<0?"":"+")+String.format("%.3fj",c));
        }
        if (d!=0) {
            if (Math.abs(d) == 1)
                out.append((d < 0 ? "-" : "+") + "k");
            else
                out.append((d < 0 ? "" : "+") + String.format("%.3fk",d));
        }
        out.append(")");
        return out.toString();
    }


}
