package com.example.beertag.models;

public class Style {

    private int id;
    private String name;

    public Style(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Style() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
