package com.example.mymoviememoir.model;

public class Movie {
    private int id;
    private String name;
    private String date;
    private String imagePath;
    private String summary;
    private Double score;

    public Movie() {

    }

    public Movie(int id, String name, String date, String imagePath, String summary, Double score) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.imagePath = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/"+imagePath;
        this.summary = summary;
        this.score = score;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
