package com.p6majo.linalg;

import com.p6majo.logger.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Matrix
 *
 * @author com.p6majo
 *
 * @date 2020-05-15 Construct matrix from Vector3D
 * @date 2020-05-15 Construct matrix from RotationAxis and angle<br>
 * (Rodrigues' formula:<link>https://en.wikipedia.org/wiki/Rodrigues%27_rotation_formula</link>)
 * @date 2020-05-22 Trace function added for square matrices
 * @date 2020-07-21 toList() function added
 * @version 2020-09-30 precision added
 */
public class Matrix implements Comparable<Matrix>{

    /*
     *************************
     *****    Statics   ******
     *************************
     */


    /**
     * returns a mxn Matrix with random integer values bound by the value b
     * @param rows m
     * @param cols n
     * @param bound b
     * @return
     */
    public static Matrix randomIntMatrix(int rows, int cols,int bound){
        Random rnd = new Random();
        Matrix returnMatrix = new Matrix(rows,cols);
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                returnMatrix.setValue(r,c,(Double) (double) (-bound+2*rnd.nextInt(bound)));

        return returnMatrix;
    }

    /**
     * returns a mxn Matrix with random integer values bound by the value b
     * @param rows m
     * @param cols n
     * @param bound b
     * @return
     */
    public static Matrix randomMatrix(int rows, int cols,double bound){
        Random rnd = new Random();
        Matrix returnMatrix = new Matrix(rows,cols);
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                returnMatrix.setValue(r,c,-bound+2.*rnd.nextDouble()*bound);

        return returnMatrix;
    }

    public static Matrix getIdentity(int dim){
        List<Double> entries = new ArrayList<>();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (i==j) entries.add(1.);
                else entries.add(0.);
            }
        }
        return new Matrix(dim,dim,entries);
    }


    /*
     *************************
     *****    Attributes   ******
     *************************
     */


    private int rows, cols;
    protected List<Double> entries ;
    protected double EPS = 1.e-6;
    private int precision = 6;
    /*
     *************************
     ***** Constructors ******
     *************************
     */


    /**
     * create ZERO matrix
     */
    public Matrix(int rows, int cols){
        this(rows,cols, Collections.nCopies(rows*cols, 0.).toArray(new Double[0]));
    }

    public Matrix(int rows, int cols, Double... entries){
        this(rows,cols, new LinkedList<>(Arrays.asList(entries)));
    }


    public Matrix(int rows, int cols, List<Double> entries){
        this.rows = rows;
        this.cols = cols;
        if (entries!=null && entries.size()==rows*cols) {
            this.entries = entries;
        }
        else{
            this.entries = new LinkedList<>();
            for (int i = 0; i < rows*cols; i++) {
                this.entries.add(0.);
            }
        }
        applyPrecision();
    }

    public Matrix(Vector3D... vecs){
        this.rows = vecs[0].entries.size();
        this.cols = vecs.length;
        this.entries = new ArrayList<>();
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                this.entries.add(vecs[i].getValue(j,0));
            }
        }
        applyPrecision();
    }


    public Matrix(List<Vector> vecs){
        this.rows = vecs.get(0).entries.size();
        this.cols = vecs.size();
        this.entries = new ArrayList<>();
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                this.entries.add(vecs.get(i).entries.get(j));
            }
        }
        applyPrecision();
    }

    /**
     * Implement Rodrigues' formula<br>
     *<a href>https://en.wikipedia.org/wiki/Rodrigues%27_rotation_formula</a>
     *
     * R = I + sin&thetasym;K+(1-cos&thetasym;)K<sup>2</sup> with K being the cross-product matrix from the rotation axis
     *
     * @param axis axis of rotation
     * @param angle angle of rotation
     *
     */
    public Matrix(Vector3D axis, double angle){
        Vector3D nAxis = axis.normalize();
        Matrix K  =new Matrix(3,3,0.,-nAxis.getZ(),nAxis.getY(),nAxis.getZ(),0.,-nAxis.getX(),-nAxis.getY(),nAxis.getX(),0.);
        Matrix rotMatrix = Matrix.getIdentity(3);
        rotMatrix = rotMatrix.sum(K.multiply(Math.sin(angle)));
        rotMatrix = rotMatrix.sum(K.multiply(K).multiply(1.-Math.cos(angle)));

        this.rows = 3;
        this.cols = 3;
        this.entries = rotMatrix.entries;
        applyPrecision();
    }


    /**
     * Create 3x1 matrix from Vector3D
     * @param vec
     */
    @Deprecated
    public Matrix(Vector3D vec){
        this.rows= 3;
        this.cols = 1;
        this.entries = new ArrayList<>();
        this.entries.add(vec.getX());
        this.entries.add(vec.getY());
        this.entries.add(vec.getZ());
    }

    /*
     **********************
     ****** Getter ********
     *********************
     */


    public Double getValue(int i, int j){
        return this.entries.get(i*this.cols +j);
    }

    public List<Double> toList(){
        return this.entries.stream().collect(Collectors.toList());
    }

    public int getRows(){return this.rows;}
    public int getColumns(){return this.cols;}


    /*
     **********************
     **** Setter **********
     **********************
     */


    /**
     * Set value at row and column
     * @param row
     * @param col
     * @param entry
     */
    public void setValue(int row, int col,Double entry){
        int pos = row*this.cols +col;
        if (pos>-1 && pos<this.entries.size()) {
            this.entries.remove(pos);
            this.entries.add(pos,entry);
        }
        else
            throw new IndexOutOfBoundsException(String.format("Matrix indices (%d,%d) out of range for (%d,%d) matrix",row,col,this.rows,this.cols));
    }

    /**
     * This value controls the precision in the range of double values.
     * Values with |v|<10<sup>-precision</sup> are set to zero automatically
     *
     * @param precision
     */
    public void setPrecision(int precision){
        this.EPS = Math.pow(10,-precision);
        this.precision = precision;
    }

    /*
     **********************
     *** special matrices *
     **********************
     */



    /*
     **********************
     *** Operations   *****
     **********************
     */

    /**
     * Calculate the trace of a square matrix
     * @return trace
     */
    public double trace(){
        if (this.rows != this.cols)
            Logger.logging(Logger.Level.error,"Cannot evaluate trace for a matrix of type ("+this.rows+"x"+this.cols+")",this);

        double trace=0.;
        for (int i = 0; i < this.rows; i++) {
            trace+=this.getValue(i,i);
        }

        return trace;
    }

    public double determinant(){
        if (this.rows==1 && this.cols==1){
            return this.entries.get(0);
        }
        else{
            double det = 0;
            int sign = 1;
            for (int c = 0; c < cols; c++) {
                det +=sign*adjoint(0,c).determinant()*getValue(0,c);
                sign*=-1;
            }
            return det;
        }
    }

    /**
     * return matrix with one row and one col removed
     * @param row
     * @param col
     * @return
     */
    public Matrix adjoint(int row, int col){
        Matrix returnMatrix = new Matrix(rows-1,cols-1);
        int rowIndex=0;
        for (int r = 0; r < rows; r++) {

            if (r!=row) {
                int colIndex = 0;
                for (int c = 0; c < cols; c++) {
                    if (c!=col) {
                        returnMatrix.setValue(rowIndex, colIndex, this.getValue(r, c));
                        colIndex++;
                    }
                }
                rowIndex++;
            }
        }
        return returnMatrix;
    }

    /**
     * TODO this is a very inefficient algorithm
     *
     * @return
     */
    public Matrix inverse(){
        List<Double> elements = new ArrayList<>();
        double det =this.determinant();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int sign = (i+j)%2==0?1:-1;
                elements.add(adjoint(j,i).determinant()*sign/det);
            }
        }
        return new Matrix(rows,cols,elements);
    }

    /**
     * Matrix addition
     * @param summand matrix S to be added
     * @return this+S
     */
    public Matrix sum(Matrix summand){
        if (this.rows == summand.rows && this.cols ==summand.cols ) {
            int dim = this.rows*this.cols;

            List<Double> sum = new ArrayList<>();

            for (int i=0;i<dim;i++)
                sum.add(this.entries.get(i)+summand.entries.get(i));

            return new Matrix(this.rows,this.cols,sum);

        }
        else
            throw new IllegalArgumentException("Rows or colums don't match, while adding to matrices");

    }


    /**
     * Negate all values of the matrix
     * @return -this
     */
    public Matrix negate() {
        List<Double> entries = new ArrayList<>();
        for (Double entry : this.entries) {
            entries.add(-entry);
        }
        return new Matrix(this.rows,this.cols,entries);
    }


    /**
     * Matrix multiplication
     * @param factor F
     * @return this*F
     */
    public Matrix multiply(Matrix factor){
        if (this.cols == factor.rows) {
            int dim = this.rows * factor.cols;

            List<Double> bproduct = new ArrayList<>();
            for (int i = 0; i < this.rows; i++)
                for (int j = 0; j < factor.cols; j++) {
                    Double sum = 0.;
                    for (int k = 0; k < this.cols; k++)
                        sum +=this.entries.get(i * this.cols + k)*factor.entries.get(k * factor.cols + j);
                    bproduct.add(sum);
                }
            return new Matrix(  this.rows, factor.cols,bproduct);
        }
        else{
            throw new IllegalArgumentException("Rows and cols do not match in matrix multiplication");
        }
    }

    /**
     * MatrixMultiplication with a Vector
     * @param factor vec
     * @return this*vec
     */
    public Vector multiply(Vector factor){
        return new Vector(multiply((Matrix) factor).entries);
    }

    /**
     * Scale all values of the matrix
     * @param scale s
     * @return this*s
     */
    public Matrix multiply(double scale){
        List<Double> entries = new ArrayList<>();
        for (Double entry : this.entries) {
            entries.add(entry*scale);
        }
        return new Matrix(this.rows, this.cols, entries);
    }

    /**
     * Matrix transposition
     * @return this<sup>T</sup>
     */
    public Matrix transpose(){
        int dim = this.rows*this.cols;
        List<Double> transposedEntries = new ArrayList<>();
        for (int i=0;i<this.rows;i++)
            for (int j = 0; j<this.cols; j++)
                transposedEntries.add(this.entries.get(j*this.cols+i));
        return new Matrix(this.cols,this.rows,transposedEntries);

    }

    /**
     * for testing purposes. It's easy to perform a zero test with this method
     * @return
     */
    public double sumOfEntriesSquared(){
        return this.entries.stream().map(x-> x*x).reduce(0.,Double::sum);
    }

    /**
     * Methods for solving a linear system of equations
     *
     */

    public Matrix rowMultiply( int i, double factor) {
        Matrix returnMatrix = this.copy();
        for (int c = 0; c < this.getColumns(); c++) {
            returnMatrix.setValue(i,c,this.getValue(i,c)*factor);
        }
        returnMatrix.applyPrecision();
        return returnMatrix;
    }

    public Matrix addMultipleOfRowToAnotherRow( double multiple, int rowSource, int rowDest) {
        Matrix returnMatrix = this.copy();
        for (int c = 0; c < this.getColumns(); c++) {
            returnMatrix.setValue(rowDest,c,this.getValue(rowSource,c)*multiple+this.getValue(rowDest,c));
        }
        returnMatrix.applyPrecision();
        return returnMatrix;
    }


    public Matrix swapRow( int i, int j) {
        int rows = this.getRows();
        int cols =this.getColumns();
        Matrix returnMatrix  =new Matrix(rows,cols,this.toList());

        for (int c = 0; c < cols; c++) {
            returnMatrix.setValue(i,c,this.getValue(j,c));
            returnMatrix.setValue(j,c,this.getValue(i,c));
        }
        return returnMatrix;
    }


    /**
     * return the number of zeroRows
     * each zero row means that the rows can be expressed as linear combination of other rows
     *
     * @return
     */
    public int checkForZeroRows() {
        return checkForZeroRows(6);
    }

    public int checkForZeroRows(int precision){
        int zeroRowCounter = 0;
        double eps = Math.pow(10.,-precision);

        List<Boolean> zeroRows = new ArrayList<>();
        for (int r = 0; r < this.getRows(); r++) {
            boolean nonzero = false;
            for (int c = 0; c < this.getColumns(); c++) {
                if (Math.abs(this.getValue(r,c))>eps) {
                    nonzero=true;
                    break;
                }
            }
            if (nonzero)
                zeroRows.add(false);
            else{
                zeroRows.add(true);
                zeroRowCounter++;
            }
        }
        return zeroRowCounter;
    }

    public int checkForZeroRows(double eps){
        int zeroRowCounter = 0;

        List<Boolean> zeroRows = new ArrayList<>();
        for (int r = 0; r < this.getRows(); r++) {
            boolean nonzero = false;
            for (int c = 0; c < this.getColumns(); c++) {
                if (Math.abs(this.getValue(r,c))>eps) {
                    nonzero=true;
                    break;
                }
            }
            if (nonzero)
                zeroRows.add(false);
            else{
                zeroRows.add(true);
                zeroRowCounter++;
            }
        }
        return zeroRowCounter;
    }

    /*
     ************************
     **** private       *****
     ************************
     */

    private void applyPrecision(){
        this.entries = entries.stream().map(x->Math.abs(x)<EPS?0:x).collect(Collectors.toList());
    }

    /*
     ************************
     **** String output *****
     ************************
     */

    public String toHtmlString(){
        String out = "<table>\n";

        for (int i=0;i<this.rows;i++){
            out += "<tr> <b>|</b>\n";
            for (int j = 0; j<this.cols; j++){
                out+="<td>"+this.entries.get(i*this.cols +j).toString()+"</td>";
            }
            out +="<b>|</b></tr>\n";
        }
        out+="</table>\n";

        return out;
    }

    public String dataString(){
        String output = "";

        for (int i=0;i<entries.size();i++)
            output+=entries.get(i).toString()+",";

        output = output.substring(0, output.length()-1);
        return output;
    }

    public String toString() {
        String out = "";
        for (int i = 0; i < this.rows; i++) {
            out += "|";
            for (int j = 0; j < this.cols; j++) {
                if (this.entries.get(i * this.cols + j) != null)
                    out += String.format(Locale.US, "%." + this.precision + "f\t", this.entries.get(i * this.cols + j));
                else
                    out += "0 ";
            }
            out = out.substring(0, out.length() - 1);//remove last tab
            out += "|\n";
        }
        return out;
    }

    public String toString(int precision){
        String out = "";

        for (int i=0;i<this.rows;i++){
            out += "|";
            for (int j = 0; j<this.cols; j++){
                if (this.entries.get(i*this.cols+j)!=null)
                    out+=String.format(Locale.US,"%."+precision+"f\t",this.entries.get(i*this.cols +j));
                else
                    out+="0 ";
            }
            out = out.substring(0,out.length()-1);//remove last tab
            out +="|\n";
        }

        return out;
    }

    public String toMathematicaString(){
        StringBuilder out = new StringBuilder("{");
        for (int i = 0; i < rows; i++) {
            out.append("{")
                    .append(entries.stream().skip(i*cols).limit(cols).map(x->x.toString()).collect(Collectors.joining(",")))
                    .append("}");
            if (i<rows-1)
                out.append(",");
        }
        out.append("}");
        return out.toString();
    }


    /*
     ********************
     *** Comparison *****
     ********************
     */


    @Override
    public int compareTo(Matrix m) {

        if (this.entries.size() == m.entries.size()){
            for (int i=0;i<this.entries.size();i++){
                int diff = this.entries.get(i).compareTo(m.entries.get(i));
                if (diff!=0)
                    return diff;
            }
            return 0;
        }
        else
            return this.entries.size() - m.entries.size();
    }

    public Matrix copy() {
        Matrix copy  =new Matrix(rows,cols,this.entries.stream().collect(Collectors.toList()));
        return copy;
    }
}
