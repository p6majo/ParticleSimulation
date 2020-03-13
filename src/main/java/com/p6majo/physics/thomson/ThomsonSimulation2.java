package com.p6majo.physics.thomson;

import com.p6majo.physics.nbody.Particle;
import com.p6majo.physics.nbody.Simulation;
import com.p6majo.utils.Boundary;
import com.p6majo.utils.Distance;
import com.p6majo.utils.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * The class NBodySimulation
 *
 * @author p6majo
 * @version 2019-03-05
 */
public class ThomsonSimulation2 extends Simulation {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private double r = 1000.;  //radius of sphere
    private double k = 10000000.; //coupling constant
    private double beta = 0.5; //friction parameter

    private final double[][] distances;

    private double potentialEnergy = 0.;


    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public ThomsonSimulation2(int dim, int n, double deltaT) {
        super(dim,n,deltaT);

        Boundary boundary = new Boundary(3,-r,-r,-r,r,r,r);
        boundary.setBoundaryConditions(Boundary.BoundaryConditions.none);
        super.setBoundary(boundary);

        distances =new double[n][n];//only the upper triangular part of the matrix will be used;
    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public double getPotentialEnergy(){
        return potentialEnergy;
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


    private double calculatePotentialEnergy(){
        double energy = 0.;

        for (int i = 0; i < particles.length; i++)
            for (int j = i+1; j < particles.length; j++) {
                energy += -Math.log(Math.sin(distances[i][j]/2))+1./12*Math.cos(distances[i][j]);
            }

        return r*energy;
    }

    /**
     * create n random particles at the surface of the sphere
     */
    protected void createParticles(){
        particles = new Particle[n];

        for (int i = 0; i < n; i++) {
            //make sure that particles sit on sphere
            double x = -r+Math.random()*2*r;
            double max= Math.sqrt(r*r-x*x);
            double y = -max+2*Math.random()*max;
            double z = Math.sqrt(r*r-x*x-y*y);
            if (Math.random()<0.5)
               z*=-1.;

            particles[i] = new Particle(i,1.,50,new Vector(3,x,y,z),new Vector(3,0,0,0),this);
            System.out.println(particles[i].toString());
        }
    }

    private void calculateDistances(){
        for (int i = 0; i < particles.length; i++)
            for (int j = i+1; j < particles.length ; j++) {
                Vector delta = particles[i].getPosition().add(particles[j].getPosition().mul(-1));
                double arg = Math.sqrt(delta.dot(delta))/2./r;
                if (arg<1) //avoid nan for distances slightly greater than 2r
                    distances[i][j] = 2.*Math.asin(arg);
                else
                    distances[i][j]=Math.PI;
            }
    }

    /*
     ***********************************************
     ***           Overrides            ************
     ***********************************************
     */

    /**
     * this method is the heart of the simulation
     * first, the new cubed distance between all particles is calculated, which is necessary to speed up the calculation of the interactions
     * second, the timestep for each particle is performed
     */
    @Override
    protected void timestep() {
        calculateDistances();
        this.potentialEnergy=calculatePotentialEnergy(); //update potential energy of the configuration

        //move particles
        for (Particle particle : particles) {
            particle.act();
        }
    }

    @Override
    public void setNumber(int n) {

    }

    @Override
    public double getOrderParameter() {
        return this.getPotentialEnergy();
    }

    @Override
    public Distance[][] getDistances() {
        return new Distance[0][];
    }

    @Override
    public String[] getInfo() {
        List<Double> distanceList = new ArrayList<>();

        int count=0;
        for (int i = 0; i < particles.length; i++)
            for (int j = i+1; j < particles.length; j++)
                distanceList.add(distances[i][j]);

            Collections.sort(distanceList);

        String[] info =new String[distanceList.size()];
        int c = 0;
        for (Double aDouble : distanceList)
            info[c++]=aDouble+"";

        return info;
    }

    /**
     * The centripetal force is implemented here, which keeps the particles on the sphere
     * @param particleIndex
     * @return
     */
    @Override
    public Vector getForceField(int particleIndex) {

        double fx=0.,fy=0.,fz=0.;
        Particle p = this.particles[particleIndex];

        //centripetal force to keep object on the surface of the sphere
        double m = p.getMass();
        double v2 = p.getv2();
        fx = -m*v2*p.getPosition().get(0)/r/r;
        fy = -m*v2*p.getPosition().get(1)/r/r;
        fz = -m*v2*p.getPosition().get(2)/r/r;
        Vector force = new Vector(3,fx,fy,fz);


        //add friction
        Vector friction = p.getMomentum().mul(-1./p.getMass()*beta);

        return force.add(friction);
    }

    /**
     * Calculate the force on the particle with particleIndex from the interaction potential.
     * <br><br>
     * <b>F</b><sub>i</sub> = -<b> &nabla;</b><sub>i</sub> U <br><br>
     *
     * The potential is the sum from all interactions. For the sake of efficiency, each pair is listed only once.
     * The sign for the contribution to the force then depends on the order of the indices of the pair of particles<br><br>
     *
     * U(<b>x</b><sub>k</sub>)= k &Sigma;<sub>i < j</sub> |<b>x</b><sub>i</sub>-<b>x</b><sub>j</sub>|<sup>-1</sup><br><br>
     *
     * <b>F</b><sub>i</sub>=k&Sigma;<sub>l < i</sub>(<b>x</b><sub>l</sub>-<b>x</b><sub>i</sub> )|<b>x</b><sub>l</sub>-<b>x</b><sub>i</sub>|<sup>-3</sup>+k&Sigma;<sub>l > i</sub>(<b>x</b><sub>i</sub>-<b>x</b><sub>l</sub> )|<b>x</b><sub>i</sub>-<b>x</b><sub>l</sub>|<sup>-3</sup>
     *
     * @param particleIndex
     * @return
     */
    @Override
    public Vector getInteraction(int particleIndex) {
        Vector force = new Vector(3,0,0,0);
        Particle pi = particles[particleIndex];

        for (int l = 0; l <particles.length; l++){
            if (l!=particleIndex){
                double d =0;
                if (l<particleIndex)
                    d=distances[l][particleIndex];
                else
                    d=distances[particleIndex][l];

               // double f = k/r/r/2*Math.sin(d)/(1.-Math.cos(d))*(7./6-1./6*Math.cos(d))*(1.+11./360*(1.-Math.cos(d*d)));//cube for n=8
              //  double f = k/r/r*(1./d-1./(d-2.*Math.PI));//cube for n=8
               double f = Math.abs(k/r/r/Math.sin(d/2));//cube for n=8
               // double f = Math.abs(k/r/r/Math.tan(d/2));//doesn't give a cube for n = 8

                Vector dir = pi.getPosition().add(particles[l].getPosition().mul(-1.));
                Vector rVec = pi.getPosition();
                Vector dirProjected = dir.add(rVec.mul(-1.*dir.dot(rVec)/r/r));
                force = force.add(dirProjected.normalize().mul(f));
            }
        }

        return force;
    }

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
