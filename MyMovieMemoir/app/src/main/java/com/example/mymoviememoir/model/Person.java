package com.example.mymoviememoir.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {
    private int personid;
    private String firstname;
    private String surname;
    private String gender;
    private String dob;
    private String address;
    private String state;
    private String postcode;
    private Credential credentialsid;

    // Parcelable
    public Person(Parcel in) {
        this.personid = in.readInt();
        this.firstname = in.readString();
        this.surname = in.readString();
        this.gender = in.readString();
        this.dob = in.readString();
        this.address = in.readString();
        this.state = in.readString();
        this.postcode = in.readString();
        this.credentialsid = in.readParcelable(Credential.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(personid);
        dest.writeString(firstname);
        dest.writeString(surname);
        dest.writeString(gender);
        dest.writeString(dob);
        dest.writeString(address);
        dest.writeString(state);
        dest.writeString(postcode);
        dest.writeParcelable(credentialsid, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    // Initial
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
