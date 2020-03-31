package com.p6majo.linalg;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PlaneTest {

    private Plane plane;
    @Before
    public void setUp() throws Exception {
        this.plane  = new Plane(new Vector3D(0,1,1),new Vector3D(1,1,1));
    }

    @Test
    public void getNormal() {
        System.out.println("Normal vector of the plane: "+this.plane.getNormal());
    }


    @Test
    public void getSupport() {
        System.out.println("supporting vector of the plane: "+this.plane.getSupport());
    }

    @Test
    public void toStringTest() {
        System.out.println(plane.toString());
    }

    @Test
    public void getOrthonormalFrame() {
        List<Vector3D> orthonormalFrame = this.plane.getOrthonormalFrame();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println("(v[" + i + "],[" + j + "]) = " + orthonormalFrame.get(i).dot(orthonormalFrame.get(j)));
            }
        }

        System.out.println(this.plane.toString());

        System.out.println("v[1]x[2] = " + orthonormalFrame.get(0).cross(orthonormalFrame.get(1)));
        System.out.println("v[2]x[3] = " + orthonormalFrame.get(1).cross(orthonormalFrame.get(2)));
        System.out.println("v[3]x[1] = " + orthonormalFrame.get(2).cross(orthonormalFrame.get(0)));

        System.out.println("v[1]x[2] -v[0]= " + orthonormalFrame.get(0).cross(orthonormalFrame.get(1)).sub(orthonormalFrame.get(2)));
        System.out.println("v[2]x[3] -v[1]= " + orthonormalFrame.get(1).cross(orthonormalFrame.get(2)).sub(orthonormalFrame.get(0)));
        System.out.println("v[3]x[1] -v[2]= " + orthonormalFrame.get(2).cross(orthonormalFrame.get(0)).sub(orthonormalFrame.get(1)));

    }
}