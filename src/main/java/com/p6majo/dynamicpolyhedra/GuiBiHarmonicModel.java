package com.p6majo.dynamicpolyhedra;

import com.p6majo.codingtrain.CodingTrainGui;
import com.p6majo.linalg.Vector;
import com.p6majo.linalg.Vector3D;
import com.p6majo.octtree.Cuboid;
import com.p6majo.octtree.Particle;

import java.awt.*;
import java.util.List;

/**
 * The class SimulationGui
 *
 * @author p6majo
 * @version 2021-02-04
 */
public class GuiBiHarmonicModel extends CodingTrainGui implements SimulationView {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private List<Particle> particles;
    private double time;
    private Cuboid box;
    private LagrangeSimulation sim;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
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
    public GuiBiHarmonicModel(int width, int height, int frameRate) {
        super(width, height, frameRate);
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

    private Color getColorFromDepth(double depth){
        int d = (int) map(depth,box.getFront(),box.getBack(),0,255);
        d = Math.min(255,d);
        d = Math.max(0,d);

        return new Color(d,255-d,0);
    }
    /*
     ***********************************************
     ***           Overrides            ************
     ***********************************************
     */

    @Override
    public void setup() {
        box = new Cuboid(new Vector3D(-100,-100,-100),new Vector3D(100,100,100));
        this.canvas.setXscale(box.getLeft(),box.getRight());
        this.canvas.setYscale(box.getBottom(),box.getTop());

        ModelBiHarmonicOscillator model = new ModelBiHarmonicOscillator(box);
        this.sim = new LagrangeSimulation(model);
        this.sim.addView(this);
        this.sim.start();
    }

    @Override
    public void draw() {
        canvas.clear(new Color(0,0,0));
        canvas.setPenColor(Color.WHITE);
        canvas.text(0,0,time+" ms");
        if (particles!=null)
            for (Particle particle : particles) {
                Vector pos = particle.getPosition();
                //System.out.println(time+" "+pos);
                double r = particle.getMass();
                canvas.setPenColor(getColorFromDepth(pos.getValue(1)));
                canvas.filledCircle(pos.getValue(0),pos.getValue(2),r);
            }

    }

    @Override
    public void init(List<ModelSoccerBall.Face> faces) {
    }

    @Override
    public void draw(double time, List<Particle> particles) {
        this.particles = particles;
        this.time = time;
    }

    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */


    public static void main(String[] args) {
        new GuiBiHarmonicModel(600,600,60);
    }

}
