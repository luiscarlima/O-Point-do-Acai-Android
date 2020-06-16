package com.devup.opointdoacai.opointdoacai.Model;


public class Comps {

    private String name;
    private String price;
    private String description;
    private boolean isSelected;

    public Comps() {
    }

    public Comps(String name, String price, String description, boolean isSelected) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
