package com.example.myapplication.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import javax.annotation.processing.Generated;

@Entity(tableName = "events")
public class Event {
    @PrimaryKey
    @SerializedName("id")
    private long id;
    @SerializedName("client")
    private final Client client;
    @SerializedName("eventName")
    private String eventName;
    @SerializedName("eventDescription")
    private String eventDescription;
    @SerializedName("eventLocation")
    private String eventLocation;
    @SerializedName("category")
    private String category;
    @SerializedName("eventUrl")
    private String eventUrl;
    @SerializedName("eventUsername")
    private String eventUsername;
    @SerializedName("event_stars_value")
    private double event_stars_value;
    @SerializedName("event_time")
    private String event_time;

    public Event(Client client, String eventName, String eventDescription, String eventLocation, String category, String eventUrl, String eventUsername, double event_stars_value, String event_time) {
        this.client = client;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.category = category;
        this.eventUrl = eventUrl;
        this.eventUsername = eventUsername;
        this.event_stars_value = event_stars_value;
        this.event_time = event_time;
    }


    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", client=" + client +
                ", eventName='" + eventName + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", eventLocation='" + eventLocation + '\'' +
                ", category='" + category + '\'' +
                ", eventUrl='" + eventUrl + '\'' +
                ", eventUsername='" + eventUsername + '\'' +
                ", event_stars_value=" + event_stars_value +
                ", event_time='" + event_time + '\'' +
                '}';
    }

    public Client getClient() {
        return client;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public long getId() {
        return id;
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

    public void setId(long id) {
        this.id = id;
    }



    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public void setEventUsername(String eventUsername) {
        this.eventUsername = eventUsername;
    }

    public void setEvent_stars_value(double event_stars_value) {
        this.event_stars_value = event_stars_value;
    }
}

