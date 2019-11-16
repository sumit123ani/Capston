package com.example.capston;


import android.net.Uri;

public class UserDetails {

    private String name;
    private String Mobile_No;
    private String Adhar_No;
    private String email;
    private String address;
    private String doB;
    private String url;

    public UserDetails()
    {

    }

    public UserDetails(String url)
    {
        this.url = url;
    }

    public UserDetails(String email, String name, String Mobile_No, String Adhar_No, String address, String doB)

    {
        this.name = name;
        this.Mobile_No = Mobile_No;
        this.Adhar_No = Adhar_No;
        this.address = address;
        this.doB = doB;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getMobile_No() {
        return Mobile_No;
    }

    public String getAdhar_no() {
        return Adhar_No;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getDoB() {
        return doB;
    }

    public String getUrl() {
        return url;
    }
}
