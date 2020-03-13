package com.p6majo.physics.brownianmotion;


import com.p6majo.physics.dynamiccoordinates.DynamicalPlot;
import com.p6majo.physics.latticedraw.LatticeDraw;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * @author p6majo
 * @version 2019-05-09
 */
public class BrownianMotionGui extends JFrame implements ActionListener, BrownianMotionListener {
    public enum Mode {simulation}
    private Mode mode= Mode.simulation;

    private LatticeDraw latticeDraw;
    private JSlider probabilitySlider;
    private JPanel bmDrawPanel;
    private JButton startButton;
    private JButton stopButton;
    private JButton heatCapacityButton;
    private JButton correlationButton;
    private JTextField magnetizationField;
    private JTextField temperatureField;
    private DynamicalPlot dynamicalPlot;
    private DynamicalPlot dynamicalPlot2;
    private DynamicalPlot dynamicalPlot3;
    private BrownianMotion bm;

    private double startTemp;
    private double endTemp;
    private double dT;

    /*
     **********************
     ***   attributes   ***
     **********************
     */

    public void createUIComponents() {
        latticeDraw = new LatticeDraw();
        latticeDraw.setCanvasSize(512, 512);
        dynamicalPlot = new DynamicalPlot(800,300);
        dynamicalPlot2 = new DynamicalPlot(400,300);
        dynamicalPlot3 = new DynamicalPlot(400,300);


    }

    /*
     *********************
     ***  constructors ***
     *********************
     */

    public BrownianMotionGui() {
        this("Brownian motion");


    }

    public BrownianMotionGui(String name) {
        super();
        setupUI();
        this.setContentPane(bmDrawPanel);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);      // closes only current window
        this.setTitle(name);
        this.setJMenuBar(createMenuBar());
        this.pack();
        this.requestFocusInWindow();
        this.setVisible(true);


        int N = 2*64;       // N-by-N lattice

        bm = new BrownianMotion(N,0.25);
        bm.addListener(this);

        latticeDraw.setXscale(0, N);
        latticeDraw.setYscale(0, N);
        latticeDraw.enableDoubleBuffering();

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = Mode.simulation;
                bm.start();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bm.stop();
            }
        });



        probabilitySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double temp = (double) probabilitySlider.getValue() /100;
                bm.setProbability(temp);
                temperatureField.setText(temp + "");
            }
        });

        probabilitySlider.setValue(5);
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


    /**
     * Sets the upper-left hand corner of the drawing window to be (x, y), where (0, 0) is upper left.
     *
     * @param x the number of pixels from the left
     * @param y the number of pixels from the top
     * @throws IllegalArgumentException if the width or height is 0 or negative
     */
    public void setLocationOnScreen(int x, int y) {
        if (x <= 0 || y <= 0) throw new IllegalArgumentException();
        this.setLocation(x, y);
    }



    /*
     ******************************
     ****     public methods    ***
     ******************************
     */

    /*
     ******************************
     ****     private methods   ***
     ******************************
     */

    // create the menu bar (changed to private)
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        JMenuItem menuItem1 = new JMenuItem(" Save...   ");
        menuItem1.addActionListener(this);
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menu.add(menuItem1);
        return menuBar;
    }



    /*
     ******************************
     ****     overrides         ***
     ******************************
     */

    /**
     * This method cannot be called directly.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        FileDialog chooser = new FileDialog(this, "Use a .png or .jpg extension", FileDialog.SAVE);
        chooser.setVisible(true);
        String filename = chooser.getFile();
        if (filename != null) {
            latticeDraw.save(chooser.getDirectory() + File.separator + chooser.getFile());
        }
    }


    /*
     ******************************
     ****     toString()        ***
     ******************************
     */


    public static void main(String[] args) {
        new BrownianMotionGui();
    }

    @Override
    public String toString() {
        return super.toString();
    }


    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void setupUI() {
        createUIComponents();
        bmDrawPanel = new JPanel();
        bmDrawPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        bmDrawPanel.add(latticeDraw, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx=2;
        gbc.gridy=0;
        gbc.gridheight = 3;
        gbc.weighty=1;
        bmDrawPanel.add(buttonPanel);






        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bmDrawPanel.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        bmDrawPanel.add(spacer2, gbc);
        probabilitySlider = new JSlider();
        probabilitySlider.setMajorTickSpacing(100);
        probabilitySlider.setMaximum(100);
        probabilitySlider.setMinimum(1);
        probabilitySlider.setMinorTickSpacing(10);
        probabilitySlider.setValue(5);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bmDrawPanel.add(probabilitySlider, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Temperature");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        bmDrawPanel.add(label1, gbc);


        startButton = new JButton();
        startButton.setText("Start");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(startButton, gbc);
        stopButton = new JButton();
        stopButton.setText("Stop");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(stopButton, gbc);
        heatCapacityButton = new JButton();
        heatCapacityButton.setText("Cv");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(heatCapacityButton, gbc);
        correlationButton = new JButton();
        correlationButton.setText("Correlation");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(correlationButton, gbc);

        final JLabel label2 = new JLabel();
        label2.setText("Magnetization");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        bmDrawPanel.add(label2, gbc);
        magnetizationField = new JTextField();
        magnetizationField.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bmDrawPanel.add(magnetizationField, gbc);
        temperatureField = new JTextField();
        temperatureField.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bmDrawPanel.add(temperatureField, gbc);


        JPanel plotPanel = new JPanel();
        plotPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridheight=5;
        bmDrawPanel.add(plotPanel, gbc);


        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.gridwidth=2;
        plotPanel.add(dynamicalPlot, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        plotPanel.add(dynamicalPlot2, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        plotPanel.add(dynamicalPlot3, gbc);
    }


    @Override
    public void updateValues(Data data) {
        switch(mode){
            case simulation:
                latticeDraw.drawDouble01Field(data.world);
                break;
        }
    }


}
