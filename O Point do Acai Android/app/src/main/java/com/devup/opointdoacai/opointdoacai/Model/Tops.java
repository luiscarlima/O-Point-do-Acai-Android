package com.devup.opointdoacai.opointdoacai.Model;

public class Tops {

    private String name;
    private String description;
    private String price_one;
    private String price_two;
    private String price_three;
    private String top_number;

    public Tops() {
    }

    public Tops(String name, String description, String price_one, String price_two, String price_three, String top_number) {
        this.name = name;
        this.description = description;
        this.price_one = price_one;
        this.price_two = price_two;
        this.price_three = price_three;
        this.top_number = top_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice_one() {
        return price_one;
    }

    public void setPrice_one(String price_one) {
        this.price_one = price_one;
    }

    public String getPrice_two() {
        return price_two;
    }

    public void setPrice_two(String price_two) {
        this.price_two = price_two;
    }

    public String getPrice_three() {
        return price_three;
    }

    public void setPrice_three(String price_three) {
        this.price_three = price_three;
    }

    public String getTop_number() {
        return top_number;
    }

    public void setTop_number(String top_number) {
        this.top_number = top_number;
    }
}
