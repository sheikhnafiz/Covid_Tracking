package com.example.covidtracking.model;

public class Prescription
{
    private String image;
    private String doctorName;
    private String details;
    private String date;

    public Prescription() {
    }

    public Prescription(String image, String doctorName, String details, String date) {
        this.image = image;
        this.doctorName = doctorName;
        this.details = details;
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
