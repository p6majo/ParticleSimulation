package com.p6majo.physics.nbody;

import com.jogamp.opengl.util.FPSAnimator;
import com.p6majo.opengl.GLView;
import com.p6majo.physics.dynamiccoordinates.DynamicalPlot;
import com.p6majo.utils.Boundary;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static com.p6majo.physics.nbody.NBodyGL.Mode.simulation;


/**
 * @author p6majo
 * @version 2019-04-04
 */
public class NBodyGL extends JFrame implements ActionListener {

    public enum Mode {simulation,heatcapacity,correlationlength}
   private Mode mode=simulation;

    private JSlider tmpSlider;
    private JPanel nbodyDrawPanel;
    private JButton startButton;
    private JButton stopButton;
    private JButton heatCapacityButton;
    private JButton correlationButton;
    private JTextField magnetizationField;
    private JTextField temperatureField;
    private DynamicalPlot dynamicalPlot;
    private DynamicalPlot dynamicalPlot2;
    private DynamicalPlot dynamicalPlot3;
    private GLView glView;
    private NBodySimulation nbody;

    private double startTemp;
    private double endTemp;
    private double dT;


    JFrame glFrame = null;
    /*
     **********************
     ***   attributes   ***
     **********************
     */

    public void createUIComponents() {
        glView  = new GLView(500,500, nbody);
        dynamicalPlot = new DynamicalPlot(800,300);
        dynamicalPlot2 = new DynamicalPlot(400,300);
        dynamicalPlot3 = new DynamicalPlot(400,300);


    }

    /*
     *********************
     ***  constructors ***
     *********************
     */

    public NBodyGL() {
        this("NBody simulation");
    }

    public NBodyGL(String name) {
        super();

        int N=200;
        Boundary b= new Boundary(3,-000,-000,-000,1000,1000,1000);
        b.setBoundaryConditions(Boundary.BoundaryConditions.reflecting);
        nbody = new NBodySimulation(3,N,1./1000);
        nbody.setBoundary(b);


        setupUI();
        this.setContentPane(nbodyDrawPanel);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);      // closes only current window
        this.setTitle(name);
        this.pack();
        this.setJMenuBar(createMenuBar());
        this.requestFocusInWindow();
        this.setVisible(true);




        FPSAnimator animator = new FPSAnimator(glView.getCanvas(),20);


        // set scale and turn on animation

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = simulation;

                if (glFrame==null) {
                    glFrame = new JFrame("Simulation");
                    glFrame.setContentPane(glView);
                    glFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    glFrame.setSize(1600, 1600);
                    glFrame.setLocation(0, 0);
                    glFrame.setVisible(true);

                }
                nbody.start();
                animator.start();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nbody.stop();
                animator.stop();
            }
        });

        heatCapacityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        correlationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        tmpSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

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
            //save something
        }
    }


    /*
     ******************************
     ****     toString()        ***
     ******************************
     */


    public static void main(String[] args) {
        new NBodyGL();
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
        nbodyDrawPanel = new JPanel();
        nbodyDrawPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc;

        /*
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        nbodyDrawPanel.add(glView, gbc);
        */

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx=2;
        gbc.gridy=0;
        gbc.gridheight = 3;
        gbc.weighty=1;
        nbodyDrawPanel.add(buttonPanel);





        final JPanel spacer1 = new JPanel();

        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nbodyDrawPanel.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        nbodyDrawPanel.add(spacer2, gbc);
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
        nbodyDrawPanel.add(tmpSlider, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Temperature");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        nbodyDrawPanel.add(label1, gbc);


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
        nbodyDrawPanel.add(label2, gbc);
        magnetizationField = new JTextField();
        magnetizationField.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nbodyDrawPanel.add(magnetizationField, gbc);
        temperatureField = new JTextField();
        temperatureField.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nbodyDrawPanel.add(temperatureField, gbc);


        JPanel plotPanel = new JPanel();
        plotPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridheight=5;
        nbodyDrawPanel.add(plotPanel, gbc);


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




}
