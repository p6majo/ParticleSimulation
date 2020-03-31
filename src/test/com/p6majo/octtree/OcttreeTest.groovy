package com.p6majo.octtree

import com.p6majo.linalg.Vector3D
import junit.framework.TestCase

class OcttreeTest extends TestCase {
 private Octtree<Particle> octtree

    void testAddObject() {
        Vector3D low = new Vector3D(0,0,0)
        Vector3D high = new Vector3D(100,100,100)

        octtree = new Octtree<>(low,high,4)
        for (int i = 0; i < 10; i++) {
            this.octtree.addObject(new Particle(low,high))
        }


       System.out.println(octtree.toString())

        Cuboid range = new Cuboid(new Vector3D(25,25,25),new Vector3D(75,75,75));
        System.out.println("Query in range "+range.toString()+":\n" +octtree.query(range).toString());
    }

    void testQuery(){
            Vector3D low = new Vector3D(0,0,0)
            Vector3D high = new Vector3D(100,100,100)

            int numberOfParticles = 100;

            Cuboid full = new Cuboid(low,high);
            octtree = new Octtree<>(full,4)
            for (int i = 0; i < numberOfParticles; i++) {
                this.octtree.addObject(Particle.random(full))
            }


            System.out.println("Query all "+octtree.query(full));
            assertEquals(octtree.query(full).size(),numberOfParticles);

            Cuboid range = new Cuboid(new Vector3D(25,25,25),new Vector3D(75,75,75));
            System.out.println("Query in range "+range.toString()+":\n" +octtree.query(range).toString());
    }
}
