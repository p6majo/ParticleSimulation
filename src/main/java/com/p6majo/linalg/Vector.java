package com.p6majo.linalg;

import com.p6majo.logger.Logger;

import java.util.*;
import java.util.stream.Collectors;


public class Vector extends Matrix {

    private List<String> tags; //for debugging

    private static List<Double> getListOfZeros(int number){
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            list.add(0.);
        }
        return list;
    }

    //collect 200 primes, to calculate hashCodes
    //generated with mathematica: Table[Prime[1000000 + 10000*i], {i, 1, 200}]
    private static int[] hashPrimes=new int[]{
            15650303, 15816469, 15981557, 16147561, 16313527, 16480501, 16646677,
            16812361, 16977973, 17144489, 17311727, 17477423, 17643037, 17810803,
            17977291, 18145249, 18312037, 18480347, 18647243, 18815231, 18982883,
            19150577, 19318703, 19485533, 19654991, 19822093, 19989791, 20158049,
            20326759, 20495843, 20664361, 20832433, 21000853, 21169831, 21338609,
            21506941, 21675881, 21844297, 22012453, 22182343, 22353031, 22522919,
            22691443, 22860683, 23029399, 23199721, 23368379, 23538737, 23708479,
            23879519, 24048797, 24218651, 24387893, 24557887, 24727249, 24898427,
            25069267, 25241299, 25411777, 25582153, 25752917, 25923059, 26093519,
            26265469, 26434907, 26605609, 26777507, 26948063, 27118249, 27290279,
            27461653, 27632113, 27803969, 27974867, 28146689, 28317637, 28490171,
            28660867, 28833901, 29005541, 29178257, 29349643, 29522497, 29693977,
            29866219, 30036287, 30207979, 30379903, 30552593, 30723761, 30895807,
            31070219, 31243013, 31415641, 31589027, 31762387, 31934521, 32107171,
            32280191, 32452843, 32625209, 32798153, 32970967, 33145117, 33317371,
            33489689, 33663871, 33836479, 34010831, 34186049, 34361009, 34534007,
            34709251, 34882457, 35056607, 35230241, 35403397, 35577769, 35751311,
            35926307, 36099559, 36273761, 36447503, 36621419, 36796379, 36971861,
            37143913, 37319687, 37493747, 37667659, 37842263, 38017867, 38190907,
            38365139, 38539471, 38714369, 38887747, 39063091, 39237481, 39410867,
            39585587, 39759641, 39935213, 40111087, 40285789, 40460741, 40637147,
            40810697, 40986793, 41161739, 41337427, 41513261, 41690101, 41864377,
            42042061, 42216827, 42391303, 42567113, 42743089, 42920191, 43095979,
            43272461, 43447373, 43623149, 43800299, 43975717, 44152781, 44328307,
            44504843, 44680319, 44856223, 45032437, 45208589, 45382451, 45558553,
            45734873, 45910519, 46086977, 46264321, 46441207, 46617629, 46793869,
            46970699, 47147459, 47323781, 47500631, 47678909, 47855287, 48032629,
            48210713, 48387587, 48565117, 48742583, 48919771, 49096849, 49273369,
            49449973, 49625413, 49803113, 49979687};

    public static Vector randomVector(int dim,double limit){
        return new Vector(Matrix.randomMatrix(dim,1,limit).entries);
    }
    public static Vector midPoint(Vector one, Vector two){
        return new Vector(one.sum(two).multiply(0.5).entries);
    }
    public static Vector getZero(int dim){
        Double[] zeros = new Double[dim];
        for (int i = 0; i < zeros.length; i++) zeros[i]=0.;
        return new Vector(Arrays.asList(zeros));
    }

    /**
     * Parse vector from format "(x|y|z|...)"
     * @param vectorString
     * @return
     */
    public static Vector parse(String vectorString){
        vectorString = vectorString.trim();
        vectorString = vectorString.substring(1,vectorString.length()-1); //remove parenthesis

        StringTokenizer tokenizer = new StringTokenizer(vectorString,"|");

        Double[] components = new Double[tokenizer.countTokens()];
        for (int i = 0; i < components.length; i++) {
            components[i] = Double.parseDouble(tokenizer.nextToken());
        }
        return new Vector(components);
    }
    /*
     *************************
     ***** Constructors ******
     *************************
     */


    /**
     * create ZERO vector
     */
    public Vector(int rows){
        super(rows,1,getListOfZeros(rows));
        tags = new ArrayList<>();
    }

    public Vector(Double... components){
        super(components.length,1,Arrays.asList(components));
        tags = new ArrayList<>();
    }

    public Vector(List<Double> entries){
        super(entries.size(),1,entries);
        tags = new ArrayList<>();
    }

    /*
     **********************
     ****** Getter ********
     *********************
     */

    public List<String> getTags(){ return tags; }

    public double getValue(int index){
        return super.getValue(index,0);
    }
    public List<Double> toList(){
        List<Double> components = new ArrayList<>();
        components.addAll(entries);
        return components;
    }
    public int getDim(){return this.entries.size(); }

    public double getX(){return entries.get(0);}
    public double getY(){return entries.get(1);}
    public double getZ(){return entries.get(2);}
    public double getW(){return entries.get(3);}

    /*
     **********************
     **** Setter **********
     **********************
     */

    public void addTag(String tag){
        tags.add(tag);
    }

    public void addTags(List<String> tags){
        this.tags.addAll(tags);
    }

    /*
     **********************
     *** Operations   *****
     **********************
     */

    @Override
    public Vector sum(Matrix other){
        return new Vector(super.sum(other).entries);
    }

    public Vector add(Matrix other){
        return new Vector(super.sum(other).entries);
    }
    public Vector sub(Matrix other){
        return new Vector(super.sum(other.negate()).entries);
    }
    public Vector neg(){return this.mul(-1.);}

    public Double getComp(int i){
        if (i<0 || i>this.entries.size()-1)
            return null;
        return this.entries.get(i);
    }


    public double dot(Vector factor){
            if (this.entries.size() != factor.entries.size())
                Logger.logging(Logger.Level.error, "Dot product of vectors with different dimensions " + this + " and " + factor);

        double sum = 0;
        for (int i = 0; i < entries.size(); i++) {
            sum+=entries.get(i)*factor.entries.get(i);
        }
        return sum;
    }

    public double angleTo(Vector v){
        return Math.acos(this.dot(v)/Math.sqrt(v.getNorm2()*this.getNorm2()));
    }

    public double getNorm2(){
        return entries.stream().collect(Collectors.summingDouble(x->x*x));
    }

    public double length(){
        return Math.sqrt(this.getNorm2());
    }

    public Vector normalize(){
        return this.mul(1./this.length());
    }

    public Vector mul(double scale){
        return new Vector(this.entries.stream().map(x->x*scale).collect(Collectors.toList()));
    }

    public double getDistance(Vector point){
        if (this.entries.size()!=point.entries.size())
            Logger.logging(Logger.Level.error,"Attempt to measure distances between vectors of different dimension: "+this+" and "+point);

        return Math.sqrt(directionTo(point).getNorm2());
    }

    /**
     * Calculate the direction vector from this to point
     * @param point
     * @return
     */
    public Vector directionTo(Vector point){
        return point.sum(this.negate());
    }

    public Vector linMap(Matrix m){
        Matrix newVec = m.multiply(this);
        return new Vector(newVec.entries);
    }


    public Vector3D toVector3D(){
        if (entries.size()<3) return null;
        return new Vector3D(entries.get(0),entries.get(1),entries.get(2));
    }


    /**
     * Create a hash code that is relatively unique
     * first for significant figures are considered
     * @return
     */
    @Override
    public int hashCode() {
        double EPS = 1.e-4;
        int sum = 0;
        for (int i = 0; i < entries.size(); i++) {
            double entry = Math.round(entries.get(i)/EPS)*EPS; //remove digits beyond precision
            double summand = Math.round(entry*hashPrimes[7*i%200])/100;
            sum+=(int) summand ;
        }
        return sum;
    }



    @Override
    public boolean equals(Object other){
        return this.hashCode()==other.hashCode();
    }

    /*
     ************************
     **** String output *****
     ************************
     */

    @Override
    public String toString() {
        return "("+this.entries.stream().map(x->String.format(Locale.US,"%.6f",x)).collect(Collectors.joining("|")) +")";
    }

    public String toString(int precision) {
        return "("+this.entries.stream().map(x->String.format(Locale.US,"%."+precision+"f",x)).collect(Collectors.joining("|")) +")";
    }

    public String toMathematicaString() {
        return "{"+this.entries.stream().map(x->String.format(Locale.US,"%.6f",x)).collect(Collectors.joining(",")) +"}";
    }

    public String toLatexString(int precision){
        StringBuilder out = new StringBuilder();
        out.append("\\left(\n")
                .append("\\begin{array}{c}\n");
        this.entries.stream().forEach(x->out.append("\t"+String.format("%."+precision+"f\\\\\n",x)));
        out.append("\\end{array}\n")
                .append("\\right)\n");
        return out.toString();
    }

    public String toLatexPositionString(int precision){
        StringBuilder out = new StringBuilder();
        out.append("\\left(\n");
        this.entries.stream().forEach(x->out.append("\t"+String.format("%."+precision+"f\\\\\n",x)));
        out.append("\\right)\n");
        return out.toString();
    }


    /*
     ********************
     *** Comparison *****
     ********************
     */


    @Override
    public Vector copy(){
        return new Vector(entries);
    }
}
