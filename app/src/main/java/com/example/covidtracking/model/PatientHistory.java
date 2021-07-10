package com.example.covidtracking.model;

public class PatientHistory
{
    private String id;
    private Double latitute;
    private Double longatitute;

    public PatientHistory() {
    }

    public PatientHistory(String id, Double latitute, Double longatitute) {
        this.id = id;
        this.latitute = latitute;
        this.longatitute = longatitute;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLatitute() {
        return latitute;
    }

    public void setLatitute(Double latitute) {
        this.latitute = latitute;
    }

    public Double getLongatitute() {
        return longatitute;
    }

    public void setLongatitute(Double longatitute) {
        this.longatitute = longatitute;
    }
}
