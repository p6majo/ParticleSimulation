package com.p6majo.utils;

import com.p6majo.logger.Logger;

/**
 * A simple vector class that contains the standard mathematical operations for vectors
 *
 * @author p6majo
 * @version 2019-03-05
 */
public class Vector {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private final int dim;
    private final double[] components;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */
    public Vector(int dim, double ... components){

        this.dim = dim;

        this.components = new double[dim];
        int count  = 0;
        for (double comp:components){
            this.components[count]=comp;
            count++;
        }
        //fill remaining components with zeros
        for (int c = count;c<dim;c++) this.components[c]=0.;
    }

    public Vector( double ... components){
        this(components.length,components);
    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public double get(int i){
        return this.components[i];
    }

    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */

public void setValue(int i, double value){
    if (i>-1 && i<dim)
        this.components[i] = value;
    else
        Logger.logging(Logger.Level.error,"Unallowed access on vector component");
}

    /*
     ***********************************************
     ***           Public methods       ************
     ***********************************************
     */

    /**
     * Addition of vectors
     * @param summand
     * @return
     */
    public Vector add(Vector summand){
        Vector returnVector= new Vector(dim);
        for (int i = 0; i < dim; i++)
            returnVector.components[i]=this.components[i]+summand.components[i];

        return returnVector;
    }

    /**
     * Subtraction of vectors
     * @param subtrahend
     * @return
     */
    public Vector sub(Vector subtrahend){
        return this.add(subtrahend.mul(-1.));
    }

    /**
     * Scalar multiplication
     * @param scalar
     * @return
     */
    public Vector mul(double scalar){
        Vector returnVector = new Vector(dim);
        for (int i = 0; i < dim; i++) {
            returnVector.components[i] = this.components[i]*scalar;
        }
        return returnVector;
    }

    /**
     * Scalar product of two vectors
     * @param other
     * @return
     */
    public double dot(Vector other){
        double returnDot = 0;
        for (int i = 0; i < dim; i++) {
            returnDot+=this.components[i]*other.components[i];
        }
        return returnDot;
    }

    /**
     * Calculate the unit vector of the given vector <b>x</b>
     * @return <b>x</b>/|<b>x</b>|
     */
    public Vector normalize(){
        return this.mul(1./Math.sqrt(this.dot(this)));
    }

    /**
     * Calculate the cross product of two vectors
     * @param factor
     * @return
     */
    public Vector crossproduct(Vector factor){
        if (dim==3) {
            Vector returnVector = new Vector(this.dim);
            returnVector.components[0]=this.get(1)*factor.get(2)-this.get(2)*factor.get(1);
            returnVector.components[1]=this.get(2)*factor.get(0)-this.get(0)*factor.get(2);
            returnVector.components[2]=this.get(0)*factor.get(1)-this.get(1)*factor.get(0);
            return returnVector;
        }
        else{
            Logger.logging(Logger.Level.error,"Cross product is only defined in three dimensions.");
            return null;
        }
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
    public String toString(){
        StringBuilder out = new StringBuilder();
        out.append("(");
        for (int i=0;i<this.components.length-1;i++)
            out.append(components[i]+",");
        out.append(components[components.length-1]+")");
        return out.toString();
    }


}
