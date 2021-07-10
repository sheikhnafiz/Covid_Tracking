package com.example.covidtracking.model;

public class Blog {
    String image;
    String title;
    String details;
    public Blog() {
    }

    public Blog(String image, String title, String details) {
        this.image = image;
        this.title = title;
        this.details = details;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
