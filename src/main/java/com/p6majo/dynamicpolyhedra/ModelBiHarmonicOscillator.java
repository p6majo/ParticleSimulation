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
public class ModelBiHarmonicOscillator extends LagrangeModel {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private double springKonstant = 1.;
    private double equilibriumDistance = 50.;
    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public ModelBiHarmonicOscillator(Cuboid box){
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
        Particle p0 = particles.get(0);
        Vector a = p0.getPosition();
        Particle p1 = particles.get(1);
        Vector b = p1.getPosition();
        Vector ab = b.sub(a);
        double d = ab.length();
        if (i==0)
            return ab.mul(springKonstant*(d-equilibriumDistance)/d).mul(1./p0.getMass());
        else
            return ab.mul(-springKonstant*(d-equilibriumDistance)/d).mul(1./p1.getMass());

    }

    @Override
    public void setupInitialData(List<Particle> particles) {
        super.particles = particles;
        //create one particle on a spring
        Vector pos0 = Vector3D.random(box.getLength() / 2, box.getWidth() / 2, box.getHeight() / 2);
        Vector dir = Vector3D.random(box.getLength() / 2, box.getWidth() / 2, box.getHeight() / 2);
        dir = dir.mul(1./dir.length());
        Vector pos1 = pos0.add(dir.mul(equilibriumDistance*1.1));
        particles.add(new Particle(pos0,Vector3D.getZERO(),1.));
        particles.add(new Particle(pos1,Vector3D.getZERO(),1.));
    }

    @Override
    public String toString() {
        return "Model of two masses attached to one spring with an equilibrium length.";
    }


}
