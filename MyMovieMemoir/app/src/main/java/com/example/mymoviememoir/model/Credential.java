package com.example.mymoviememoir.model;

public class Credential {

    private int credentialsid;
    private String passwordhash;
    private String signupdate;
    private String username;

    public Credential(int credentialsid, String passwordhash, String signupdate, String username) {
        this.credentialsid = credentialsid;
        this.passwordhash = passwordhash;
        this.signupdate = signupdate;
        this.username = username;
    }

    public int getCredentialsid() {
        return credentialsid;
    }

    public void setCredentialsid(int credentialsid) {
        this.credentialsid = credentialsid;
    }

    public String getPasswordhash() {
        return passwordhash;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    public String getSignupdate() {
        return signupdate;
    }

    public void setSignupdate(String signupdate) {
        this.signupdate = signupdate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
