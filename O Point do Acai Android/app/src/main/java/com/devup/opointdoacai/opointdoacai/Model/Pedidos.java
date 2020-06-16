package com.devup.opointdoacai.opointdoacai.Model;

public class Pedidos {

    private String valorCopo;
    private String valorComp;

    public Pedidos() {
    }

    public Pedidos(String valorCopo, String valorComp) {
        this.valorCopo = valorCopo;
        this.valorComp = valorComp;
    }

    public String getValorCopo() {
        return valorCopo;
    }

    public void setValorCopo(String valorCopo) {
        this.valorCopo = valorCopo;
    }

    public String getValorComp() {
        return valorComp;
    }

    public void setValorComp(String valorComp) {
        this.valorComp = valorComp;
    }
}
