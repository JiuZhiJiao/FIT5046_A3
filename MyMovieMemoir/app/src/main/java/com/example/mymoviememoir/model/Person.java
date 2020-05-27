package com.example.mymoviememoir.model;

public class Person {
    private int personid;
    private String firstname;
    private String surname;
    private String gender;
    private String dob;
    private String address;
    private String state;
    private String postcode;
    private Credential credentialsid;

    public Person() {

    }

    public Person(int personid, String firstname, String surname, String gender, String dob, String address, String state, String postcode, Credential credentialsid) {
        this.personid = personid;
        this.firstname = firstname;
        this.surname = surname;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.state = state;
        this.postcode = postcode;
        this.credentialsid = credentialsid;
    }

    public int getPersonid() {
        return personid;
    }

    public void setPersonid(int personid) {
        this.personid = personid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Credential getCredentialsid() {
        return credentialsid;
    }

    public void setCredentialsid(Credential credentialsid) {
        this.credentialsid = credentialsid;
    }
}
