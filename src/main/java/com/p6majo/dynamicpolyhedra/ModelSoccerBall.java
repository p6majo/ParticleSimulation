package com.p6majo.dynamicpolyhedra;

import com.p6majo.linalg.Vector;
import com.p6majo.linalg.Vector;
import com.p6majo.octtree.Cuboid;
import com.p6majo.octtree.Particle;

import java.util.ArrayList;
import java.util.List;


/**
 * The class HarmonicOscillator
 *
 * @author p6majo
 * @version 2021-02-04
 */
public class ModelSoccerBall extends LagrangeModel {

    /*
     *********************************************
     ***           Private classes      **********
     *********************************************
     */


    private class Edge{
        int one;
        int two;
        public Edge(int one,int two){this.one=one;this.two=two;}
    }

    public class Face{
        Integer[] indices;
        List<Particle> particles;
        public Face(Integer... indices){
            this.indices = indices;
            this.particles = new ArrayList<>();
        }
        public void addParticle(Particle p){ this.particles.add(p);}
        public List<Particle> getParticles(){return this.particles;}
        public boolean contains(Particle p){return this.particles.contains(p);}
    }


    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */


    public Vector[] vertices = new Vector[]{new Vector(0.000000,-0.979432,-0.201774), new Vector(0.000000,0.979432,-0.201774), new Vector(0.730026,0.201774,-0.652955), new Vector(-0.854729,-0.403548,0.326477), new Vector(-0.730026,0.201774,-0.652955), new Vector(0.730026,-0.201774,-0.652955), new Vector(0.854729,-0.403548,0.326477), new Vector(-0.854729,0.403548,0.326477), new Vector(-0.730026,-0.201774,-0.652955), new Vector(0.326477,-0.854729,-0.403548), new Vector(0.854729,0.403548,0.326477), new Vector(-0.652955,0.730026,0.201774), new Vector(-0.403548,0.326477,-0.854729), new Vector(-0.326477,-0.854729,-0.403548), new Vector(0.326477,0.854729,-0.403548), new Vector(0.201774,-0.652955,0.730026), new Vector(0.652955,0.730026,0.201774), new Vector(-0.652955,-0.730026,0.201774), new Vector(0.403548,0.326477,-0.854729), new Vector(-0.403548,-0.326477,-0.854729), new Vector(-0.326477,0.854729,-0.403548), new Vector(-0.201774,-0.652955,0.730026), new Vector(0.201774,0.652955,0.730026), new Vector(0.652955,-0.730026,0.201774), new Vector(0.403548,-0.326477,-0.854729), new Vector(-0.201774,0.000000,-0.979432), new Vector(-0.979432,-0.201774,0.000000), new Vector(-0.201774,0.652955,0.730026), new Vector(-0.201774,0.000000,0.979432), new Vector(0.979432,-0.201774,0.000000), new Vector(0.201774,-0.652955,-0.730026), new Vector(0.201774,0.000000,-0.979432), new Vector(-0.979432,0.201774,0.000000), new Vector(-0.403548,0.326477,0.854729), new Vector(0.201774,0.000000,0.979432), new Vector(0.979432,0.201774,0.000000), new Vector(-0.201774,-0.652955,-0.730026), new Vector(0.201774,0.652955,-0.730026), new Vector(-0.652955,0.730026,-0.201774), new Vector(0.403548,0.326477,0.854729), new Vector(-0.403548,-0.326477,0.854729), new Vector(0.326477,-0.854729,0.403548), new Vector(-0.201774,0.652955,-0.730026), new Vector(-0.652955,-0.730026,-0.201774), new Vector(0.652955,0.730026,-0.201774), new Vector(0.403548,-0.326477,0.854729), new Vector(0.326477,0.854729,0.403548), new Vector(-0.326477,-0.854729,0.403548), new Vector(-0.854729,-0.403548,-0.326477), new Vector(0.652955,-0.730026,-0.201774), new Vector(0.730026,0.201774,0.652955), new Vector(-0.326477,0.854729,0.403548), new Vector(0.854729,-0.403548,-0.326477), new Vector(-0.854729,0.403548,-0.326477), new Vector(-0.730026,0.201774,0.652955), new Vector(0.730026,-0.201774,0.652955), new Vector(0.854729,0.403548,-0.326477), new Vector(-0.730026,-0.201774,0.652955), new Vector(0.000000,-0.979432,0.201774), new Vector(0.000000,0.979432,0.201774)};
    public Edge[] edges = new Edge[]{new Edge(0,9), new Edge(0,13), new Edge(0,58), new Edge(1,14), new Edge(1,20), new Edge(1,59), new Edge(2,5), new Edge(2,18), new Edge(2,56), new Edge(3,17), new Edge(3,26), new Edge(3,57), new Edge(4,8), new Edge(4,12), new Edge(4,53), new Edge(5,24), new Edge(5,52), new Edge(6,23), new Edge(6,29), new Edge(6,55), new Edge(7,11), new Edge(7,32), new Edge(7,54), new Edge(8,19), new Edge(8,48), new Edge(9,30), new Edge(9,49), new Edge(10,16), new Edge(10,35), new Edge(10,50), new Edge(11,38), new Edge(11,51), new Edge(12,25), new Edge(12,42), new Edge(13,36), new Edge(13,43), new Edge(14,37), new Edge(14,44), new Edge(15,21), new Edge(15,41), new Edge(15,45), new Edge(16,44), new Edge(16,46), new Edge(17,43), new Edge(17,47), new Edge(18,31), new Edge(18,37), new Edge(19,25), new Edge(19,36), new Edge(20,38), new Edge(20,42), new Edge(21,40), new Edge(21,47), new Edge(22,27), new Edge(22,39), new Edge(22,46), new Edge(23,41), new Edge(23,49), new Edge(24,30), new Edge(24,31), new Edge(25,31), new Edge(26,32), new Edge(26,48), new Edge(27,33), new Edge(27,51), new Edge(28,33), new Edge(28,34), new Edge(28,40), new Edge(29,35), new Edge(29,52), new Edge(30,36), new Edge(32,53), new Edge(33,54), new Edge(34,39), new Edge(34,45), new Edge(35,56), new Edge(37,42), new Edge(38,53), new Edge(39,50), new Edge(40,57), new Edge(41,58), new Edge(43,48), new Edge(44,56), new Edge(45,55), new Edge(46,59), new Edge(47,58), new Edge(49,52), new Edge(50,55), new Edge(51,59), new Edge(54,57)};
    public Face[] faces = new Face[]{new Face(0,9,30,36,13,0), new Face( 1,14,37,42,20,1), new Face( 2,5,24,31,18,2), new Face( 3,17,43,48,26,3), new Face( 4,8,19,25,12,4), new Face( 6,23,49,52,29,6), new Face( 7,11,38,53,32,7), new Face( 10,16,44,56,35,10), new Face( 15,21,47,58,41,15), new Face( 22,27,51,59,46,22), new Face( 28,33,54,57,40,28), new Face( 34,39,50,55,45,34), new Face( 0,9,49,23,41,58,0), new Face( 0,13,43,17,47,58,0), new Face( 1,20,38,11,51,59,1), new Face( 1,14,44,16,46,59,1), new Face( 2,5,52,29,35,56,2), new Face( 2,18,37,14,44,56,2), new Face( 3,26,32,7,54,57,3), new Face( 3,17,47,21,40,57,3), new Face( 4,8,48,26,32,53,4), new Face( 4,12,42,20,38,53,4), new Face( 5,24,30,9,49,52,5), new Face( 6,29,35,10,50,55,6), new Face( 6,23,41,15,45,55,6), new Face( 7,11,51,27,33,54,7), new Face( 8,19,36,13,43,48,8), new Face( 10,16,46,22,39,50,10), new Face( 12,25,31,18,37,42,12), new Face( 15,21,40,28,34,45,15), new Face( 19,25,31,24,30,36,19), new Face( 22,27,33,28,34,39,22)};


    private double patchConstant = 0.;
    private double floorConstant = 1000.;
    private double airConstant =0.;
    private double mass =0.5;
    private double radius =2.5;
    private double patchDamping = 0.*Math.sqrt(mass*patchConstant);
    private double friction = 0.03;//0.03  is realistic
    private double torqueParam = 0.;
    private double drift = 0.;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public ModelSoccerBall(Cuboid box){
        super(box);
    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public Face[] getFaces(){
        return this.faces;
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
    public Vector getAccelerationWithRotation(int i, Vector center){
        Vector torque = new Vector(1.,0.,0.).mul(-1.*torqueParam);
        Vector pos = particles.get(i).getPosition();
        Vector r = pos.sub(center);
        Vector a = r.toVector3D().cross(torque.toVector3D()).mul(1./r.dot(r));
        return getAcceleration(i).add(a);
    }

    @Override
    public Vector getAcceleration(int i) {
        NNParticle particle = (NNParticle) particles.get(i);
        List<NNParticle> neighbours = particle.getNeighbours();
        List<NNParticle> nNeighbours = particle.getSecondClassNeighbours();
        Vector position =particle.getPosition();

        //gravity
        Vector gravity = new Vector(0.,-10.0,0.);

        //ground
        Vector floor = Vector.getZero(3);
        double y = particle.getPosition().getY();
        Vector v1 = particle.getVelocity();
        double m = particle.getMass();
        if (y <-0.)
            floor = new Vector(0.,1.,0.).mul(-(y)*floorConstant/m);


        //nearest neighbours a=k/m*(|d|-l)/|d| *d
        Vector nForce = Vector.getZero(3);
        if (patchConstant>0) {
            for (NNParticle neighbour : neighbours) {
                Vector distance = neighbour.getPosition().sub(position);
                double d = distance.length();
                double l = particle.getDistanceTo(neighbour);
                Vector v2 = neighbour.getVelocity();
                Vector relV = v1.sub(v2);
                nForce = nForce.add(distance.mul(patchConstant / m / d * (d - l))).add(relV.mul(-patchDamping / m));
            }
        }

        Vector nnForce = Vector.getZero(3);
        if (airConstant>0) {
            //second class neighbours interacting via air pressure
            for (NNParticle neighbour : nNeighbours) {
                Vector distance = neighbour.getPosition().sub(position);
                double d = distance.length();
                double l = particle.getDistanceTo(neighbour);
                Vector v2 = neighbour.getVelocity();
                Vector relV = v1.sub(v2);
                nnForce = nnForce.add(distance.mul(airConstant / m / d * (d - l)));
            }
        }

        return gravity.add(floor).add(nForce).add(nnForce).add(particle.getVelocity().mul(-friction/m));
    }

    @Override
    public void setupInitialData(List<Particle> particles) {
        super.particles = particles;
        Vector center = new Vector(0.,box.getWidth()*3/4,0.);
        double scale = box.getHeight()/10;

         Vector driftV = new Vector(0.,0.,1.).mul(drift);

        //create vertices of the soccer ball
        for (int i = 0; i < vertices.length; i++) {
            Vector pos = vertices[i].mul(scale).add(center);
            System.out.println(i + ": " + pos);
            NNParticle particle = new NNParticle(pos, driftV, mass,radius,i);
            particles.add(particle);
        }

        //fill faces with particles
        for (Face face : faces) {
            for (Integer index : face.indices) {
                face.addParticle(particles.get(index));
            }
        }

       //setup nearest neigbours
        for (Particle particle : particles) {
            for (Face face : faces) {
                if (face.contains(particle)){
                    for (Integer index : face.indices) {
                        NNParticle possibleNeighbour = (NNParticle) particles.get(index);
                        if (((NNParticle) particle).addNeighbour(possibleNeighbour));//addNeighbour takes care that no particle is added twice
                    }
                }
            }
        }

        //add all other particles as second nearest neighbours
        for (Particle particle : particles) {
            for (Particle particle1 : particles) {
                ((NNParticle) particle).addSecondClassNeighbour((NNParticle) particle1);
            }
            System.out.println(particle);
        }
    }

    @Override
    public String toString() {
        return "Model of a soccer ball";
    }


}
