package com.p6majo.linalg;

import com.p6majo.logger.Logger;
import com.p6majo.logger.Logger.Level;
import com.p6majo.transformation.Rotation;
import com.p6majo.transformation.TransformableObject;
import com.p6majo.transformation.Transformation;
import com.p6majo.transformation.Translation;

import java.util.List;
import java.util.StringTokenizer;


/**
 * @author com.p6majo
 * @version 1.0
 * @version 2019-08-26
 * @version 2020-05-15 Transformation by Matrix
 *
 * @version 2020-09-11
 * added Math.round to hash-function to avoid conversion from 7.99999 -> 7
 *
 * @version 2020-09-30
 * convert to subclass of Vector
 *
 * @version 2020-12-19
 * implement the interface TransformableObject
 */
public class Vector3D extends Vector implements TransformableObject {

    /************************/
    /****       statics   ***/
    /************************/

    public static Vector3D getUnitZ(){ return new Vector3D(0,0,1); }
    public static Vector3D getUnitY(){ return new Vector3D(0,1,0); }
    public static Vector3D getUnitX(){ return new Vector3D(1,0,0); }
    public static Vector3D getZERO() {
        return new Vector3D(0, 0, 0);
    }
    public static Vector3D randomInt(int maxX, int maxY, int maxZ){ return new Vector3D(-maxX+(int) (2.*Math.random()*maxX),-maxY+(int) (2.*Math.random()*maxY),-maxZ+(int) (2.*Math.random()*maxZ)); }
    public static Vector3D random(double maxX, double maxY, double maxZ){ return new Vector3D(-maxX+2.*Math.random()*maxX,-maxY+2.*Math.random()*maxY,-maxZ+2.*Math.random()*maxZ); }
    public static Vector3D midPoint(Vector3D one, Vector3D two){ return new Vector3D(Vector.midPoint(one,two).entries);}


    /**
     * Parse vector3D from format "(x|y|z)"
     * @param vectorString
     * @return
     */
    public static Vector3D parse(String vectorString){
        vectorString = vectorString.trim();
        vectorString = vectorString.substring(1,vectorString.length()-1); //remove parenthesis

        StringTokenizer tokenizer = new StringTokenizer(vectorString,"|");
        if (tokenizer.countTokens()<3)
            Logger.logging(Level.error,"Not enough components for a vector "+vectorString);

        double x = Double.parseDouble(tokenizer.nextToken());
        double y = Double.parseDouble(tokenizer.nextToken());
        double z = Double.parseDouble(tokenizer.nextToken());

        return new Vector3D(x,y,z);
    }

    /**
     * Calculate normal  <b> n</b> = (<b> b</b>-<b>a</b>)x(<b>c</b>-<b>a</b>)
     * @param a mid point
     * @param b span point 1
     * @param c span point 2
     * @return
     */
    public static Vector3D calculateNormalToThreePoints(Vector3D a, Vector3D b, Vector3D c){
        return b.sub(a).cross(c.sub(a));
    }


    /**********************/
    /***   attributes   ***/
    /**********************/


    /*********************/
    /***  constructors ***/
    /*********************/

    public Vector3D(double x, double y, double z){
        super(x,y,z);
    }

    public Vector3D(List<Double> entries){ super(entries); }

    /************************/
    /****       getter    ***/
    /************************/

    public double getX(){ return getValue(0); }
    public double getY(){ return getValue(1); }
    public double getZ(){ return getValue(2); }


    /************************/
    /****       setter    ***/
    /************************/


    /******************************/
    /****     public methods    ***/
    /******************************/

    public Vector3D apply(Transformation transformation){
        if (transformation instanceof Rotation){
            Rotation rot = (Rotation) transformation;
            return this.linMap(rot.getRotationMatrix());
        }
        else if (transformation instanceof Translation){
            Translation trans = (Translation) transformation;
            return this.add(trans.getTranslation());
        }
        return null;
    }

    public Vector3D directionTo(Vector3D point){ return new Vector3D( super.directionTo(point).entries); }
    public Vector3D add(double x, double y, double z){ return new Vector3D( super.sum(new Vector(x,y,z)).entries); }
    public Vector3D normalize(){return new Vector3D( super.normalize().entries);}
    public Vector3D add(Vector3D shift){ return new Vector3D(super.sum(shift).entries); }
    public Vector3D sub(Vector3D shift){ return new Vector3D(this.add(shift.mul(-1)).entries); }
    public Vector3D mul(double scale){ return new Vector3D(super.mul(scale).entries); }
    public Vector3D neg(){return new Vector3D(super.negate().entries);}
    public Vector3D linMap(Matrix m){return new Vector3D( super.linMap(m).entries);}


    public Vector3D cross(Vector3D factor) {return new Vector3D(
            this.getY()*factor.getZ()-this.getZ()*factor.getY(),
            this.getZ()*factor.getX()-this.getX()*factor.getZ(),
            this.getX()*factor.getY()-this.getY()*factor.getX());}



    /******************************/
    /****     private methods   ***/
    /******************************/


    /******************************/
    /****     overrides         ***/
    /******************************/


    /******************************/
    /****     toString()        ***/
    /******************************/


    public String toPovString() {
        return "<"+getX()+","+getY()+","+getZ()+">";
    }
    public String toPovString2() {
        return "<"+getX()+","+getZ()+","+getY()+">";
    }


    @Override
    public Vector3D copy() {
        return new Vector3D(this.getX(), this.getY(), this.getZ());
    }

    @Override
    public Vector3D performTransformation(Transformation transformation) {
        if (transformation instanceof Translation) {//apply translation
            Translation translation = (Translation) transformation;
            return this.copy().add(translation.getTranslation());
        } else if (transformation instanceof Rotation) {//apply rotation
            Rotation rotation = (Rotation) transformation;
            Vector3D center = rotation.getCenter();
            return this.copy().add(center.neg()).linMap(rotation.getRotationMatrix()).add(center);
        }
        return this;
    }
}
