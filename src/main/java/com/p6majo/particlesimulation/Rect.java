package com.p6majo.particlesimulation;

/**
 * @author AlexK
 * @version 2020-01-18
 * @author p6majo
 * @version 2020-03-04
 * Uebergang zu double, damit auch Teilchen, die sich sehr nahe kommen, in zwei getrennte Zweige gepackt werden koennen
 */
public class Rect {



    /*
     **********************
     ***   attributes   ***
     **********************
     */

    private double x;
    private double y;
    private double w;
    private double h;
    
    /*
     *********************
     ***  constructors ***
     *********************
     */

    public Rect(double x,double y,double width,double height){
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
    }

    /*
     ***********************
     ****       getter   ***
     ***********************
     */
    public double getX(){
        return this.x;
    }
    
    public double getY(){
        return this.y;
    }
    
    public double getW(){
        return this.w;
    }
    
    public double getH(){
        return this.h;
    }


    /*
     ************************
     ****       setter    ***
     ************************
     */

    /*
     *****************************
     ***     public methods    ***
     *****************************
     */

    public void update(){
        
    }

    /**
     * p6majo: inklusiv nach links, exklusiv nach rechts, um Ueberlappung zu vermeiden (nicht sonderlich wichtig)
     * @param point
     * @return
     */
    public boolean contains(Particle point){
        return(point.getX() >= this.x - this.w &&
               point.getX() < this.x + this.w &&
               point.getY() >= this.y - this.h &&
               point.getY() < this.y + this.h);
    }
    
    public boolean intersects(Rect range){
        return !(range.getX() - range.getW() > this.x + this.w ||
                 range.getX() + this.w < this.x - this.w ||
                 range.getY() - range.getH() > this.y + this.h ||
                 range.getY() + this.h < this.y - this.h);
    }
    
    public boolean cIntersects(Circle range){
        return !(range.getX() - range.getR() > this.x + this.w ||
                 range.getX() + this.w < this.x - this.w ||
                 range.getY() - range.getR() > this.y + this.h ||
                 range.getY() + this.h < this.y - this.h);
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
        return "("+(x-w)+"|"+(y-w)+")-("+(x+w)+"|"+(y+w)+")";
    }


}
