package com.p6majo.dynamicpolyhedra;

import com.p6majo.linalg.Vector;
import com.p6majo.linalg.Vector3D;
import com.p6majo.octtree.Cuboid;
import com.p6majo.octtree.Particle;

import java.util.List;

/**
 * The class HarmonicOscillator
 *
 * @author p6majo
 * @version 2021-02-04
 */
public class ModelHarmonicOscillator extends LagrangeModel {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private double springKonstant = 1.;
    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public ModelHarmonicOscillator(Cuboid box){
        super(box);
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
    public Vector getAcceleration(int i) {
        Particle p = particles.get(i);
        return p.getPosition().mul(springKonstant).mul(-1./p.getMass());
    }

    @Override
    public void setupInitialData(List<Particle> particles) {
        super.particles = particles;
        //create one particle on a spring
        particles.add(new Particle(Vector3D.random(box.getLength()/2,box.getWidth()/2,box.getHeight()/2),Vector3D.getZERO(),1.));
    }

    @Override
    public String toString() {
        return super.toString();
    }


}
