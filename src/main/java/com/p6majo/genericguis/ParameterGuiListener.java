package com.p6majo.genericguis;


public interface ParameterGuiListener {
    void valueChange(int index, double value);
    void flagValueChange(int index, boolean selected);
    void buttonClicked(ParameterGui.ButtonEvent buttonEvent);
}
