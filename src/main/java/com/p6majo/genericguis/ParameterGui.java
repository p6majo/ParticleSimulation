package com.p6majo.genericguis;

import com.p6majo.parameterspace.Parameter;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * ParameterGui can be used as a standalone gui to vary a predefined set of parameters
 * The values are communicated to the registered the {@link ParameterGuiListener}
 *
 * @author p6majo
 * @version 2019-05-22
 */
public class ParameterGui extends JFrame  {




    /*
     **********************
     ***   attributes   ***
     **********************
     */


    public static enum ButtonEvent {start,stop,exit};

    private final int n;

    private final JLabel[] parameterLabel;
    private final JSlider[] parameterSlider;
    private final JRadioButton[] radios;
    private final JTextField[] parameterValue;
    private final Parameter[] parameters;
    private final Flag[] flags;

    private int addedParameter = 0;
    private int addedFlags = 0;

    private JPanel parameterGUIPanel;
    private JLabel titleLabel;
    private JPanel parameterPanel;
    private JPanel flagPanel;

    private Map<JSlider,Integer> sliderIndexMap;
    private Map<JRadioButton,Integer> radioIndexMap;

    private final ParameterGuiListener listener;

    /*
     *********************
     ***  constructors ***
     *********************
     */

    /**
     * Constructor for a ParameterGui with n parameters
     * @param n number of parameters
     * @param m number of flags
     */
    public ParameterGui(int n,int m, ParameterGuiListener listener){
        this.n = n;
        this.listener = listener;
        parameterLabel = new JLabel[n];
        parameterSlider = new JSlider[n];
        parameterValue = new JTextField[n];
        parameters = new Parameter[n];
        flags = new Flag[m];
        radios = new JRadioButton[m];
        sliderIndexMap = new HashMap<>();
        radioIndexMap = new HashMap<>();

    }


    /*
     ************************
     ****       getter    ***
     ************************
     */

    public double getValue(int index) {
        Parameter p = parameters[index];
        return p.step2value(parameterSlider[index].getValue());
    }



    /*
     ************************
     ****       setter    ***
     ************************
     */

    public void setValue(int index, double value) {
        Parameter p = parameters[index];
        parameterSlider[index].setValue(p.value2step(value));
    }

    /*
     ******************************
     ****     public methods    ***
     ******************************
     */

    public void addParameter(Parameter parameter){
        if (addedParameter<parameters.length)
            parameters[addedParameter++]=parameter;
    }

    public void addFlag(Flag flag){
        if (addedFlags<flags.length)
            flags[addedFlags++]=flag;
    }

    public void display(){

        createGui();
        this.setContentPane(parameterGUIPanel);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);      // closes only current window
        this.setTitle("ParameterGui");
        this.pack();
        this.requestFocusInWindow();
        this.setVisible(true);
    }

    /*
     ******************************
     ****     private methods   ***
     ******************************
     */

    private void createGui(){

        parameterGUIPanel = new JPanel();
        parameterGUIPanel.setLayout(new GridBagLayout());

        //title
        titleLabel = new JLabel();
        titleLabel.setText("Parameter Panel");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        parameterGUIPanel.add(titleLabel, gbc);

        //scrollpane for slider panel
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        parameterGUIPanel.add(scrollPane1, gbc);


        //slider panel panel
        parameterPanel = new JPanel();
        parameterPanel.setLayout(new GridBagLayout());
        scrollPane1.setViewportView(parameterPanel);


        //generate columns

        for (int i = 0; i < addedParameter; i++) {

            Parameter p = parameters[i];
            parameterLabel[i] = new JLabel();
            parameterLabel[i].setText(p.getLabel());
            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.2;
            gbc.anchor = GridBagConstraints.WEST;
            parameterPanel.add(parameterLabel[i], gbc);

            parameterValue[i] = new JTextField();
            gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridy = i;
            gbc.weightx = 0.2;
            parameterValue[i].setText(p.getValue() + "");
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            parameterPanel.add(parameterValue[i], gbc);

            parameterSlider[i] = new JSlider();
            gbc = new GridBagConstraints();
            gbc.gridx = 2;
            gbc.gridy = i;
            gbc.weightx = 0.6;
            parameterSlider[i].setValue(p.value2step(p.getValue()));
            parameterSlider[i].setMaximum(p.getSteps());
            parameterSlider[i].setMinimum(0);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            parameterPanel.add(parameterSlider[i], gbc);

            sliderIndexMap.put(parameterSlider[i], i);


            parameterSlider[i].addChangeListener(e -> {
                JSlider sl = (JSlider) e.getSource();
                int index = sliderIndexMap.get(sl);

                Parameter parameter = parameters[index];
                JTextField tf = parameterValue[index];

                tf.setText(parameter.step2value(sl.getValue()) + "");
                listener.valueChange(index, sl.getValue());
            });
        }

        //scrollpane for flag panel
        final JScrollPane scrollPane12 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        parameterGUIPanel.add(scrollPane12, gbc);

        //flag panel
        flagPanel = new JPanel();
        flagPanel.setLayout(new GridBagLayout());
        scrollPane12.setViewportView(flagPanel);


        //radios
        for (int i = 0; i < addedFlags; i++) {
            Flag flag = flags[i];

            JLabel flagLabel = new JLabel();
            flagLabel.setText(flag.getLabel());
            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.2;
            gbc.anchor = GridBagConstraints.WEST;
            flagPanel.add(flagLabel, gbc);

            radios[i] = new JRadioButton();
            gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridy = i;
            gbc.weightx = 0.2;
            radios[i].setSelected(flag.isSelected());
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            flagPanel.add(radios[i], gbc);
            radioIndexMap.put(radios[i],i);

            radios[i].addChangeListener(e -> {
                JRadioButton r = (JRadioButton) e.getSource();
                int index = radioIndexMap.get(r);
                listener.flagValueChange(index,r.isSelected());

                if (r.isSelected()){
                    //deselect all others
                    for (int j = 0; j < addedFlags; j++) {
                        if ((j!=index) && (radios[j].isSelected()))
                            radios[j].setSelected(false);
                    }
                }

            });

        }

        //button panel
        JPanel buttonPanel = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        parameterGUIPanel.add(buttonPanel, gbc);
        buttonPanel.setLayout(new GridBagLayout());

        JButton startButton  = new JButton();
        startButton.setText("Start");
        startButton.addActionListener(e -> listener.buttonClicked(ButtonEvent.start));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        buttonPanel.add(startButton, gbc);

        JButton stopButton  = new JButton();
        stopButton.setText("Stop");
        stopButton.addActionListener(e -> listener.buttonClicked(ButtonEvent.stop));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        buttonPanel.add(stopButton, gbc);

        JButton exitButton  = new JButton();
        exitButton.addActionListener(e -> listener.buttonClicked(ButtonEvent.exit));
        exitButton.setText("Exit");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        buttonPanel.add(exitButton, gbc);

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
