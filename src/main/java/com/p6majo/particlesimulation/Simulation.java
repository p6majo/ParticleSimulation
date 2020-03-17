package com.p6majo.particlesimulation;


import com.p6majo.codingtrain.CodingTrainGui;
import com.p6majo.models.Model;
import com.p6majo.models.ModelWithTwoGalaxies;

import java.awt.*;
import java.util.List;

/**
 * @author AlexK
 * @version 2020-01-18
 */
public class Simulation extends CodingTrainGui {



    /*
     **********************
     ***   attributes   ***
     **********************
     */


    private Model model;
    private GravityEngine engine;

    /*
     *********************
     ***  constructors ***
     *********************
     */

    /**
     * Create a CodingTrainGui that consists of a Canvas, where graphical objects can be drawn.
     * The parameters define the width and the height of the canvas and the frameRate determines, how often
     * the method draw() is called within a second. A framerate of 0 causes the method draw() to be called only once.
     *
     * @param width     with of the canvas
     * @param height    height of the canvas
     * @param frameRate frequency with which the draw() method is called.
     */
    public Simulation(int width, int height, int frameRate) {
        super(width, height, frameRate);

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

    private void showParticle(Particle particle){
        if (particle.isFixed())
            this.canvas.setPenColor(Color.yellow);
        else
            this.canvas.setPenColor(particle.getColor());
        this.canvas.filledCircle(particle.getX(),particle.getY(), Math.sqrt(particle.getMass()));
    }

    public void showEnhancedParticle(Particle particle){
        this.canvas.setPenColor(Color.BLUE);
        this.canvas.filledCircle(particle.getX(),particle.getY(),5);
    }

    private void showRect(Rect rect){
            this.canvas.setPenColor(Color.RED);
            this.canvas.rectangle(rect.getX(),rect.getY(),rect.getW(),rect.getH());
    }

    private void showBarnesHutCenters(BarnesHutCenter center){
        this.canvas.setPenColor(Color.GREEN);
        this.canvas.circle(center.getX(),center.getY(),Math.sqrt(center.getMass()));
    }


    /*
     ******************************
     ****     overrides         ***
     ******************************
     */

    /**
     * Initialisiere Simulation
     */
    @Override
    public void setup() {
        canvas.setBackground(Color.BLACK);
        //manually select model here
        //this.model =  new ModelWithFixedCenter(width,height);
       // this.model = new ModelWithGalaxy(width,height);
        this.model = new ModelWithTwoGalaxies(width,height,1,1.5,0.1);
        this.engine = new GravityEngine(model);
    }

        @Override
        public void draw() {
            engine.timestep();
            this.canvas.clear(new Color(0,0,0));

        List<Particle> particles = this.engine.getParticles();
        if (particles!=null && particles.size()>0) {
            particles.stream().forEach(x->showParticle(x));
            showEnhancedParticle(particles.get(0));
        }

       List<BarnesHutCenter> gravityCenters = this.engine.getBarnesHutCenters();
       if (gravityCenters!=null) gravityCenters.stream().forEach(x->showBarnesHutCenters(x));


    }


    @Override
    public void keyAction(char keyChar) {

    }


    /*
     ******************************
     ****     main              ***
     ******************************
     */


    public static void main(String[] args) {
        //starte hier die Klasse mit einer Bildbreite, Bildhoehe und Bildrate (0 fuer keine Wiederholung, 60 fuer 60 Bilder pro Sekunde)
        new Simulation(1500,1000,30);
    }

}
