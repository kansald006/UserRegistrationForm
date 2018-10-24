package com.example.deepak.userregistrationform;

import java.io.Serializable;

public class User implements Serializable {
    public int id;
    public String name;
    public String email;
    public String CDate;
    public String gender;
    public String city;

    User(){

    }

    public User(int id,String name, String email, String CDate, String gender, String city) {
        this.id= id;
        this.name = name;
        this.email = email;
        this.CDate = CDate;
        this.gender = gender;
        this.city = city;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", CDate='" + CDate + '\'' +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}

