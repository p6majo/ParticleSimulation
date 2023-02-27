package com.p6majo.dynamicpolyhedra;

import com.p6majo.dynamicpolyhedra.ModelSoccerBall.Face;
import com.p6majo.linalg.Vector;
import com.p6majo.linalg.Vector3D;
import com.p6majo.octtree.Particle;
import com.p6majo.povrayanimation.PovrayAnimation;
import com.p6majo.povrayanimation.appearances.PlaneAppearance;
import com.p6majo.povrayanimation.appearances.SphereAppearance;
import com.p6majo.povrayanimation.objects.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

/**
 * The class PovrayViewSoccerball
 *
 * @author p6majo
 * @version 2021-02-06
 */
public class PovrayViewSoccerball extends PovrayAnimation {


    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private List<Face> faces;
    private double stretchCutOff = 0.125;
    private double fractionOfSprings = 0.02;
    private boolean first = true;
    private List<Boolean> selectedSprings ;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */


    /**
     * The Animation is defined by its name, which is the keyword for the file
     * and the frames that are generated.
     *
     * @param name     characterization of the animation
     * @param duration duration of the animation in seconds
     * @param fps      frames per second in Hz
     * @param width
     * @param height
     * @param dim
     */
    public PovrayViewSoccerball(String name, int duration, int fps, int width, int height, double dim) {
        super(name, duration, fps, width, height, dim);
        selectedSprings = new ArrayList<>();
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

    private List<PovrayObject> corners(List<Particle> particles){
        List<PovrayObject> objects = new ArrayList<>();
        for (Particle particle : particles) {
            objects.add(new Sphere(particle.getPosition().toVector3D(),particle.getRadius(),new SphereAppearance(Color.DARKRED)));
        }
        return objects;
    }


    private List<PovrayObject> springs_inside(List<Particle> particles){
        List<PovrayObject> springs = new ArrayList<>();


        int counter =0;
        for (Particle particle : particles) {
            NNParticle nnParticle = (NNParticle) particle;
            List<NNParticle> neighbours = nnParticle.getNeighbours();
            for (Particle particle1 : particles) {
                if (!neighbours.contains(particle1) && !particle1.equals(nnParticle)){
                    double equiDistance = nnParticle.getDistanceTo((NNParticle) particle1);
                    double actualDistance = nnParticle.getPosition().getDistance(particle1.getPosition());
                    double stretch = (equiDistance-actualDistance)/equiDistance;
                    if (stretch<0)
                        stretch = Math.max(-stretchCutOff,stretch);
                    else
                        stretch=Math.min(stretchCutOff,stretch);
                    Color color = null;
                    int s=(int) (255* stretch/stretchCutOff);
                    if (s<0)
                        color =Color.rgb(255,255+s,255+s);
                    else
                        color =Color.rgb(255-s,255-s,255);

                    if (first) {
                        if (Math.random() < fractionOfSprings) {
                            selectedSprings.add(true);
                            springs.add(new Cylinder(0.5, particle1.getPosition().toVector3D(), nnParticle.getPosition().toVector3D(), new SphereAppearance(color)));
                        } else {
                            selectedSprings.add(false);
                        }
                    }
                    else{
                        if (selectedSprings.get(counter++))
                            springs.add(new Cylinder(0.5, particle1.getPosition().toVector3D(), nnParticle.getPosition().toVector3D(), new SphereAppearance(color)));
                    }
                }
            }
        }
        if (first)
            first = false;
        return springs;
    }


    private List<PovrayObject> springs_outside(List<Particle> particles){
        List<PovrayObject> springs = new ArrayList<>();

        for (Particle particle : particles) {
            NNParticle nnParticle = (NNParticle) particle;
            List<NNParticle> neighbours = nnParticle.getNeighbours();
            for (NNParticle neighbour : neighbours) {
                double equiDistance = nnParticle.getDistanceTo((NNParticle) neighbour);
                double actualDistance = nnParticle.getPosition().getDistance(neighbour.getPosition());
                double stretch = (equiDistance - actualDistance) / equiDistance;
                if (stretch < 0)
                    stretch = Math.max(-stretchCutOff, stretch);
                else
                    stretch = Math.min(stretchCutOff, stretch);
                Color color = null;
                int s = (int) (255 * stretch / stretchCutOff);
                if (s < 0)
                    color = Color.rgb(255, 255 + s, 255 + s);
                else
                    color = Color.rgb(255 - s, 255 - s, 255);

                springs.add(new Cylinder(0.5, neighbour.getPosition().toVector3D(), nnParticle.getPosition().toVector3D(), new SphereAppearance(color)));
            }
        }
        return springs;
    }

    private List<PovrayObject> faces(List<Particle> particles){
        Vector center = Vector.getZero(3);
        for (Particle particle : particles) {
            center = center.add(particle.getPosition());
        }
        center = center.mul(1./particles.size());

        List<PovrayObject> objects = new ArrayList<>();
        int faceCounter=0;
        for (Face face : faces) {
            List<Vector> faceVertices = new ArrayList<>();
            for (Integer index : face.indices) {
                faceVertices.add(particles.get(index).getPosition());
            }
            Color color = null;
            if (faceVertices.size()==6)
                color = BLACK;
            else
                color = WHITE;

            //triangulate to allow for folded faces
            Vector first = faceVertices.get(0);
            for (int i = 1; i <faceVertices.size()-2 ; i++) { //omit last vertex, since it is the first one repeated
                List<Vector3D> triangle = new ArrayList<>();
                triangle.add(first.toVector3D());
                triangle.add(faceVertices.get(i).toVector3D());
                triangle.add(faceVertices.get(i+1).toVector3D());
                objects.add(new OrientedPrism(faceCounter++,triangle,center.toVector3D(),0.1,new PlaneAppearance(color,1.),true));
            }
        }
        return objects;
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
        return super.toString();
    }

    @Override
    public void draw(double time, List<Particle> particles){
        List<PovrayObject> objects = new ArrayList<>();

        objects.addAll(corners(particles));
        //objects.addAll(springs_inside(particles));
       // objects.addAll(springs_outside(particles));
        //objects.addAll(faces(particles));

        generateFrame(objects);
    }

    @Override
    public void init(List<Face> faces){
        this.faces = faces;
    }

}
