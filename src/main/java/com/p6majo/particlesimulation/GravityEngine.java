package com.p6majo.particlesimulation;

import java.util.ArrayList;
import java.util.List;

/**
 * The physics engine that contains the dynamics of the system
 *
 *
 * @author p6majo
 * @version 2020-03-11
 *
 */
public class GravityEngine {

    /*
     **********************
     ***   attributes   ***
     **********************
     */

    private Model model;
    private QuadTree qt;
    private Rect boundary;

    private List<Particle> particles ;
    private List<BarnesHutCenter> barnesHutCenters;
    private double time;
    private int count;


    /*
     *********************
     ***  constructors ***
     *********************
     */

    public GravityEngine(Model model){
        this.model = model;
        this.particles = model.getParticles();
        this.time = 0;
        int width = model.getWidth();
        int height = model.getHeight();
        this.boundary = new Rect(width/2,height/2,width/2,height/2);
       // testing();
    }
    /*
     ***********************
     ****       getter   ***
     ***********************
     */


    public List<Particle> getParticles() {
        return particles;
    }


    public List<BarnesHutCenter> getBarnesHutCenters(){
        return this.barnesHutCenters;
    }

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


    public void timestep(){
       // count++;
       // long start=0;
      //  long stop=0;
      //  if (count%10==0)
       //     start = System.currentTimeMillis();

        double dt = model.dt;
        time +=dt;
        //strukturiere Teilchen im Quadtree
         this.qt = new QuadTree(model,boundary, 1);
        for(int i = 0; i< particles.size(); i++)
            qt.insert(this.particles.get(i));

        //Calculate the mass distribution through the quad tree
        this.qt.ComputeMassDistribution();

        //calculate the acceleration for the first particle
        particles.get(0).setAcceleration(qt.calculateAcceleration(particles.get(0),model.getTheta(),true));
        this.barnesHutCenters = qt.getBarnesHutCenters();

        for (int i = 1; i < particles.size(); i++) {
            particles.get(i).setAcceleration(qt.calculateAcceleration(particles.get(i),model.getTheta(),false));
        }
        particles.stream().forEach(x->x.update(dt));

        //if (count%10==0)
          //  System.out.println((System.currentTimeMillis()-start)/10 + " ");
    }

    /*
     ******************************
     ****     private methods   ***
     ******************************
     */


    private void testing(){
        int width = model.getWidth();
        int height = model.getHeight();

        this.boundary = new Rect(width/2,height/2,width/2,height/2);

        //strukturiere Teilchen im Quadtree
        this.qt = new QuadTree(model, boundary,1);
        for(int i = 0; i< particles.size(); i++)
            qt.insert(particles.get(i));


        System.out.println(qt.toString());
        Rect rect = new Rect(50.,50.,50.,50.);
        ArrayList<Particle> result = qt.query(rect);
        System.out.println("Suche im Rechteck " + rect.toString());
        for (Particle particle : result) {
            System.out.println(particle.toString());
        }

        this.qt.ComputeMassDistribution();
        System.out.println("Das Massenzentrum sollte in ungefaehr Bildschirmmitte sein fuer zufaellig verteilte Massen. Tatsaechlich liegt es bei: " + qt.getCenterOfMass().toString() + "\n");
        System.out.println("Die Gesamtmasse liegt bei: " + qt.getMass());
        // System.exit(0);
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
    public String toString(){
        return "Gravity engine supplied with the model "+model.toString();
    }

}
