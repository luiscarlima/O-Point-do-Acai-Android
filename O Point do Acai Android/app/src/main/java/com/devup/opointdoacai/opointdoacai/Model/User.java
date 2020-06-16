package com.devup.opointdoacai.opointdoacai.Model;

public class User {

    private String Name;
    private String Password;
    private String Phone;

    public User() {
    }

    public User(String name, String password, String phone) {
        Name = name;
        Password = password;
        Phone = phone;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
