package com.p6majo.povrayanimation.objects;


import com.p6majo.linalg.Vector3D;
import com.p6majo.logger.Logger;
import com.p6majo.povrayanimation.appearances.Appearance;
import com.p6majo.transformation.Rotation;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The class Prism
 *
 * There are a few steps required to get a povray prism that corresponds to the face.
 * The three dimensional vertices have to be mapped onto the x-z-plane. Thereafter the prism is created
 * and rotated back into the original position.
 *
 * @author com.p6majo
 * @version 2020-06-11
 *
 * @version 2020-01-27
 * add gradient appearance
 */
public class Prism extends PovrayObject  {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private List<Vector3D> vertices;
    private Set<Vector3D> uniqueVertices; //avoid double vertices that can arise from degeneracies
    private List<Vector3D> preImages;
    private double height;

    protected Vector3D normal; //normal of the prism, can be modified by the subclass OrientedPrism
    private Vector3D center; //center of the prism
    private double angle; //angle between normal and y-Axis

    private int id;
    private boolean isValid=true;


    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public Prism(int id, List<Vector3D> vertices, double height, Appearance appearance) {
        super(appearance);
        this.id = id;
        this.vertices = vertices;
        this.height = height;
        uniqueVertices = new HashSet<>();
        uniqueVertices.addAll(vertices);

        calculateNormal();
        calculateCenter();
    }


    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */


    public List<Vector3D> getPreImages(){
        return this.preImages;
    }
    public List<Vector3D> getVertices(){
        return this.vertices;
    }

    public double getHeight() {
        return height;
    }

    public Vector3D getNormal() {
        return normal;
    }
    public Vector3D getCenter() {

        if (center==null)
            calculateCenter();
        return center;
    }

    public double getAngle() {
        return angle;
    }

    public boolean isValid() { return isValid && (appearance!=null); }

    public int getId(){return this.id;}

    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */

    public void setVertices(List<Vector3D> vertices){ this.vertices = vertices;

        uniqueVertices = new HashSet<>();
        uniqueVertices.addAll(vertices);

        calculateNormal();
        calculateCenter();
    }

    public void setAppearance(Appearance appearance){ this.appearance = appearance; }


    public void setNormal(Vector3D normal){
        this.normal = normal;
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

    private void prepareDrawing(){
        calculateAngle();
        calculatePreImages();
    }

    private void calculateNormal(){
        if (this.uniqueVertices.size()<3) {
            Logger.logging(Logger.Level.warning, "At least three points must be given to define prism but it received\n" + vertices+"\n["+vertices.stream().map(x->x.hashCode()+"").collect(Collectors.joining(","))+"]", this);
            this.isValid = false;
        }
        else {
            Iterator<Vector3D> iterator = uniqueVertices.iterator();
            Vector3D v0 = iterator.next();
            Vector3D v1 = iterator.next();
            Vector3D v2 = iterator.next();

            Vector3D u = v0.directionTo(v1);
            Vector3D v = v0.directionTo(v2);

            Vector3D n = u.cross(v);
            if (n.length() == 0.) {
                Logger.logging(Logger.Level.warning, "Three points in one line were provided for the prism: " + v0 + " " + v1 + " " + v2, this);
                this.isValid = false;
            }

            this.normal = n.normalize();

            //make sure it points outward. Since all polyhedra are centered around the origin
            //we just have to make sure that the normal is roughly aligned with the center of the face
            if (normal.dot(getCenter())<0)
                normal = normal.neg();
        }
    }


    private void calculateCenter(){
        Vector3D sum = Vector3D.getZERO();
        for (Vector3D vertex : uniqueVertices) {
            sum = sum.add(vertex);
        }

        this.center = sum.mul(1./this.uniqueVertices.size());
    }

    private void calculateAngle(){
        this.angle = Math.acos(Vector3D.getUnitY().dot(this.normal));
    }

    private void calculatePreImages(){
        this.preImages = new ArrayList<>();
        Vector3D omega = this.normal.cross(Vector3D.getUnitY());
        if (omega.length()>0.0001) {
            Rotation rot = new Rotation(omega.normalize(),angle);
            for (Vector3D vertex : vertices) {
                this.preImages.add(vertex.sub(center).linMap(rot.getRotationMatrix()));
            }
        }
        else{ //no rotation is needed
            for (Vector3D vertex : vertices) {
                this.preImages.add(vertex.sub(center));
            }
        }
    }


    /*
     ***********************************************
     ***           Overrides            ************
     ***********************************************
     */


    @Override
    public String draw() {
        prepareDrawing();
        StringBuilder povString = new StringBuilder();

        povString.append("#declare prism"+id+"= prism {\n")
                .append("\tlinear_spline\n")
                .append("\t0, "+height+", "+(vertices.size()+1)+"\n")
                .append("\t");
        boolean first = true;
        for (Vector3D preImage : preImages) {
            povString.append("<"+preImage.getX()+","+preImage.getZ()+">,");
        }
        //add first point again
        povString.append("<"+preImages.get(0).getX()+","+preImages.get(0).getZ()+">\n");


        povString.append(super.appearance.getPovrayRepresentation(super.fadeInCounter,super.fadeInMax));
        if (!shadow)
            povString.append("\tno_shadow\n");

        povString.append("}\n");

        Vector3D omega = this.normal.cross(Vector3D.getUnitY());
        Vector3D position = center.add(normal.mul(0.2));
        if (omega.length()>0.0001) {
            Rotation rot = new Rotation(omega.normalize(),-angle);
            povString.append("object{prism"+id+" rotate"+rot.getEulerAnglesDegrees().toPovString()+" translate"+position.toPovString()+"}\n");
        }
        else
            povString.append("object{prism"+id+" translate"+position.toPovString2()+"}\n");

        //for debugging of the normal direction
        //Ray ray = new Ray(0.3,getCenter(),getCenter().add(normal.mul(4.)),new SphereAppearance(RED));
        //povString.append(ray.draw());

        return povString.toString();
    }

    @Override
    public PovrayObject copy() {
        return new Prism(this.id,this.vertices,this.height,super.appearance);
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
