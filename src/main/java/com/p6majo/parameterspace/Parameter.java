package com.p6majo.parameterspace;

import org.jetbrains.annotations.NotNull;

/**
 * The class Parameter which can be used as
 * as a container class for the configuration of {@link javax.swing.JSlider}.
 *
 * @author p6majo
 * @version 1.0
 * @date 2019-01-07
 */
public class Parameter implements Comparable<Parameter>{
    private final String label;
    private double value;
    private double min;
    private double max;
    private int steps;

    public Parameter(String label) {
        this.label = label;
    }

    public Parameter(String label, double value,double min,double max,int steps){
        this.label = label;
        this.value = value;
        this.min = min;
        this.max = max;
        this.steps = steps;
    }

/*********************************************/
/***           Attributes           **********/
/*********************************************/


/*********************************************/
/***           Constructors         **********/
/*********************************************/


/*********************************************/
/***           Getters              **********/
/*********************************************/
    public String getLabel() {
        return label;
    }
    public double getValue() {
        return value;
    }
    public double getMin() { return min; }
    public double getMax() { return max; }
    public int getSteps() {return steps;}

    public int value2step(double value){ return (int) ((value-min)/(max-min)*steps); }
    public double step2value(int step){return min+step*(max-min)/steps;}

/*********************************************/
/***           Setters              **********/
/*********************************************/


/*********************************************/
/***           Public methods       **********/
/*********************************************/


/*********************************************/
/***           Private methods      **********/
/*********************************************/


/*********************************************/
/***           Overrides            **********/
/*********************************************/

@Override
public int compareTo(@NotNull Parameter o) {
    return this.label.compareTo(o.label);
}
/*********************************************/
/***           toString             **********/
    /*********************************************/

    @Override
    public String toString()
    {
        String out=this.label;
        if (this.value!=0) out+="="+this.value;
        return out;
    }


}
