package com.devup.opointdoacai.opointdoacai.Model;

public class Config {

    private String name;
    private String valor;

    public Config() {
    }

    public Config(String name, String valor) {
        this.name = name;
        this.valor = valor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
