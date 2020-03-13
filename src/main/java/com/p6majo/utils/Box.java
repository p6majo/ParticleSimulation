package com.p6majo.utils;


import com.p6majo.logger.Logger;

/**
 * The class Box provides a container for an {@link com.p6majo.physics.nbody.NBodySimulation}.
 * You can choose the dimension, the size of each dimension and the boundary conditions (reflecting or periodic).
 *
 * @author p6majo
 * @version 2019-03-05
 */
public class Box {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */


    public static enum BoundaryConditions {reflecting, periodic};

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
    public Box(int dim, double... size){
        this.dim = dim;
        this.size = new double[dim];
        for (int i = 0; i < Math.min(size.length,dim); i++)
            this.size[i]=size[i];

        if (size.length<dim)
            for (int i = size.length; i < dim; i++)
                this.size[i] = size[size.length-1];

        this.bc = BoundaryConditions.periodic;
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

    public double getSize(int i){
        if (i>-1 && i<this.dim)
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
        return super.toString();
    }


}
