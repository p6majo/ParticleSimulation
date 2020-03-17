package com.p6majo.octtree

import junit.framework.TestCase

class OcttreeTest extends TestCase {
 private Octtree<Particle> octtree

    void testAddObject() {

        Point3D low = new Point3D(0,0,0)
        Point3D high = new Point3D(100,100,100)

        octtree = new Octtree<>(low,high)
        for (int i = 0; i < 10; i++) {
            this.octtree.addObject(new Particle(low,high))
        }


       System.out.println(octtree.toString())
    }
}
