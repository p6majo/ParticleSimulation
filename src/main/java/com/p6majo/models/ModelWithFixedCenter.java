package com.p6majo.models;

import com.p6majo.physics.particlesimulation2D.Particle;
import com.p6majo.physics.particlesimulation2D.QuadTree;
import com.p6majo.physics.particlesimulation2D.Rect;

import java.util.function.Function;

/**
 * A container to keep all the parameters and initial data of the simulation
 * @author p6majo
 * @version 2020=03-11
 *
 */
public class ModelWithFixedCenter  extends Model{

    public double G = 1.;
    public double theta = 2.; //acccuracy parameter for the Barnes-Hutt algorithm
    public int particleNumber = 10000;

    public Function<Double,Double> gravityLaw =new Function<Double,Double>(){
        @Override
        public Double apply(Double distance) {
            return 1./distance;
        }
    };
    /*
     **********************
     ***   attributes   ***
     **********************
     */


    /*
     *********************
     ***  constructors ***
     *********************
     */

    public ModelWithFixedCenter(int width, int height,double G, double theta, double dt){
        super(width,height,G,theta,dt);

        for(int i = 0;i<particleNumber;i++){
            Particle p = newRndParticle();
            super.particles.add(p);
        }

        //add central star
        super.particles.add(new Particle(this.width/2,this.height/2,10000,true));

        //calculate velocities

        Rect boundary = new Rect(width/2,height/2,width/2,height/2);

        this.qt = new QuadTree(this,boundary, 1,null,0);
        for(int i = 0; i< particles.size(); i++)
            qt.insert(particles.get(i));

        //Calculate the mass distribution through the quad tree
        qt.ComputeMassDistribution();

        //setup Geschwindigkeit : v^2 = GM/r

        for (Particle particle : particles) {
            particle.setVel(qt.getMass(),qt.getCenterOfMass());
        }


    }

    /*
     ***********************
     ****       getter   ***
     ***********************
     */


    /*
     ************************
     ****       setter    ***
     ************************
     */

    /*
     *****************************
     ***     public methods    ***
     *****************************
     */

    /*
     ******************************
     ****     private methods   ***
     ******************************
     */

    private Particle newRndParticle(){
        double r = rnd.nextDouble()*this.height/2;
        double phi =  rnd.nextDouble()*2.*Math.PI;
        return new Particle(this.width/2+r*Math.cos(phi),this.height/2+r*Math.sin(phi));
    }

    /*
     ******************************
     ****     overrides         ***
     ******************************
     */

    /*
     ******************************
     ****     toString()        ***
     ******************************
     */

    @Override
    public String toString() {
        String out = "Dust disc with central star\n";
        out+="Number of particles: "+this.particles.size()+"\n";
        if (this.qt!=null) {
            out += "Total mass: " +qt.getMass()+"\n";
            out+= "centered at: "+qt.getCenterOfMass().toString()+"\n";
        }
        return out;
    }

}

