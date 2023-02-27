package com.p6majo.physics.thomson;


import com.p6majo.linalg.Vector;
import com.p6majo.physics.nbody.Particle;
import com.p6majo.physics.nbody.Simulation;
import com.p6majo.utils.Boundary;
import com.p6majo.utils.Distance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.p6majo.physics.thomson.ThomsonSimulation.ForceLaw.coulomb3d;

/**
 * The class NBodySimulation
 *
 * @author p6majo
 * @version 2019-03-05
 */
public class ThomsonSimulation extends Simulation {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    public enum ForceLaw {coulomb3d,artificial2d}

    private ForceLaw forceLaw=coulomb3d;

    private double r = 1000.;  //radius of sphere
    private double k = 10000000.; //coupling constant
    private double beta = 0.05; //friction parameter

    private Distance[][] distances;
    private double potentialEnergy = 0.;

    //for edges
    private double smallestLength=0.;
    private double largestLength = 1.;



    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public ThomsonSimulation(int dim, int n, double deltaT) {
        super(dim,n,deltaT);

        Boundary boundary = new Boundary(3,-r,-r,-r,r,r,r);
        boundary.setBoundaryConditions(Boundary.BoundaryConditions.none);
        super.setBoundary(boundary);

        distances =new Distance[n][n];//only the upper triangular part of the matrix will be used;
        for (int i = 0; i < n; i++)
            for (int j = i+1; j < n; j++)
                distances[i][j] = new Distance();
    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public double getPotentialEnergy(){
        return potentialEnergy;
    }
    public Distance[][] getDistances(){
        return this.distances;
    }


    public ForceLaw getForceLaw(){
        return this.forceLaw;
    }
    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */

    public void setForceLaw(ForceLaw forceLaw){
        this.forceLaw = forceLaw;
    }


    public void setSmallestLength(double percent){
        this.smallestLength=percent;
    }

    public void setLargestLength(double percent)
    {
        this.largestLength = percent;
    }


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
        switch(forceLaw){
            case coulomb3d:
                for (int i = 0; i < particles.length; i++)
                    for (int j = i+1; j < particles.length; j++)
                        energy+=1./distances[i][j].distance;

                return r*energy;
            case artificial2d:
                for (int i = 0; i < particles.length; i++)
                    for (int j = i+1; j < particles.length; j++) {
                        energy += -Math.log(Math.sin(distances[i][j].distance/2))+1./12*Math.cos(distances[i][j].distance);
                    }

                return r*energy;
        }
       return 0.;
    }


    protected void createParticles(){
        createParticles(0,this.n,null);
    }

    /**
     * create n random particles at the surface of the sphere
     * If an array of particles is provided, the new particles are created into the given array otherwise
     * the particle array of the class attributes is populated
     */
    protected void createParticles(int start, int end, Particle[] particles){
        if (particles==null)
            this.particles = new Particle[n];

        for (int i = start; i < end; i++) {
            //make sure that particles sit on sphere
            double x = -r+Math.random()*2*r;
            double max= Math.sqrt(r*r-x*x);
            double y = -max+2*Math.random()*max;
            double z = Math.sqrt(r*r-x*x-y*y);
            if (Math.random()<0.5)
               z*=-1.;

            if (particles==null)
                this.particles[i] = new Particle(i,1.,50,new Vector(x,y,z),new Vector(0.,0.,0.),this);
            else
                particles[i] = new Particle(i,1.,50,new Vector(x,y,z),new Vector(0.,0.,0.),this);
        }
    }

    private void calculateDistances(){
        switch(forceLaw){
            case artificial2d:
                for (int i = 0; i < particles.length; i++)
                    for (int j = i+1; j < particles.length ; j++) {
                        Vector delta = particles[i].getPosition().add(particles[j].getPosition().mul(-1));
                        double arg = Math.sqrt(delta.dot(delta))/2./r;
                        if (arg<1) //avoid nan for distances slightly greater than 2r
                            distances[i][j].distance = 2.*Math.asin(arg);
                        else
                            distances[i][j].distance=Math.PI;

                        double d = distances[i][j].distance/Math.PI;
                        if (d>=smallestLength && d<=largestLength)
                            distances[i][j].visible=true;
                        else
                            distances[i][j].visible=false;
                    }
                break;
            case coulomb3d:
                for (int i = 0; i < particles.length; i++)
                    for (int j = i+1; j < particles.length ; j++) {
                        Vector delta = particles[i].getPosition().add(particles[j].getPosition().mul(-1));
                        distances[i][j].distance = Math.sqrt(delta.dot(delta));

                        double d = distances[i][j].distance/2./r;
                        if (d>=smallestLength && d<=largestLength)
                            distances[i][j].visible=true;
                        else
                            distances[i][j].visible=false;
                    }
                break;
        }
    }

    /*
     ***********************************************
     ***           Overrides            ************
     ***********************************************
     */

    /**
     * dynamically change the number of particles
     * @param n
     */
    @Override
    public void setNumber(int n) {
        boolean interrupted = false;
        Particle[] np = new Particle[n];
        if (n>this.n){
            for (int i = 0; i < this.n; i++)
                np[i]=particles[i];

            if (running) {
                this.stop();
                interrupted = true;
            }
            createParticles(this.n,n,np);
        }
        else if (n<this.n){
            for (int i = 0; i < n; i++)
                np[i]=particles[i];

            if (running) {
                interrupted = true;
                this.stop();
            }
        }
        distances = new Distance[n][n];
        for (int i = 0; i < n; i++)
            for (int j = i+1; j < n; j++)
                distances[i][j] = new Distance();
        this.particles = np;
        this.n = n;

        if (interrupted)
            start();
    }

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
    public double getOrderParameter() {
        return this.getPotentialEnergy();
    }

    @Override
    public String[] getInfo() {
        List<Double> distanceList = new ArrayList<>();

        int count=0;
        for (int i = 0; i < particles.length; i++)
            for (int j = i+1; j < particles.length; j++)
                if (distances[i][j].visible)
                    distanceList.add(distances[i][j].distance);


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
        fx = -m*v2*p.getPosition().getValue(0)/r/r;
        fy = -m*v2*p.getPosition().getValue(1)/r/r;
        fz = -m*v2*p.getPosition().getValue(2)/r/r;
        Vector force = new Vector(fx,fy,fz);


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
        Vector force = new Vector(0.,0.,0.);
        Particle pi = particles[particleIndex];


        switch(forceLaw){
            case coulomb3d:
                for (int l = 0; l <particleIndex; l++){
                    Particle pl = particles[l];
                    Vector diff = pl.getPosition().add(pi.getPosition().mul(-1));
                    double d = distances[l][particleIndex].distance;
                    force = force.add(diff.mul(k/d/d/d));

                }
                for (int l = particleIndex+1; l < particles.length; l++) {
                    Particle pl = particles[l];
                    Vector diff = pl.getPosition().add(pi.getPosition().mul(-1));
                    double d = distances[particleIndex][l].distance;
                    force = force.add(diff.mul(k/d/d/d));
                }

                //calculate the normal component of the gradient
                Vector normal = pi.getPosition().mul(-pi.getPosition().dot(force)/r/r);

                force = force.add(normal).mul(-1);
                break;
            case artificial2d:
                for (int l = 0; l <particles.length; l++){
                    if (l!=particleIndex){
                        double d =0;
                        if (l<particleIndex)
                            d=distances[l][particleIndex].distance;
                        else
                            d=distances[particleIndex][l].distance;

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
                break;
        }

        return force;
    }

    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    @Override
    public String toString() {
        return "Simulation of "+this.n+" charges under the action of "+this.forceLaw;
    }



}
