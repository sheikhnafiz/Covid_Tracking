package com.example.covidtracking.model;

public class Model
{
    int image;
    String title;
    String details;

    public Model(int image, String title, String details) {
        this.image = image;
        this.title = title;
        this.details = details;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
