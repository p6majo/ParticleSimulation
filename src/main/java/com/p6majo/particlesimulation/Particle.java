package com.p6majo.particlesimulation;

import java.awt.*;
import java.util.Random;

/**
 * @author AlexK
 * @version 2020-01-18
 */
public class Particle {



    /*
     **********************
     ***   attributes   ***
     **********************
     */
    private Canvas canvas;
    private Random rnd = new Random();

    private Vector pos;
    private Vector vel;
    private Vector acc;

    private Color color;
    private int mass = rnd.nextInt(6)+1;

    private boolean fixed=false;
    /*
     *********************
     ***  constructors ***
     *********************
     */

    public Particle(double x, double y){
        this(x,y,0,false);
    }

    public Particle(double x, double y, int mass, boolean fixed,Color color){
         pos = new Vector(x,y);
        vel = new Vector(0,0);
        if (mass!=0)
            this.mass =mass;
        acc = new Vector(0,0);
        this.fixed = fixed;
        this.color = color;
    }
    public Particle(double x, double y, int mass, boolean fixed){
        this(x,y,mass,fixed,Color.RED);
    }

    /*
     ***********************
     ****       getter   ***
     ***********************
     */
    public double getX(){return this.pos.getX();}
    public double getY(){return this.pos.getY();}
    public Vector getPos(){return this.pos;}
    public int getMass(){return this.mass;}
    public Canvas getCanvas(){return this.canvas;}
    public boolean isFixed() {
        return fixed;
    }
    public Color getColor() {
        return this.color;
    }
    /*
     ************************
     ****       setter    ***
     ************************
     */

    public void setAcceleration(Vector acc){
        this.acc = acc;
    }
    public void setFixed(boolean fixed){
        this.fixed = fixed;
    }
    public void addVel(Vector vel){
        this.vel=this.vel.plus(vel);
    }

    /*
     *****************************
     ***     public methods    ***
     *****************************
     */
    /*
    public void attracted(AVector target){
        AVector force = new AVector(target,this.pos);
        double dsquared = force.magSq();
        dsquared = limit(dsquared, 25, 500);
        double G = 1;//6.67408*10E-1;
        double strength;
        strength = G / dsquared;
        force.setMag(strength);
        acc = force;
    }
     */

    /**
     * Berechne Distanz zu einem anderen Teilchen
     * @param p zweites Teilchen
     * @return Distanz
     */
    public double distanceTo(Particle p){
       return this.pos.minus(p.pos).mag();
    }


    public double distanceTo(Vector p){
        return this.pos.minus(p).mag();
    }

    public double limit(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    public void update(double dt){
       if (!fixed){
           this.vel.add(this.acc.mal(dt));
            this.pos.add(this.vel.mal(dt));
       }
    }

    public void setPosition(double x, double y){
        this.pos = new Vector(x,y);
    }

    public void setVel(double totalMass,Vector center){
        this.setVel(totalMass,center,1);
    }

    public void setVel(double totalMass, Vector center,int orientation){
        double G = 1.;//Gravitationskonstante
        double r  = center.minus(pos).mag(); //Entfernung vom Mittelpunkt
        double theta = Math.atan2(this.getY()-center.getY(),this.getX()-center.getX());
        double v=Math.sqrt(totalMass/3);

        this.vel = new Vector(-v*Math.sin(theta),v*Math.cos(theta));
        this.vel = this.vel.mal(orientation);
    }
    /*
     ******************************
     ****     private methods   ***
     ******************************
     */


    /*
     ******************************
     ****     overrides         ***
     ******************************
     */

    /*
     ******************************
     ****     toString()        ***
     ******************************
     */

    @Override
    public String toString() {
        return "("+(int) Math.round(pos.getX())+"|"+(int) Math.round(pos.getY())+"): "+ mass+"";
    }


}
