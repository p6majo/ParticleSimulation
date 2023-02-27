package com.p6majo.povrayanimation.camera;


import com.p6majo.linalg.Vector3D;
import com.p6majo.povrayanimation.objects.PovrayObject;

/**
 * The class CameraDefault, non-moving camera
 * The distance is entered into the constructor as a multiple of the scenesize
 *
 * @author p6majo
 * @version 2020-05-26
 */
public class CameraDefault extends PovRayCamera {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    protected final double aspectRatio;
    private final double sceneSize;
    protected double distance; //can be changed by subclasses


    public CameraDefault(double sceneSize,double aspectRatio, double distance) {
        this.sceneSize = sceneSize;
        this.distance = distance;
        this.aspectRatio = aspectRatio;

        super.position = new Vector3D(-distance*sceneSize,0,0);
        super.lookAt = Vector3D.getZERO();

    }




    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */



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

    /**
     * The position of the camera is set as multiples of the sceneSize
     * @param position
     */
    public void setPosition(Vector3D position){
    super.position = position.mul(sceneSize);
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
    public String draw() {
        StringBuilder camera =new StringBuilder();
        camera.append("camera {\n")
                .append("\tlocation "+this.position.toPovString()+"\n")
                .append("\tright -x*"+aspectRatio+"\n")
                .append("\trotate <0,180,0>\n")
                .append("\trotate <90,0,0>\n")
                //.append("\trotate<0,-20,0>\n")
                //.append("\trotate<0,0,30>\n")
                .append("\tlook_at "+this.lookAt.toPovString()+"\n")
                .append("}\n");
        return camera.toString();
    }

    @Override
    public PovrayObject copy() {
        return new CameraDefault(this.sceneSize,this.aspectRatio,this.distance);
    }

    @Override
    public String toString() {
        return "Default camera with sceneSize "+this.sceneSize+" and aspectRation "+aspectRatio+" and distance "+distance;
    }

}
