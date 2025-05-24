package com.example.myapplication.res;

import com.example.myapplication.domain.Client;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ClientApiService {
    @GET("/user/{id}")
    Call<Client> getClientById(@Path("id") long id);

    @GET("/user")
    Call<ArrayList<Client>> getAllClients();

    @PUT("/user")
    Call<Client> updateClient(@Body Client client);

    @POST("/user")
    Call<Client> addClient(@Body Client client);

    @DELETE("/user/{id}")
    Call<Void> deleteClientById(@Path("id") long id);

    @GET("/user/{login}")
    Call<Client> getClientByLogin(@Query("login") String login);
}