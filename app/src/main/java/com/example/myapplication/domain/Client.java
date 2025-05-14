package com.example.myapplication.domain;

public  class Client{
    private long id;
    private String login;
    private String city;
    private double starsvalue;
    private String username;
    private String password;

    public Client(String login, String password, String city, String username) {
        this.login = login;
        this.password = password;
        this.city = city;
        this.username = username;
        this.starsvalue = 5.0;
    }
    public Client(long id, String login, String city, double starsvalue,
                  String username, String password) {
        this.id = id;
        this.login = login;
        this.city = city;
        this.starsvalue = starsvalue;
        this.username = username;
        this.password = password;
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

    public double getStarsvalue() {
        return starsvalue;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", city='" + city + '\'' +
                ", stars_value=" + starsvalue +
                ", user_name='" + username + '\'' +
                ", pass='" + password + '\'' +
                '}';
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
        this.starsvalue = starsvalue;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
