package com.example.myapplication.res;

import android.content.Context;

import com.example.myapplication.domain.Client;
import com.example.myapplication.domain.Event;

public class AppApiVolley implements AppApi{
    private Context context;
    private static final String BASE_URL = "http://192.168.1.7";
    @Override
    public void getClientById(long id) {

    }

    @Override
    public void getAllUsers() {

    }

    @Override
    public void updateUser(long id, String login, String city, double starsvalue, String username, String password) {

    }

    @Override
    public void addUser(Client client) {

    }

    @Override
    public void deleteUserById(long id) {

    }

    @Override
    public void getUserByLogin(String login) {

    }

    @Override
    public void getEventById(long id) {

    }

    @Override
    public void getAllEvents() {

    }

    @Override
    public void updateEvent(long id, long userId, String eventName, String eventDescription, String eventLocation, String category, String eventUrl, String eventUsername, double event_stars_value) {

    }

    @Override
    public void addEvent(Event event) {

    }

    @Override
    public void getEventsByCategory(String category) {

    }

    @Override
    public void deleteEventById(long id) {

    }
}
