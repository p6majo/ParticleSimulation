package com.p6majo.genericguis;

/**
 * The class Flag
 *
 * @author p6majo
 * @version 2019-05-26
 */
public class Flag {

    /*
     *********************************************
     ***           Attributes           **********
     *********************************************
     */

    private final String label;
    private boolean selected;

    /*
     **********************************************
     ****           Constructors         **********
     **********************************************
     */

    public Flag(String label, boolean selected){
        this.label=label;
        this.selected = selected;
    }
    /*
     ***********************************************
     ***           Getters              ************
     ***********************************************
     */

    public String getLabel() {
        return label;
    }

    public boolean isSelected() {
        return selected;
    }


    /*
     ***********************************************
     ***           Setters              ************
     ***********************************************
     */

    public void setSelected(boolean selected) {
        this.selected = selected;
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
       return "Flag "+this.label+" set to "+ selected;
    }




}
