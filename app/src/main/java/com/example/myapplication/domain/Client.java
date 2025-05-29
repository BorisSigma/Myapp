package com.example.myapplication.domain;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "users")
public  class Client{
    @PrimaryKey
    @SerializedName("id")
    private long id;
    @SerializedName("login")
    private String login;
    @SerializedName("city")
    private String city;
    @SerializedName("starsValue")
    private double starsValue;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;


    public Client(String login, String password, String city, String username, double starsvalue) {
        this.login = login;
        this.password = password;
        this.city = city;
        this.username = username;
        this.starsValue = starsvalue;
    }
    public Client(long id, String login, String city, double starsvalue,
                  String username, String password) {
        this.id = id;
        this.login = login;
        this.city = city;
        this.username = username;
        this.password = password;
        this.starsValue = starsvalue;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", city='" + city + '\'' +
                ", starsValue=" + starsValue +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getCity() {
        return city;
    }

    public double getStarsValue() {
        return starsValue;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStarsValue(double starsvalue) {
        this.starsValue = starsvalue;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
