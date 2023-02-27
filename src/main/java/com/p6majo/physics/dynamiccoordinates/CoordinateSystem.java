package com.p6majo.physics.dynamiccoordinates;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

import static com.p6majo.physics.dynamiccoordinates.CoordinateSystem.AxesModus.*;

/**
 * A coordinate system that allows to plot a set of functions evalutated over the same domain
 *
 * @author p6majo
 * @version 2019-04-09
 */
public class CoordinateSystem extends JPanel {


    /*
     **************************************************
     ***   private classes: Container for the data  ***
     **************************************************
     */

    private class Point{
        public double x;
        public double [] y;


        public Point(double x, double... y){
            this.x=x;
            this.y=y;
        }

        public Point(int n){
            this.x = 0;
            this.y = new double[n];
        }
    }
    private class MaxPoint extends Point{
        public MaxPoint(int n){
            super(n);
            this.x = -Double.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                this.y[i]=-Double.MAX_VALUE;
            }
        }

        /**
         * change the number of functions
         * keeping the existing maximal values
         * @param n
         * @param old provide existing maximal values
         */
        public MaxPoint(int n,MaxPoint old){
            super(n);
            this.x = old.x;
            for (int i = 0; i < Math.min(n,old.y.length); i++)
                this.y[i]=old.y[i];
        }
    }
    private class MinPoint extends Point{
        public MinPoint(int n){
            super(n);
            this.x = Double.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                this.y[i]=Double.MAX_VALUE;
            }
        }

        /**
         * change the number of functions
         * keeping the existing minimal values
         * @param n
         * @param old provide existing minimal values
         */
        public MinPoint(int n,MinPoint old){
            super(n);
            this.x = old.x;
            for (int i = 0; i < Math.min(n,old.y.length); i++)
                this.y[i]=old.y[i];
        }
    }

    /*
     **********************
     ***   attributes   ***
     **********************
     */
    //view components
    private com.p6majo.physics.latticedraw.Canvas canvas;
    private JLabel xMinLabel;
    private JLabel xMaxLabel;
    private JLabel yMin1Label;
    private JLabel yMax1Label;
    private JLabel yMax2Label;
    private JLabel yMin2Label;

    public static enum AxesModus {LogLog, LogX, LogY, Linear}

    private final float invLg10 = (float) (1. / Math.log(10));
    private final float[] logSubTics = new float[]{0.3010f, 0.47712f, 0.6021f, 0.69897f, 0.77815f, 0.8451f, 0.90309f, 0.95424f};

    private AxesModus axesModus;

    private int plotHeight;
    private int plotWidth;

    private int skip = 0;
    private int skipLength = 1;
    private int numberOfFunctions = 1;
    private Color[] colors = new Color[]{Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW,Color.CYAN,Color.MAGENTA, Color.DARK_GRAY,Color.ORANGE,Color.PINK};
    private boolean changePlotDimensions = false;
    private int lastPoint = 0;



    private java.util.List<Point> data;
    private MaxPoint max;
    private MinPoint min;

    private boolean fixedRange = false;

    /*
     *********************
     ***  constructors ***
     *********************
     */
    public CoordinateSystem(int width, int height){

        super();
        GridBagConstraints gbc;
        this.setLayout(new GridBagLayout());


        //row 0

        yMax1Label = new JLabel();
        yMax1Label.setText("0.0");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty=0;
        gbc.weightx=0;
        this.add(yMax1Label, gbc);



        yMax2Label = new JLabel();
        yMax2Label.setText("0.0");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weighty=0;
        gbc.weightx=0;
        this.add(yMax2Label, gbc);


        //row 1

        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.weightx=0;
        gbc.fill = GridBagConstraints.VERTICAL;
        this.add(spacer2, gbc);

        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.weightx=0;
        gbc.fill = GridBagConstraints.VERTICAL;
        this.add(spacer3, gbc);


        //row 2
        yMin1Label = new JLabel();
        yMin1Label.setText("0.0");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty=0;
        gbc.weightx=0;
        this.add(yMin1Label, gbc);


        yMin2Label = new JLabel();
        yMin2Label.setText("0.0");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.weighty=0;
        gbc.weightx=0;
        this.add(yMin2Label, gbc);

        //row 3
        xMinLabel = new JLabel();
        xMinLabel.setText("0.0");

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weighty=0;
        gbc.weightx=0;
        this.add(xMinLabel, gbc);

        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty=0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(spacer1, gbc);

        xMaxLabel = new JLabel();
        xMaxLabel.setText("0.0");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.weighty=0;
        gbc.weightx=0;
        gbc.anchor = GridBagConstraints.WEST;
        this.add(xMaxLabel, gbc);

        canvas = new com.p6majo.physics.latticedraw.Canvas();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.gridheight=3;
        gbc.weighty=1.0;
        gbc.weightx=1.0;
        this.plotHeight=height;
        this.plotWidth =width;

        canvas.setCanvasSize(width,height);
        canvas.setXscale(0,width);
        canvas.setYscale(0,height);
        canvas.setDoubleBuffered(true);

        gbc.fill = GridBagConstraints.BOTH;
        this.add(canvas, gbc);

        this.data = new ArrayList<>();
        max = new MaxPoint(this.numberOfFunctions);
        min = new MinPoint(this.numberOfFunctions);

        axesModus = Linear;
    }

    /*
     ************************
     ****       getter    ***
     ************************
     */

    /*
     ************************
     ****       setter    ***
     ************************
     */

    public void clear(){
        this.numberOfFunctions=1; //default value
        data.clear();
        canvas.clear();
        max = new MaxPoint(1);
        min = new MinPoint(1);
    }

    public void setAxesModus(AxesModus modus){
        if (modus==Linear)
            axesModus=Linear;
        else if (modus==LogX){
            if (axesModus==Linear)
                axesModus=LogX;
            else if (axesModus==LogY)
                axesModus=LogLog;
        }
        else if (modus==LogY){
            if (axesModus==Linear)
                axesModus=LogY;
            else if (axesModus==LogX)
                axesModus=LogLog;
        }
    }

    public void setRange(double xmin,double xmax,double ymin,double ymax){
        //this.fixedRange = true;
        this.min = new MinPoint(1);
        this.min.x = xmin;
        this.min.y[0] = ymin;

        this.max = new MaxPoint(1);
        this.max.x = xmax;
        this.max.y[0] = ymax;

        refreshLabel();
        //plotGrid(xmin,min.y,xmax,max.y,(xmax-xmin)/plotWidth,new double[]{(max.y[0]-min.y[0])/plotHeight});
    }

    /*
     ******************************
     ****     public methods    ***
     ******************************
     */

    public void addData(double x,double... y){
        int n = y.length;

        Point point;
        if (n<this.numberOfFunctions){
            double[] tmpY = new double[n];
            for (int i = 0; i < y.length; i++)
                tmpY[i] = y[i];
            point = new Point(x,tmpY);
        }
        else if (n>this.numberOfFunctions){
            this.numberOfFunctions = n;
            MaxPoint tmpMax = new MaxPoint(n,max);
            MinPoint tmpMin = new MinPoint(n,min);
            min = tmpMin;
            max = tmpMax;
            point = new Point(x,y);
        }
        else
            point = new Point(x,y);


        if (!fixedRange)
            updateRange(x,y);
        skip++;
        if (skip>=skipLength) {
            this.data.add(point);
            skip=0;
        }
        if (data.size()>plotWidth) {
            reduceData();
            skipLength*=2;
            refreshLabel();
        }

        updatePlot();

    }

    /*
     ******************************
     ****     private methods   ***
     ******************************
     */
    private void updateRange(double x,double... y) {
        if (x > max.x) {
            max.x = x;
            changePlotDimensions = true;
        }
        if (x < min.x) {
            min.x = x;
            changePlotDimensions = true;
        }
        for (int i = 0; i < y.length; i++) {
            if (y[i] > max.y[i]) {
                //calculate the next full decimal value 0.075->0.1
                double p = Math.ceil(Math.log(Math.abs(y[i])) * invLg10);
                max.y[i] = Math.signum(y[i]) * Math.pow(10, p);
                changePlotDimensions = true;
            }
            if ( y[i] < min.y[i]) {
                //calculate the next full decimal value 0.075->0.1
                double p = Math.ceil(Math.log(Math.abs(y[i])) * invLg10);
                min.y[i] = Math.signum(y[i]) * Math.pow(10, p);
                changePlotDimensions = true;
            }

        }

        if (changePlotDimensions)
            refreshLabel();

    }

    private void refreshLabel(){
        xMinLabel.setText(min.x+"");
        xMaxLabel.setText(max.x+"");
        yMin1Label.setText(min.y[0]+"");
        yMax1Label.setText(max.y[0]+"");
        if (numberOfFunctions>1){
            yMin2Label.setText(min.y[1]+"");
            yMax2Label.setText(max.y[1]+"");
        }
    }

    private void updatePlot() {

        if (data.size() == 1 && !fixedRange) {
            if (max.x==min.x)
                max.x *= plotWidth;//assume one pixel for each incoming data point
            changePlotDimensions = true;
        }
        else if (data.size() > 2) {

            double xPlotMin = min.x;
            double[] yPlotMin = min.y;
            double xPlotMax = max.x;
            double[] yPlotMax = max.y;

            switch (axesModus) {
                case LogLog:
                    for (int i = 0; i < numberOfFunctions; i++) {
                        yPlotMax[i] = Math.log(max.y[i]) * invLg10;
                        yPlotMin[i] = Math.log(min.y[i]) * invLg10;
                    }
                    xPlotMin = Math.log(min.x) * invLg10;
                    xPlotMax = Math.round(Math.log(max.x) * invLg10 + 0.49f);
                case LogY:
                    for (int i = 0; i < numberOfFunctions; i++) {
                        yPlotMax[i] = Math.log(max.y[i]) * invLg10;
                        yPlotMin[i] = Math.log(min.y[i]) * invLg10;
                    }

            }

            float dx = (float) (plotWidth / (max.x - min.x));
            double[] dy = new double[numberOfFunctions];
            for (int i = 0; i < numberOfFunctions; i++)
                dy[i] = (float) (plotHeight / 1.01f / (yPlotMax[i] - yPlotMin[i]));


            if (changePlotDimensions) {
                canvas.clear();
                lastPoint = 0;
                plotGrid(xPlotMin, yPlotMin, xPlotMax, yPlotMax, dx, dy);
                changePlotDimensions = false;
            }


            for (int k = 0; k < numberOfFunctions; k++) {
                canvas.setPenColor(colors[k]);

                switch (axesModus) {
                    case LogLog:
                        for (int i = lastPoint; i < data.size() - 1; i++) {
                            canvas.line((Math.log(data.get(i).x) * invLg10 - xPlotMin) * dx, (Math.log(data.get(i).y[k]) * invLg10 - yPlotMin[k]) * dy[k], (Math.log(data.get(i + 1).x) * invLg10 - xPlotMin) * dx, (Math.log(data.get(i + 1).y[k]) * invLg10 - yPlotMin[k]) * dy[k]);
                        }
                        break;
                    case LogY:
                        for (int i = lastPoint; i < data.size() - 1; i++) {
                            canvas.line((data.get(i).x - xPlotMin) * dx, (Math.log(data.get(i).y[k]) * invLg10 - yPlotMin[k]) * dy[k], (data.get(i + 1).x - xPlotMin) * dx, (Math.log(data.get(i + 1).y[k]) * invLg10 - yPlotMin[k]) * dy[k]);
                        }
                        break;
                    case Linear:
                        for (int i = lastPoint; i < data.size() - 1; i++) {
                            canvas.line((data.get(i).x - xPlotMin) * dx, (data.get(i).y[k] - yPlotMin[k]) * dy[k], (data.get(i + 1).x - xPlotMin) * dx, (data.get(i + 1).y[k] - yPlotMin[k]) * dy[k]);
                        }
                        break;

                }
            }
            lastPoint = data.size()-1;//drawBooleanField only last part of the plot
            canvas.update();
        }
    }


    private void plotGrid(double xmin, double[] ymin, double xmax, double[] ymax, double dx, double[] dy) {
        switch (axesModus) {
            case LogLog:

                double x = Math.floor(xmin);
                while (x < xmax) {
                    canvas.setPenColor(Color.GRAY);
                    canvas.line((x - xmin) * dx, 0, (x - xmin) * dx, plotHeight);
                    canvas.setPenColor(Color.lightGray);
                    for (float subX : this.logSubTics)
                        canvas.line((x + subX - xmin) * dx, 0, (x + subX - xmin) * dx, plotHeight);
                    x += 1;
                }

                for (int i = 0; i < numberOfFunctions; i++) {
                    double y = Math.floor(ymin[i]);
                    while (y < ymax[i]) {
                        canvas.setPenColor(colors[i]);
                        canvas.line(0, ((y - ymin[i])) * dy[i], plotWidth, ((y - ymin[i])) * dy[i]);
                        for (float subY : this.logSubTics)
                            canvas.line(0, ((y + subY - ymin[i])) * dy[i], plotWidth, ((y + subY - ymin[i])) * dy[i]);
                        y += 1;
                    }
                }
                break;

            case LogY:
                x =  Math.floor(xmin);
                double dX =  Math.pow(10,Math.floor(Math.log(xmax)*invLg10));
                while (x < xmax) {
                    canvas.setPenColor(Color.lightGray);
                    canvas.line((x - xmin) * dx, 0, (x - xmin) * dx, plotHeight);
                    x += dX;
                }
                for (int i = 0; i < numberOfFunctions; i++) {
                    double y = Math.floor(ymin[i]);
                    while (y < ymax[i]) {
                        canvas.setPenColor(colors[i]);
                        canvas.line(0, ((y - ymin[i])) * dy[i], plotWidth, ((y - ymin[i])) * dy[i]);
                        for (float subY : this.logSubTics)
                            canvas.line(0, ((y + subY - ymin[i])) * dy[i], plotWidth, ((y + subY - ymin[i])) * dy[i]);
                        y += 1;
                    }
                }
                break;

            case LogX:
                for (int i = 0; i < numberOfFunctions; i++) {
                    double y = Math.floor(ymin[i]);
                    double dY = Math.pow(10, Math.floor(Math.log(ymax[i]) * invLg10));
                    while (y < ymax[i]) {
                        canvas.setPenColor(colors[i]);
                        canvas.line(0, ((y - ymin[i])) * dy[i], plotWidth, ((y - ymin[i])) * dy[i]);
                        y += dY;
                    }
                }
                x =  Math.floor(xmin);
                while (x < xmax) {
                    canvas.setPenColor(Color.GRAY);
                    canvas.line((x - xmin) * dx, 0, (x - xmin) * dx, plotHeight);
                    canvas.setPenColor(Color.lightGray);
                    for (float subX : this.logSubTics)
                        canvas.line((x + subX - xmin) * dx, 0, (x + subX - xmin) * dx, plotHeight);
                    x += 1;
                }
                break;
            case Linear:
                x =  Math.floor(xmin);
                dX =  Math.pow(10,Math.floor(Math.log(xmax)*invLg10));
                while (x < xmax) {
                    canvas.setPenColor(Color.lightGray);
                    canvas.line((x - xmin) * dx, 0, (x - xmin) * dx, plotHeight);
                    x += dX;
                }
                for (int i = 0; i < numberOfFunctions; i++) {
                    double y = Math.floor(ymin[i]);
                    double dY = Math.pow(10, Math.floor(Math.log(ymax[i]) * invLg10));
                    while (y < ymax[i]) {
                        canvas.setPenColor(colors[i]);
                        canvas.line(0, ((y - ymin[i])) * dy[i], plotWidth, ((y - ymin[i])) * dy[i]);
                        y += dY;
                    }
                }
                break;
        }
    }

    private  void reduceData(){
        java.util.List<Point> reducedData = new ArrayList<>();
        max.x*=2;
        for (int i = 0; i < data.size(); i+=2) {
            reducedData.add(data.get(i));
        }
        data  = reducedData;

        changePlotDimensions = true;
    }
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
