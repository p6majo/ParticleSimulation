package com.p6majo.octtree;

import com.p6majo.linalg.Vector3D;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class CuboidTest {

    private Cuboid one;
    private Cuboid two;

    @Before
    public void setUp() throws Exception {
        this.one = Cuboid.random(100,200,300);
        this.two = Cuboid.random(100,200,300);
    }

    @Test
    public void getLow() {
        System.out.println("Cuboid one: " + one.toString());
        System.out.println("Test getLow(): "+one.getLow());
    }

    @Test
    public void getHigh() {
        System.out.println("Cuboid one: " + one.toString());
        System.out.println("Test getHigh(): "+one.getHigh());
    }

    @Test
    public void getLength() {
        System.out.println("Cuboid one: " + one.toString());
        System.out.println("Test getLength(): "+one.getLength());
        assertEquals(one.getLength(),one.getRight()-one.getLeft());
    }

    @Test
    public void getWidth() {
        System.out.println("Cuboid one: " + one.toString());
        System.out.println("Test getWidth(): "+one.getWidth());
        assertEquals(one.getWidth(),one.getBack()-one.getFront());
    }

    @Test
    public void getHeight() {
        System.out.println("Cuboid one: " + one.toString());
        System.out.println("Test getHeight(): "+one.getHeight());
        assertEquals(one.getHeight(),one.getTop()-one.getBottom());
    }


    @Test
    public void contains() {
        System.out.println("Cuboid " + one.toString());
        int count = 0;
        for (int i = 0; i < 1000; i++) {
            Vector3D point = Vector3D.random(100,200,300);
            if (one.contains(point))
                System.out.println("Contained: " + point.toString());
           else
            count++;
        }

        System.out.println(count + "/1000 points missed the cuboid");
    }

    @Test
    public void intersects() {
        System.out.println("Test for intersection between " + one.toString() + " and " + two.toString());
        System.out.println(one.intersects(two));
        System.out.println("The intersection cuboid is given by " + one.getIntersection(two) + " or\n" + two.getIntersection(one));
    }

}