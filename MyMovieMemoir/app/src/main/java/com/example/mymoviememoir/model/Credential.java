package com.example.mymoviememoir.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Credential implements Parcelable {

    private int credentialsid;
    private String passwordhash;
    private String signupdate;
    private String username;

    // Parcelable
    public Credential(Parcel in) {
        this.credentialsid = in.readInt();
        this.passwordhash = in.readString();
        this.signupdate = in.readString();
        this.username = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(credentialsid);
        dest.writeString(passwordhash);
        dest.writeString(signupdate);
        dest.writeString(username);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Credential> CREATOR = new Creator<Credential>() {
        @Override
        public Credential createFromParcel(Parcel source) {
            return new Credential(source);
        }

        @Override
        public Credential[] newArray(int size) {
            return new Credential[size];
        }
    };

    // Initial
    public Credential() {

    }

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
