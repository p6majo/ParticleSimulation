package com.p6majo.dynamicpolyhedra;

import com.p6majo.linalg.Vector;
import com.p6majo.linalg.Vector;
import com.p6majo.octtree.Cuboid;
import com.p6majo.octtree.Particle;

import java.util.List;

/**
 * The class LagrangeModel is a class that deals with a n objects that are controlled
 * by a Lagrangian.
 *
 *
 *
 * @author p6majo
 * @version 2021-02-04
 */
public abstract class LagrangeModel {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    protected List<Particle> particles;
    protected Cuboid box;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public LagrangeModel(Cuboid box){
        this.box = box;
    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    /**
     * Return the acceleration for the particle with index i
     * @param i index
     * @return
     */
    public abstract Vector getAcceleration(int i);

    /**
     * The default acceleration with rotation just returns the regular acceleration
     * without rotation.<br>
     *
     * To implement rotation, the method has to be overridden.
     * @param i
     * @param center
     * @return
     */
    public Vector getAccelerationWithRotation(int i, Vector center){
        return getAcceleration(i);
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

    public abstract void setupInitialData(List<Particle> particles);

    public void evolve(double timeStep){
        Vector center = Vector.getZero(3);
        for (Particle particle : particles) {
            center = center.add(particle.getPosition());
        }
        center =center.mul(1./particles.size());

        checkForCollisions();

        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).setAcceleration(getAccelerationWithRotation(i,center));
            particles.get(i).update(timeStep);
        }
    }

    /*
     ***********************************************
     ***           Private methods      ************
     ***********************************************
     */

    private void checkForCollisions(){
        int n = particles.size();
        for (int i = 0; i < n ; i++) {
            Particle particleI = particles.get(i);
            double r1 = particleI.getRadius();
            Vector pos1 = particleI.getPosition();
            for (int j = i+1; j < n; j++) {
                Particle particleJ = particles.get(j);
                double r2 = particleJ.getRadius();
                Vector pos2 = particleJ.getPosition();
                double d = (r1+r2);
                double d2 = d*d;
                Vector diff = pos1.add(pos2.mul(-1));
                double diff2 = diff.dot(diff);


                if (diff2<d2) {
                    collide(particleI,particleJ);
                }
                else if (diff2<2*d2){
                    particleI.setApproaching(true);
                    particleJ.setApproaching(true);
                }
            }
        }
    }

    /**
     * The impact parameter b is the shortest approach that the particles p and q will have along their current
     * trajectories. The minimum of the distance <b>b</b>(t)=<b>x</b><sub>p</sub>(t)-<b>x</b><sub>q</sub>(t) is given by<br>
     *     <table>
     *       <tbody>
     *         <tr>
     *           <td rowspan="2">b<sup>2</sup><sub>min</sub>=(<b>x</b><sub>p0</sub>-<b>x</b><sub>q0</sub>)<sup>2</sup>-</td>
     *           <td style="border-bottom:solid 1px">[(<b>x</b><sub>p0</sub>-<b>x</b><sub>q0</sub>)<span>&#183;</span>(<b>v</b><sub>p</sub>-<b>v</b><sub>q</sub>)]<sup>2</sup></td>
     *           </tr>
     *           <tr>
     *             <td>(<b>v</b><sub>p</sub>-<b>v</b><sub>q</sub>)<sup>2</sup></td>
     *           </tr>
     *       </tbody>
     *    </table>
     * @param p particle one
     * @param q particle two
     * @return b<sub>min</sub>
     */
    private double calculateImpactParameter(Particle p, Particle q){
        Vector xp = p.getPosition();
        Vector xq = q.getPosition();
        Vector vp = p.getMomentum().mul(1./p.getMass());
        Vector vq = q.getMomentum().mul(1./q.getMass());

        Vector dx = xp.sub(xq);
        Vector dv = vp.sub(vq);
        double dxdv = dx.dot(dv);
        double dv2 = dv.dot(dv);
        if (dv2>0)
            return Math.sqrt(Math.abs(dx.dot(dx)-dxdv*dxdv/dv2));
        else
            return 0;
    }

    /**
     * Calculate the angle &lambda; by which the momenta are rotated in the center of mass frame <br>
     *     &lambda; = 2 arccos(b/(r<sub>p</sub>+r<sub>q</sub>))
     * @param p particle one
     * @param q particle two
     * @param b impact parameter
     * @return &lambda;
     */
    private double calculateAngle(Particle p, Particle q, double b){
        return 2.*Math.acos(b/(p.getRadius()+q.getRadius()));
    }

    /**
     * Calculate the velocity of the center of mass frame<br>
     *     <b>v</b><sub>com</sub> = (<b>p</b><sub>p</sub>+<b>q</b><sub>q</sub>)/(m<sub>p</sub>+m<sub>q</sub>)
     * @param p particle one
     * @param q particle two
     * @return <b>v</b><sub>com</sub>
     */
    private Vector calculateCenterOfMassVelocity(Particle p, Particle q){
        return p.getMomentum().add(q.getMomentum()).mul(1./(p.getMass()+q.getMass()));
    }


    /**
     * Determine the sign of the rotation.
     * @param p
     * @param q
     * @return +1 for counterclockwise and -1 for clockwise rotation, 0 for no rotation
     */
    private double calculateSignOfRotation(Particle p, Particle q){
        Vector xp = p.getPosition();
        Vector xq = q.getPosition();
        Vector dx = xp.sub(xq);
        double s = p.getMomentum().dot(dx);
        if (s!=0)
            return s/Math.abs(s);
        else
            return 0;
    }

    /**
     * Returns true, if the collision takes place in the future.
     * This check ensures, that a previously calculated collision does not get inverted by a new collision calculation,
     * which is triggered by the remaining overlap of the two objects.
     *
     * @param p
     * @param q
     * @return
     */
    private boolean checkForFutureCollision(Particle p, Particle q){
        double t = p.getPosition().sub(q.getPosition()).dot(q.getMomentum().sub(p.getMomentum()));
        if (t>0)
            return true;
        else
            return false;
    }

    /**
     * The collision results in a change of momentum for both particles
     * This change is calculated with the following steps:<br>
     *     1.) The impact parameter b<br>
     *     2.) The momentum of each particles is transformed into the center of mass frame<br>
     *     3.) The momenta is rotated according to the impact parameter<br>
     *     4.) The momenta are transformed back into the lab frame<br>
     *     5.) The particles are set with their new momentum
     *
     * @param p
     * @param q
     */
    private void collide(Particle p, Particle q) {
        if (checkForFutureCollision(p, q)) {
            double b = calculateImpactParameter(p, q);
            double angle = calculateAngle(p, q, b);
            Vector vcom = calculateCenterOfMassVelocity(p, q);
            p.galileiTransform(vcom);
            q.galileiTransform(vcom);

            double sign = calculateSignOfRotation(p, q);


            Vector dx = p.getPosition().sub(q.getPosition());
            Vector axis = p.getMomentum().toVector3D().cross(dx.toVector3D());
            double norm = axis.dot(axis);
            if (norm > 0)
                axis = axis.normalize();
            else {
                //create random orthogonal axis
                //head on collision any orthogonal vector to p will do
                if (p.getMomentum().getValue(0) == 0.)
                    axis = new Vector(1., 0., 0.);
                else if (p.getMomentum().getValue(1) == 0.)
                    axis = new Vector(0., 1., 0.);
                else if (p.getMomentum().getValue(2) == 0.)
                    axis = new Vector(0., 0., 1.);
                else {
                    axis = new Vector(p.getMomentum().getValue(1), -p.getMomentum().getValue(2), 0.);
                    axis = axis.normalize();
                }
            }

            //System.out.println("before: " + p);
            p.rotateMomentum3d(sign * angle, axis);
            // System.out.println("after: " + p);
            q.rotateMomentum3d(sign * angle, axis);


            p.galileiTransform(vcom.mul(-1.));
            q.galileiTransform(vcom.mul(-1.));

        }
    }

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
        return super.toString();
    }


}
