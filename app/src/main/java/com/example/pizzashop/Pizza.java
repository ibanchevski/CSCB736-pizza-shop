package com.example.pizzashop;

import androidx.annotation.NonNull;

public class Pizza {
    private String name;
    private String description;
    private float price;

    public Pizza(String name, String description, float price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s\n%s", this.name, this.description);
    }
}
