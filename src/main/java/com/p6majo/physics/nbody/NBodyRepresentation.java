package com.p6majo.physics.nbody;


import com.p6majo.utils.Boundary;

public interface NBodyRepresentation {
    public void showParticle(Particle particle);
    public void setDoubleBuffering(boolean doubleBuffering);
    public void showScale(double size);
    public void setBoundary(Boundary boundary);
    public void clear();
    public void refresh();
}
