package com.p6majo.transformation;

import com.p6majo.logger.Logger;

import java.util.List;

/**
 * The class Transformation contains a transformation or a List of Transformation
 *
 * A transformation can be a single transformation or a List of transformations that are assumed to be a single entity.
 *
 *  A transformation can be applied to a PovrayObject and the result is a new PovrayObject.
 *
 * @author com.p6majo
 * @version 2020-07-21
 */
public class Transformation {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private List<Transformation> combination;
    private boolean isComposite ;
    private int parts ;
    private String label ="";
    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    /**
     * Constructor for a combination of transformations
     * @param combination
     */
    public Transformation(List<Transformation> combination){
        this.combination = combination;
        this.isComposite = true;
        this.parts  = combination.size();
    }

    /**
     * default constructor
     */
    public Transformation(){
        this.combination = null;
        this.isComposite = false;
        this.parts = 0;
    }

    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */


    public boolean isComposite() {
        return isComposite;
    }


    public int getParts() {
        return parts;
    }

    public Transformation get(int i){
        return this.combination.get(i);
    }

    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */

    public void setLabel(String label){
        this.label = label;
    }

    public void appendTransformation(Transformation transformation){
        if(!this.isComposite){
            Logger.logging(Logger.Level.error,"Cannot append to a single transformation. Use a combination instead.");
        }
        else{
            if (transformation.isComposite())
                this.combination.addAll(transformation.combination);
            else
                this.combination.add(transformation);
        }
    }
    /*
     ***********************************************
     ***           Public methods       ************
     ***********************************************
     */


    /**
     * Apply transformation to an object.
     * If a transformation consists of a list of transformations, each transformation of the list is applied sequentially.
     *
     * Since each object transforms differently, the method implementTransformation is specific for each object
     * and has to be implemented in the class PovrayObject.
     *
     *
     * @param object
     */
    public TransformableObject applyTo(TransformableObject object){
        TransformableObject returnObject= object.copy();
        if (combination!=null){
            for (Transformation transformation : combination)
                returnObject = transformation.applyTo(returnObject);
        }
        else
            returnObject = object.performTransformation(this);

        return returnObject;
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

    /**
     * Do not override this method. Use method setLabel() instead
     * @return
     */
    @Override
    public String toString() {
        return combination!=null?combination.toString():label.length()>0?label.toString():"Transformation";
    }

}
