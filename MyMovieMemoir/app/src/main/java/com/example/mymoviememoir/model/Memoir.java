package com.example.mymoviememoir.model;

public class Memoir {

    private int memoirid;
    private String moviename;
    private String releasedate;
    private String watchedtime;
    private String comment;
    private double ratingscore;
    private Person personid;
    private Cinema cinemaid;

    public Memoir() {

    }

    public Memoir(int memoirid, String moviename, String releasedate, String watchedtime, String comment, double ratingscore, Person personid, Cinema cinemaid) {
        this.memoirid = memoirid;
        this.moviename = moviename;
        this.releasedate = releasedate;
        this.watchedtime = watchedtime;
        this.comment = comment;
        this.ratingscore = ratingscore;
        this.personid = personid;
        this.cinemaid = cinemaid;
    }

    public int getMemoirid() {
        return memoirid;
    }

    public void setMemoirid(int memoirid) {
        this.memoirid = memoirid;
    }

    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getWatchedtime() {
        return watchedtime;
    }

    public void setWatchedtime(String watchedtime) {
        this.watchedtime = watchedtime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRatingscore() {
        return ratingscore;
    }

    public void setRatingscore(double ratingscore) {
        this.ratingscore = ratingscore;
    }

    public Person getPersonid() {
        return personid;
    }

    public void setPersonid(Person personid) {
        this.personid = personid;
    }

    public Cinema getCinemaid() {
        return cinemaid;
    }

    public void setCinemaid(Cinema cinemaid) {
        this.cinemaid = cinemaid;
    }
}
