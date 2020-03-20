package com.p6majo.models;

import com.p6majo.gravityengines.GravityEngine3D;
import com.p6majo.octtree.Cuboid;
import com.p6majo.octtree.Vector3D;
import org.junit.Before;
import org.junit.Test;

public class GravityEngine3DTest {

    private GravityEngine3D engine;

    @Test
    public void timestep() {
        for (int i = 0; i < 100; i++) {
            engine.timestep();
            System.out.println(String.format("Time %.2f: ",engine.getTime())+engine.getParticles());
        }
    }

    @Before
    public void setUp() throws Exception {
        Cuboid container = new Cuboid(new Vector3D(0,0,0),new Vector3D(100,100,100));
        Model3D model = new Model3DTwoBodies(1.,1.,2);

        engine = new GravityEngine3D(model);
        System.out.println(engine.toString());

    }
}