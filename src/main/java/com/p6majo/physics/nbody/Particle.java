package com.p6majo.physics.nbody;


import com.p6majo.utils.Boundary;
import com.p6majo.utils.Vector;

/**
 * The class Particle, the physical entity in an N-Body-Simulation
 *
 * @author p6majo
 * @version 2019-03-05
 */
public class Particle {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private double mass;
    private double radius;
    private Vector position;
    private Vector momentum;
    private int index;

    private Vector force;
    private Simulation simulation;

    private boolean marked;
    private boolean approaching;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    /**
     * Constructor with all required properties of a particle. The index is used to refresh the position of the particle
     * within the array of all particles to make sure that the interaction is not overcounting. Each particle pair
     * interacts only once.
     *
     * @param index
     * @param m
     * @param r
     * @param x
     * @param p
     */
    public Particle(int index, double m, double r, Vector x, Vector p, Simulation sim){
        this.index=index;
        this.mass = m;
        this.radius = r;
        this.position = x;
        this.momentum = p;
        this.simulation = sim;

    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public Vector getPosition() {
        return position;
    }

    public Vector getMomentum() {
        return momentum;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public boolean isMarked() { return marked; }

    public boolean isApproaching() { return approaching; }

    public double getv2(){ return momentum.dot(momentum)/mass/mass; }


    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */

    /**
     * the momentum can be changed dynamically via interaction with other particles
     * @return
     */
    public void setMomentum(Vector p){
        this.momentum = p;
    }

    public void addForce(Vector f){
        this.force = this.force.add(f);
    }

    public void setMarked(boolean marked) { this.marked = marked; }

    public void setApproaching(boolean approaching) { this.approaching = approaching; }
    /*
     ***********************************************
     ***           Public methods       ************
     ***********************************************
     */

    /**
     * Time evolution of the particle
     * The interaction with all other particles is
     */
    public void act(){
        this.force  = new Vector(this.simulation.dim);

        Vector gravity = simulation.getForceField(this.index);
        Vector interaction = simulation.getInteraction(this.index);

        if (gravity!=null)
            this.force = this.force.add(gravity);
        if (interaction!=null) {
           this.force = this.force.add(interaction);
            //System.out.println(simulation.getT() + " " + interaction);
        }

        momentum = momentum.add(force.mul(this.simulation.deltaT));
        position = position.add(momentum.mul(1./this.mass*this.simulation.deltaT));

      //  applyBoundaryConditions();
    }

    /**
     * Perform a Galileian transformation on the momentum of the particle. The position of the particle remains unchanged <br>
     *     <b>p</b><sub>new</sub>=m (<b>v</b>-<b>u</b>)
     * @param u the velocity parameter of the Galileian transformation
     */
    public void galileiTransform(Vector u){
        this.setMomentum(this.getMomentum().mul(1./this.getMass()).sub(u).mul(this.getMass()));
    }

    /**
     * Rotate the momentum by a given angle and axis of rotation.
     * <br>
     *     <br>
     *         The following calculation is applied:<br>
     *             p<sub>new</sub> =p<sub>old</sub>cos(&phi;)+(a&#10799;p<sub>old</sub>)sin(&phi;)
     * @param angle
     * @param axis
     */
    public void rotateMomentum3d(double angle, Vector axis){
        Vector pNew = this.getMomentum().mul(Math.cos(angle)).add(axis.crossproduct(this.getMomentum()).mul(Math.sin(angle)));
        this.setMomentum(pNew);
    }

    /**
     * Perform a simple two dimensional rotation of the momentum.
     * The sign of the angle determines, whether the rotation is
     * performed in clockwise or counterclockwise orientation
     * @param angle
     */
    public void rotateMomentum2d(double angle){
        Vector p = this.getMomentum();
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        Vector pNew = new Vector(2,p.get(0)*c-p.get(1)*s,p.get(0)*s+p.get(1)*c);
        this.setMomentum(pNew);
    }

    /*
     ***********************************************
     ***           Private methods      ************
     ***********************************************
     */

    private void applyBoundaryConditions(){
        Boundary bc = simulation.getBoundary();
        switch(bc.getBc()){
            case reflecting:
                for (int i = 0; i < simulation.dim; i++) {
                    if (position.get(i)<bc.getMinSize(i)+radius)
                        momentum.setValue(i,Math.abs(momentum.get(i)));
                    if (position.get(i)>bc.getMaxSize(i)-radius)
                        momentum.setValue(i,Math.abs(momentum.get(i))*(-1));
                }
                break;
            case periodic:
                for (int i = 0; i < simulation.dim; i++) {
                    if (position.get(i)<0)
                        position.setValue(i,bc.getSize(i));
                    if (position.get(i)>bc.getSize(i))
                        position.setValue(i,0);
                }
                break;
            case none:
                break;
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
        return this.index+": "+position+" "+momentum;
    }



}
