package com.example.myapplication.res;

import com.example.myapplication.domain.Event;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EventApiService {
    @GET("/event/{id}")
    Call<Event> getEventById(@Path("id") long id);

    @GET("/event")
    Call<ArrayList<Event>> getAllEvents();

    @PUT("/event")
    Call<Event> updateEvent(@Body Event event);

    @POST("/event")
    Call<Event> addEvent(@Body Event event);

    @DELETE("/event/{id}")
    Call<Void> deleteEventById(@Path("id") long id);

    @GET("/event/{category}")
    Call<ArrayList<Event>> getEventsByCategory(@Path("category") String category);
}