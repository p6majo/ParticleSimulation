package com.p6majo.physics.ising;

import com.p6majo.physics.dynamiccoordinates.DynamicalPlot;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import com.p6majo.physics.latticedraw.LatticeDraw;

import static com.p6majo.physics.ising.IsingGui.Mode.*;

/**
 * @author p6majo
 * @version 2019-04-04
 */
public class IsingGui extends JFrame implements ActionListener, IsingListener, HeatCapacityListener, CorrelationLengthListener {

    public enum Mode {simulation,heatcapacity,correlationlength}
   private Mode mode=simulation;

    private LatticeDraw latticeDraw;
    private JSlider tmpSlider;
    private JPanel isingDrawPanel;
    private JButton startButton;
    private JButton stopButton;
    private JButton heatCapacityButton;
    private JButton correlationButton;
    private JTextField magnetizationField;
    private JTextField temperatureField;
    private DynamicalPlot dynamicalPlot;
    private DynamicalPlot dynamicalPlot2;
    private DynamicalPlot dynamicalPlot3;
    private Ising ising;

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

    public IsingGui() {
        this("Ising model");


    }

    public IsingGui(String name) {
        super();
        setupUI();
        this.setContentPane(isingDrawPanel);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);      // closes only current window
        this.setTitle(name);
        this.setJMenuBar(createMenuBar());
        this.pack();
        this.requestFocusInWindow();
        this.setVisible(true);


        int N = 32;       // N-by-N lattice
        double kT = 3;    // temperature
        ising = new Ising(this, N, kT, 0.5);
        ising.addListener(this);

        // set scale and turn on animation

        latticeDraw.setXscale(0, N);
        latticeDraw.setYscale(0, N);
        latticeDraw.enableDoubleBuffering();

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = simulation;
                ising.start();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ising.stop();
            }
        });

        heatCapacityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = heatcapacity;
                startTemp = 2.1;
                endTemp=3.0;
                dT = 0.01;
                ising.equilibriate(startTemp);
                dynamicalPlot2.clear();
                dynamicalPlot2.setRange(startTemp,endTemp,0,6000);
                heatCapacityCalculation(startTemp);
            }
        });

        correlationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = correlationlength;
                ising.equilibriate();
                ising.step();
                dynamicalPlot3.clear();
                dynamicalPlot3.setRange(-1,N/2,-0.1,1);
                correlationLengthCalculation();
            }
        });

        tmpSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double temp = (double) tmpSlider.getValue() / 40;
                ising.setTemperature(temp);
                temperatureField.setText(temp + "");
            }
        });

        tmpSlider.setValue(100);
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

    /**
     * Copies offscreen buffer to onscreen buffer. There is no reason to call
     * this method unless double buffering is enabled.
     */
    public void update(boolean[][] spin) {
        latticeDraw.drawBooleanField(spin);
        magnetizationField.setText(ising.magnetization() + "");
    }


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

    private void heatCapacityCalculation(double temp){
        HeatCapacityThread heatThread = new HeatCapacityThread(ising,temp,10000,this);
        heatThread.start();
    }

    private void correlationLengthCalculation(){
        CorrelationLengthCalculationThread corThread = new CorrelationLengthCalculationThread(ising,this);
        corThread.start();
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
        new IsingGui();
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
        isingDrawPanel = new JPanel();
        isingDrawPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        isingDrawPanel.add(latticeDraw, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx=2;
        gbc.gridy=0;
        gbc.gridheight = 3;
        gbc.weighty=1;
        isingDrawPanel.add(buttonPanel);






        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        isingDrawPanel.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        isingDrawPanel.add(spacer2, gbc);
        tmpSlider = new JSlider();
        tmpSlider.setMajorTickSpacing(100);
        tmpSlider.setMaximum(200);
        tmpSlider.setMinimum(1);
        tmpSlider.setMinorTickSpacing(10);
        tmpSlider.setValue(5);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        isingDrawPanel.add(tmpSlider, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Temperature");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        isingDrawPanel.add(label1, gbc);


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
        isingDrawPanel.add(label2, gbc);
        magnetizationField = new JTextField();
        magnetizationField.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        isingDrawPanel.add(magnetizationField, gbc);
        temperatureField = new JTextField();
        temperatureField.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        isingDrawPanel.add(temperatureField, gbc);


        JPanel plotPanel = new JPanel();
        plotPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridheight=5;
        isingDrawPanel.add(plotPanel, gbc);


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
                dynamicalPlot.addData(data.time, data.magnetization, data.temperature);
                break;
            case heatcapacity:
                dynamicalPlot2.addData(data.temperature,data.heatCapacity);
                if (data.temperature != (double) tmpSlider.getValue() / 40)
                    tmpSlider.setValue((int) (data.temperature * 40));
                break;
            case correlationlength:
                dynamicalPlot3.addData(data.distance,data.correlation);
                break;
        }
    }


    @Override
    public void updateFromHeatCapacityThread(Data data) {

        System.out.println(data.heatCapacity);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                latticeDraw.drawBooleanField(ising.getSpin());
                updateValues(data);
                repaint();
            }
        });

        if (data.temperature<endTemp)
            heatCapacityCalculation(data.temperature+dT);
    }

    @Override
    public void updateFromCorrelationLengthThread(Data data) {
        this.updateValues(data);
    }

}
