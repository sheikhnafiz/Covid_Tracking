package com.example.covidtracking.model;

import java.util.List;

public class UserProfileInfo {

    private String id;
    private String name;
    private String image;
    private String number;
    private String gender;
    private String category;
    private List<Prescription> prescription;

    public UserProfileInfo()
    {

    }


    public UserProfileInfo(String id, String name, String image, String number, String gender, String category) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.number = number;
        this.gender = gender;
        this.category = category;
    }

    public UserProfileInfo(String id, String name, String image, String number, String gender, String category, List<Prescription> prescription) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.number = number;
        this.gender = gender;
        this.category = category;
        this.prescription = prescription;
    }

    public List<Prescription> getPrescription() {
        return prescription;
    }

    public void setPrescription(List<Prescription> prescription) {
        this.prescription = prescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
