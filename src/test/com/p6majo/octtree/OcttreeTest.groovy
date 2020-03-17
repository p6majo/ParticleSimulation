package com.p6majo.octtree

import junit.framework.TestCase

class OcttreeTest extends TestCase {
 private Octtree<Particle> octtree

    void testAddObject() {

        Vector3D low = new Vector3D(0,0,0)
        Vector3D high = new Vector3D(100,100,100)

        octtree = new Octtree<>(low,high)
        for (int i = 0; i < 10; i++) {
            this.octtree.addObject(new Particle(low,high))
        }


       System.out.println(octtree.toString())
    }
}
