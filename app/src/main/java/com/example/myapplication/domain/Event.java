package com.example.myapplication.domain;

public class Event {
    private long id;
    private long userId;
    private String eventName;
    private String eventDescription;
    private String eventLocation;
    private String category;
    private String eventUrl;
    private String eventUsername;

    private double event_stars_value;

    public Event(long userId, String eventName, String eventDescription, String eventLocation, String category, String eventUrl, String eventUsername) {
        this.userId = userId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.category = category;
        this.eventUrl = eventUrl;
        this.eventUsername = eventUsername;
        this.event_stars_value = 5.0;
    }
    public Event(long id,long userId, String eventName, String eventDescription, String eventLocation, String category, String eventUrl, String eventUsername, double event_stars_value) {
        this.id = id;
        this.userId = userId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.category = category;
        this.eventUrl = eventUrl;
        this.eventUsername = eventUsername;
        this.event_stars_value = event_stars_value;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public String getCategory() {
        return category;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public String getEventUsername() {
        return eventUsername;
    }

    public double getEvent_stars_value() {
        return event_stars_value;
    }
}

