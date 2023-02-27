package com.p6majo.transformation;


import com.p6majo.linalg.Matrix;
import com.p6majo.linalg.Vector3D;
import com.p6majo.logger.Logger;
import com.p6majo.fieldextensions.Complex;
import com.p6majo.fieldextensions.Quaternion;

/**
 * The class Rotation
 *
 *  A rotation can be constructed in four different ways
 * <ul>
  * <li>by a rotation vector and rotation angle</li>
  *<li>by a rotation matrix</li>
  * <li>by Euler angles</li>
  * <li>by a quaternion</li>
 *</ul>
  *
  * This class provides tools to convert between the four representations
  *
  *
  * @author com.p6majo
  * @version 2020-05-22
 *
 * @version 2020-07-21
 * Embedding into <link> ContinuousTransformation</link>
 * It is possible to continuously deform the rotation from the identity to the final rotation by setting the state
 * between 0 and 1. So far, only the Matrix for the method getRotationMatrix is adjusted
 */
public class Rotation extends ContinuousTransformation {


    /*
     *********************************************
     ***           Statics              **********
     *********************************************
     */


    /**
     * returns a random rotation, which is seeded by a random rotation vector and random angle between zero and &pi;
     * @return
     */
    public static Rotation random(){
        return new Rotation(Vector3D.random(1,1,1),Math.random()*Math.PI);
    }


    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private Vector3D omega;
    private double theta;
    private Quaternion q;
    private Vector3D eulerAngles;
    private Vector3D eulerAnglesDegrees;
    private Matrix m;
    final private Vector3D center;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    /**
     * Construct the rotation from a rotation vector &omega; and the angle &theta;
     *
     * @param omega rotation vector, which is normalized automatically
     * @param theta rotation angle
     */
    public Rotation(Vector3D omega, double theta){
        super(theta);
        this.omega = omega.normalize();
        this.theta = theta;
        this.center = Vector3D.getZERO();
        calcQuaternionFromOmega();
        calcEulerAngle();
        calcMatrix();

    }

    /**
     * Construct the rotation from a rotation vector &omega; and the angle &theta;
     *
     * @param omega rotation vector, which is normalized automatically
     * @param theta rotation angle
     */
    public Rotation(Vector3D omega, double theta,Vector3D center){
        super(theta);
        this.omega = omega.normalize();
        this.theta = theta;
        this.center = center;

        calcQuaternionFromOmega();
        calcEulerAngle();
        calcMatrix();
    }

    /**
     * Construct a rotation from the quaternion representation.
     *
     * @param quatRotation
     */
    public Rotation(Quaternion quatRotation){
        super(1.);
        this.q = quatRotation;
        this.center = Vector3D.getZERO();

        calcOmegaAndThetaFromQuaternion();
        super.finalState = this.theta;
        calcEulerAngle();
        calcMatrix();
    }

    /**
     * Construct a rotation from euler angles around the origin
     *
     * @param eulerAngles
     */
    public Rotation(Vector3D eulerAngles){
        super(1.);
        this.eulerAngles = eulerAngles;
        this.center = Vector3D.getZERO();

        calcQuaternionFromEuler();
        calcOmegaAndThetaFromQuaternion();
        super.finalState = theta;
        calcMatrix();
    }

    /**
     * Construct a rotation from a rotation matrix
     * @param rotationMatrix
     */
    public Rotation(Matrix rotationMatrix){
        super(1.);
        this.m = rotationMatrix;
        this.center = Vector3D.getZERO();

        calcOmegaAndThetaFromMatrix();
        calcQuaternionFromOmega();
        calcEulerAngle();
        super.finalState = theta;
    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public Vector3D getRotationAxis(){
        return omega;
    }
    public double getAngle(){
        return theta;
    }
    public Quaternion getQuaternion(){return q;}

    /**
     * Returns the rotation matrix for a rotation between the identity (state = 0) and the rotation itself (state =1)
     * 
     * @return
     */
    public Matrix getRotationMatrix(){calcMatrix();return m;}
    public Vector3D getEulerAngles(){return eulerAngles;}
    public Vector3D getEulerAnglesDegrees(){return eulerAnglesDegrees;}
    public Vector3D getCenter(){return this.center;}



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

    /**
     * Construct the quaternion that corresponds to the rotation provided by &theta; and &omega;
     * The equivalence is provided by<br>
     * q=(cos(&theta;/2),&omega; sin(&theta;/2)) <br>
     * where &omega; has to be normalized
     */
    private void calcQuaternionFromOmega(){
        this.q = new Quaternion(Math.cos(theta/2),omega.normalize().mul(Math.sin(theta/2)));
    }

    private void calcQuaternionFromEuler(){
        double phi = this.eulerAngles.getX();
        double theta = this.eulerAngles.getY();
        double psi = this.eulerAngles.getZ();

        double sPhi= Math.sin(phi/2);
        double cPhi= Math.cos(phi/2);
        double sTheta = Math.sin(theta/2);
        double cTheta = Math.cos(theta/2);
        double sPsi = Math.sin(psi/2);
        double cPsi = Math.cos(psi/2);

        Complex a = new Complex(cPhi*cTheta*cPsi+sPhi*sTheta*sPsi,sPhi*cTheta*cPsi-cPhi*sTheta*sPsi);
        Complex b = new Complex(cPhi*sTheta*cPsi+sPhi*cTheta*sPsi,cPhi*cTheta*sPsi-sPhi*sTheta*cPsi);

        this.q = new Quaternion(a,b);

    }

    /**
     * This method assumes that the quaternion representation of the rotation
     * is established
     * <ul>
     * <li>&theta; = 2*arccos(Re(q))</li>
     * <li>&theta; = 2*arctan(|vec(q)|/Re(q))</li>
     * <li>&omega; = vec(q)/sin(&theta;/2)</li>
     * </ul>
     */
    private void calcOmegaAndThetaFromQuaternion(){
        //this.theta = 2.*Math.acos(q.re());
        this.theta = 2.*Math.atan2(q.getVector().length(),q.re()); //numerically more stable
        this.omega = q.getVector().mul(1./Math.sin(this.theta/2));
    };

    /**
     * This method assumes that the matrix representation R of the rotation
     * is established
     * <ul>
     * <li> cos&theta; = (tr R-1)/2</li>
     * <li> &omega; is obtained from the antisymmetric part of R </li>
     * </ul>
     */
    private void calcOmegaAndThetaFromMatrix(){

        double arg=(this.m.trace()-1.)/2.;

        if (Math.abs(arg)>1.) //numerical inaccuracies can make it larger than 1
            this.theta = Math.PI;
        else
            this.theta = Math.acos(arg);


        double omegax=0;
        double omegay=0;
        double omegaz=0;

        if (this.theta==0) {
            this.omega = Vector3D.getZERO();
            Logger.logging(Logger.Level.warning,"No rotation axis was determined for rotation angle of zero",this);
            return;
        }
        else if (this.theta!=Math.PI){
            omegax = 1./2/Math.sin(theta)*(m.getValue(2,1)-m.getValue(1,2));
            omegay = 1./2/Math.sin(theta)*(m.getValue(0,2)-m.getValue(2,0));
            omegaz = 1./2/Math.sin(theta)*(m.getValue(1,0)-m.getValue(0,1));
            this.omega = new Vector3D(omegax,omegay,omegaz);
            if (this.omega.length()>0.9)
                return;
            else //something went wrong, assume value of theta close to &pi;
                this.theta = Math.PI;
        }


        //special case for theta = pi
        double omegax2 = (m.getValue(0,0)+1.)/2.;
        double omegay2 = (m.getValue(1,1)+1.)/2;
        double omegaz2 = (m.getValue(2,2)+1.)/2;
        if (omegax2>0){
            omegax=Math.sqrt(omegax2);
            if (omegay2>0){
                omegay = Math.sqrt(omegay2);
                if (m.getValue(0,1)<0)
                    omegay*=-1;
            }
            if (omegaz2>0){
                omegaz=Math.sqrt(omegaz2);
                if (m.getValue(0,2)<0)
                    omegaz*=-1;
            }
        }
        else if (omegay2>0){ //only if omegax = 0
            omegay = Math.sqrt(omegay2);
            if (omegaz2>0){
                omegaz=Math.sqrt(omegaz2);
                if (m.getValue(1,2)<0)
                    omegaz*=-1;
            }
        }
        else //only if omegax=0 and omegay = 0
            omegaz = Math.sqrt(omegaz2);
        this.omega = new Vector3D(omegax,omegay,omegaz);


    };
    /**
     * This function should only be called, once &omega; and &theta; are calculated
     * It calculates the rotation matrix representation for this rotation.
     */
    private void calcMatrix(){
        Matrix generator = new Matrix(3,3,0.,-omega.getZ(),omega.getY(),omega.getZ(),0.,-omega.getX(),-omega.getY(),omega.getX(),0.);
        this.m  = Matrix.getIdentity(3).sum(generator.multiply(Math.sin(theta*state))).sum(generator.multiply(generator).multiply(1.-Math.cos(theta*state)));
    };

    /**
     * This  method assumes that the quaternion representation of the rotation is established
     */
    private void calcEulerAngle(){
        this.eulerAngles = q.getEulerAngles();
        this.eulerAnglesDegrees = eulerAngles.mul(180./Math.PI);
    };

    /*
     ***********************************************
     ***           Overrides            ************
     ***********************************************
     */

    @Override
    public void setState(double state) {
        super.setState(state);
    }
    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */


    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("Rotation:\n")
                .append("rotation axis: "+getRotationAxis()+" and angle "+getAngle()+"\n")
                .append("quaternion representation: "+getQuaternion().toString()+"\n")
                .append("matrix represenation: "+getRotationMatrix().toString()+"\n")
                .append("Euler angles: "+getEulerAngles().toString()+"\n")
        ;
        return out.toString();
    }



}
