package com.devup.opointdoacai.opointdoacai.Model;

public class SelectedComps {

    private String compName;
    private String compPrice;

    public SelectedComps() {
    }

    public SelectedComps(String compName, String compPrice) {
        this.compName = compName;
        this.compPrice = compPrice;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompPrice() {
        return compPrice;
    }

    public void setCompPrice(String compPrice) {
        this.compPrice = compPrice;
    }
}
