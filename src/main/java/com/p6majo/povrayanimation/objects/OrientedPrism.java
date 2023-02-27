package com.p6majo.povrayanimation.objects;


import com.p6majo.linalg.Vector3D;
import com.p6majo.povrayanimation.appearances.Appearance;

import java.util.List;

/**
 * The class OrientedPrism
 *
 * There are a few steps required to get a povray prism that corresponds to the face.
 * The three dimensional vertices have to be mapped onto the x-z-plane. Thereafter the prism is created
 * and rotated back into the original position.
 *
 * In this class, an additional point of the polytope is provided to have the normal vector pointing inward or outward,
 * which is controlled by the outward property
 *
 * @author com.p6majo
 * @version 2020-09-18
 */
public class OrientedPrism extends Prism {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private boolean outward = true;

    //the reference point defines the direction of the normal vector
    private Vector3D cellCenterPoint;


    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    /**
     *
     * @param id the index of the face
     * @param vertices the list of vertices that span the face
     * @param center the center of the cell that the face is part of
     * @param height
     * @param appearance
     * @param outward
     */
    public OrientedPrism(int id, List<Vector3D> vertices, Vector3D center, double height, Appearance appearance, boolean outward) {
        super(id,vertices,height,appearance);
        this.cellCenterPoint =center;
        this.outward = outward;
    }


    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public boolean isOutward(){return this.outward;}
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



    public String drawNormalVectors() {
        Vector3D faceCenterPoint = this.getCenter();
        Vector3D outdirection = faceCenterPoint.sub(cellCenterPoint);
        double sp = getNormal().dot(outdirection);

        if (outward && sp<0)
            setNormal(getNormal().neg());
        else if (!outward && sp>0)
            setNormal(getNormal().neg());

        return new Ray(0.3,faceCenterPoint,faceCenterPoint.add(getNormal().mul(17.)),appearance).draw();
    }
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


    @Override
    public String draw() {
        Vector3D faceCenterPoint = this.getCenter();
        Vector3D outdirection = faceCenterPoint.sub(cellCenterPoint);
        double sp = getNormal().dot(outdirection);

        if (outward && sp<0)
            setNormal(getNormal().neg());
        else if (!outward && sp>0)
            setNormal(getNormal().neg());


        return super.draw();
    }

    @Override
    public PovrayObject copy() {
        return new OrientedPrism(super.getId(),super.getVertices(),this.cellCenterPoint,super.getHeight(),super.appearance,this.outward);
    }


    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */


    @Override
    public String toString() {
        String out = outward?"outward bound ":"inward bound ";
        return out+super.toString();
    }



}
