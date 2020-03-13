package com.p6majo.utils;

import com.p6majo.logger.Logger;
import com.p6majo.opengl.GLView;

import static com.p6majo.utils.Boundary.BoundaryConditions.periodic;

/**
 * The class Boundary provides a container for an {@link com.p6majo.physics.nbody.Simulation}.
 * You can choose the dimension, the size of each dimension and the boundary conditions (reflecting or periodic).
 *
 * @author p6majo
 * @version 2019-03-05
 */
public class Boundary {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */


    public static enum BoundaryConditions {none,reflecting, periodic};

    private final int dim;
    private double[] size;
    private BoundaryConditions bc;


    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    /**
     * Construct a box with a given dimension and a number of sizes for each dimensions
     * @param dim the dimension of the box
     * @param size one double number for each dimension,
     *             if there are fewer numbers than dimensions the last number fills the remaining dimensions.
     */
    public Boundary(int dim, double... size){
        this.dim = dim;
        this.size = new double[2*dim];

        if (size.length==2*dim){
            for (int i = 0; i < size.length; i++)
                this.size[i]=size[i];
        }
        else if(size.length ==dim) {
            for (int i = 0; i < size.length; i++)
                this.size[dim+i] = size[i];
        }
        else{
            Logger.logging(Logger.Level.error,"Number of dimensions doesn't match the number of sizes provided, which should be equivalent in number or twice as large.");
        }

        this.bc = periodic;
    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */



    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */

    public void setBoundaryConditions(BoundaryConditions bc){
        this.bc = bc;

    }

    /*
     ***********************************************
     ***           Public methods       ************
     ***********************************************
     */

    public int getDim() {
        return dim;
    }

    public BoundaryConditions getBc() {
        return bc;
    }

    public double[] getSize(){
        return this.size;
    }

    public double getMaxSize(int i){
        if (i>-1 && i<this.dim)
            return this.size[this.dim+i];
        Logger.logging(Logger.Level.error,"Unallowed request for size of the dimension.");
        return -1;
    }

    public double getMinSize(int i){
        if (i>-1 && i<this.dim)
            return this.size[i];
        Logger.logging(Logger.Level.error,"Unallowed request for size of the dimension.");
        return -1;
    }

    public double getSize(int i){
        if (i>-1 && i<2*this.dim)
            return this.size[i];
        Logger.logging(Logger.Level.error,"Unallowed request for size of the dimension.");
        return -1;
    }
    /*
     ***********************************************
     ***           Private methods      ************
     ***********************************************
     */


    /*
     ***********************************************
     ***           Overrides            ************
     ***********************************************
     */

    /*
     ***********************************************
     ***           toString             ************
     ***********************************************
     */

    @Override
    public String toString() {
        String out = "Box of dimension "+this.dim+"\n";
        for (int i = 0; i < this.dim; i++) {
            out+=this.getSize(i)+" .. "+this.getSize(this.dim+i)+"\n";
        }
        return out;
    }


}
