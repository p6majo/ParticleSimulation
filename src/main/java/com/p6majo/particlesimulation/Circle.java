package com.p6majo.particlesimulation;


import com.p6majo.canvas.Canvas;

import java.awt.*;

/**
 * @author AlexK
 * @version 2020-01-18
 */
public class Circle {



    /*
     **********************
     ***   attributes   ***
     **********************
     */

    private int x;
    private int y;
    private int r;
    
    private Canvas canvas;
    /*
     *********************
     ***  constructors ***
     *********************
     */

    public Circle(int x, int y, int radius, Canvas canvas){
        this.canvas = canvas;
        this.x = x;
        this.y = y;
        this.r = radius;
    }

    /*
     ***********************
     ****       getter   ***
     ***********************
     */
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
    
    public int getR(){
        return this.r;
    }
    
    public Canvas getCanvas(){
        return this.canvas;
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
    
    public boolean contains(Particle point){
        return(point.getPos().getX() >= this.x - this.r &&
               point.getPos().getX() <= this.x + this.r &&
               point.getPos().getY() >= this.y - this.r &&
               point.getPos().getY() <= this.y + this.r);
    }
    
    public boolean intersects(Circle range){
        return !(range.getX() - range.getR() > this.x + this.r ||
                 range.getX() + this.r < this.x - this.r ||
                 range.getY() - range.getR() > this.y + this.r ||
                 range.getY() + this.r < this.y - this.r);
    }
    
    public void show(){
        this.canvas.setPenColor(Color.RED);
        this.canvas.circle(x, y, r);
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
        return super.toString();
    }

}
