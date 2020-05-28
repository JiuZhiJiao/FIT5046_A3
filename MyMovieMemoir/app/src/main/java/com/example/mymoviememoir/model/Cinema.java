package com.example.mymoviememoir.model;

public class Cinema {

    private int cinemaid;
    private String cinemaname;
    private String cinemapostcode;

    public Cinema() {

    }

    public Cinema(int cinemaid, String cinemaname, String cinemapostcode) {
        this.cinemaid = cinemaid;
        this.cinemaname = cinemaname;
        this.cinemapostcode = cinemapostcode;
    }

    public int getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(int cinemaid) {
        this.cinemaid = cinemaid;
    }

    public String getCinemaname() {
        return cinemaname;
    }

    public void setCinemaname(String cinemaname) {
        this.cinemaname = cinemaname;
    }

    public String getCinemapostcode() {
        return cinemapostcode;
    }

    public void setCinemapostcode(String cinemapostcode) {
        this.cinemapostcode = cinemapostcode;
    }
}
