package com.p6majo.physics.dynamiccoordinates;

import javax.swing.*;
import java.awt.*;

import static com.p6majo.physics.dynamiccoordinates.CoordinateSystem.AxesModus.*;

/**
 * The class DynamicalPlot
 *
 * @author p6majo
 * @version 2019-04-04
 */
public class DynamicalPlot extends JPanel {



    private CoordinateSystem coordinateSystem;
    private JButton clearButton;
    private JButton linearButton;
    private JButton logXButton;
    private JButton logYButton;





    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */



    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */
    public DynamicalPlot(int width, int height) {
        super();

        this.setLayout(new GridBagLayout());

        coordinateSystem = new CoordinateSystem(width,height);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        this.add(coordinateSystem, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        this.add(spacer2, gbc);
        clearButton = new JButton();
        clearButton.setText("Clear");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(clearButton, gbc);
        linearButton = new JButton();
        linearButton.setText("Linear");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(linearButton, gbc);
        logXButton = new JButton();
        logXButton.setText("LogX");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(logXButton, gbc);
        logYButton = new JButton();
        logYButton.setText("LogY");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(logYButton, gbc);



        clearButton.addActionListener(e -> coordinateSystem.clear());
        linearButton.addActionListener(e -> coordinateSystem.setAxesModus(Linear));
        logXButton.addActionListener(e -> coordinateSystem.setAxesModus(LogX));
        logYButton.addActionListener(e -> coordinateSystem.setAxesModus(LogY));
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

    public void clear(){
        coordinateSystem.clear();
    }

    public void addData(double x, double... y){
        coordinateSystem.addData(x,y);
    }

    public void setRange(double xmin,double xmax, double ymin,double ymax){
        this.coordinateSystem.setRange(xmin,xmax,ymin,ymax);

    }
    /*
     ***********************************************
     ***           Public methods       ************
     ***********************************************
     */


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
