package com.example.capston;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PublicKey;

public class UserDetails {

    String name;
    String Mobile_No;
    String Adhar_no;
    String email;


    public UserDetails()
    {

    }

    public UserDetails(String email, String name, String Mobile_No, String Adhar_no)

    {
        this.email = email;
        this.name = name;
        this.Mobile_No = Mobile_No;
        this.Adhar_no = Adhar_no;
    }

    public String getName() {
        return name;
    }

    public String getMobile_No() {
        return Mobile_No;
    }

    public String getAdhar_no() {
        return Adhar_no;
    }

    public String getId() {
        return email;
    }

}
