package com.example.myapplication.res;
import com.example.myapplication.domain.Client;
import com.example.myapplication.domain.Event;

import java.util.ArrayList;

public interface AppApi {
    void getClientById(long id);
    void getAllUsers();
    void updateUser(long id, String login,String city, double starsvalue, String username, String password);
    void addUser(Client client);
    void deleteUserById(long id);
    void getUserByLogin(String login);
    void getEventById(long id);
    void getAllEvents();
    void updateEvent(long id,long userId, String eventName, String eventDescription, String eventLocation, String category, String eventUrl, String eventUsername, double event_stars_value);
    void addEvent(Event event);
    void getEventsByCategory(String category);
    void deleteEventById(long id);
}
