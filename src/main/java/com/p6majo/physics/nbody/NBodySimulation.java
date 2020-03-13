package com.p6majo.physics.nbody;


import com.p6majo.utils.Boundary;
import com.p6majo.utils.Distance;
import com.p6majo.utils.Vector;

/**
 * The class NBodySimulation
 *
 * @author p6majo
 * @version 2019-03-05
 */
public class NBodySimulation extends Simulation {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */


    public double g = -500.; //gravity



    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public NBodySimulation(int dim, int n, double deltaT) {
        super(dim,n,deltaT);
        Boundary boundary = new Boundary(3,1000,1000,1000);
        boundary.setBoundaryConditions(Boundary.BoundaryConditions.reflecting);
        super.setBoundary(boundary);
    }

    @Override
    public Distance[][] getDistances() {
        return new Distance[0][];
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



    private void unmarkAllParticles(){
        if (particles==null){
            System.err.println("Set boundary conditions first!");
            System.exit(0);
        }
        for (Particle particle : particles) {
            particle.setMarked(false);
            particle.setApproaching(false);
        }

    }

    private void checkForCollisions(){
        unmarkAllParticles();
        for (int i = 0; i < n ; i++) {
            double r1 = particles[i].getRadius();
            Vector pos1 = particles[i].getPosition();
            for (int j = i+1; j < n; j++) {
                double r2 = particles[j].getRadius();
                Vector pos2 = particles[j].getPosition();
                double d = (r1+r2);
                double d2 = d*d;
                Vector diff = pos1.add(pos2.mul(-1));
                double diff2 = diff.dot(diff);


                if (diff2<d2) {
                    particles[i].setMarked(true);
                    particles[j].setMarked(true);
                    collide(particles[i],particles[j]);
                }
                else if (diff2<2*d2){
                    particles[i].setApproaching(true);
                    particles[j].setApproaching(true);
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
    private void collide(Particle p, Particle q){

        if(checkForFutureCollision(p,q)) {
            double b = calculateImpactParameter(p, q);
            double angle = calculateAngle(p, q, b);
            Vector vcom = calculateCenterOfMassVelocity(p, q);
            p.galileiTransform(vcom);
            q.galileiTransform(vcom);

            double sign = calculateSignOfRotation(p, q);

            if (dim == 2) {
                p.rotateMomentum2d(sign * angle);
                q.rotateMomentum2d(sign * angle);
            } else if (dim == 3) {
                Vector dx = p.getPosition().sub(q.getPosition());
                Vector axis = p.getMomentum().crossproduct(dx);
                double norm = axis.dot(axis);
                if (norm>0)
                    axis=axis.normalize();
                else{
                    //create random orthogonal axis
                    //head on collision any orthogonal vector to p will do
                    if (p.getMomentum().get(0)==0.)
                        axis = new Vector(3,1,0,0);
                    else if (p.getMomentum().get(1)==0.)
                        axis = new Vector(3,0,1,0);
                    else if (p.getMomentum().get(2)==0.)
                        axis = new Vector(3,0,0,1);
                    else {
                        axis = new Vector(3,p.getMomentum().get(1),-p.getMomentum().get(2),0);
                        axis =axis.normalize();
                    }
                }

                //System.out.println("before: " + p);
                p.rotateMomentum3d(sign * angle, axis);
                // System.out.println("after: " + p);
                q.rotateMomentum3d(sign * angle, axis);
            }

            p.galileiTransform(vcom.mul(-1.));
            q.galileiTransform(vcom.mul(-1.));
        }
    }

    /*
     ***********************************************
     ***           Overrides            ************
     ***********************************************
     */

    @Override
    protected void createParticles(){
        particles = new Particle[n];

        for (int i = 0; i < n; i++) {
            double mass;
            mass = Math.random()*40+2;

            double radius = mass;

            Vector position = new Vector(dim);
            Vector momentum = new Vector(dim);



            for (int j = 0; j < this.dim; j++) {
                position.setValue(j, this.boundary.getSize(j)+Math.random() * this.boundary.getSize(dim+j));
                momentum.setValue(j, 2000 - Math.random() * 4000);
            }


            this.particles[i]=new Particle(i,mass,radius,position,momentum,this);
        }
    }

    @Override
    public Vector getForceField(int particleIndex){
        return new Vector(3,0,g,0);
    }

    @Override
    protected void timestep(){
        checkForCollisions();
        for (int i = 0; i < n; i++) {
            particles[i].act();
        }
    }

    @Override
    public void setNumber(int n) {

    }
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
