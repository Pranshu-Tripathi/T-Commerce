package com.example.t_commerce.models;

public class StandardListModel {

    int standard;
    boolean isSelected;

    public StandardListModel(int s, boolean i)
    {
        this.standard = s;
        this.isSelected = i;
    }

    public int getStandard() {
        return standard;
    }

    public void setStandard(int standard) {
        this.standard = standard;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
