package com.example.myapplication.res;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://192.168.1.7:8081/";
    private static Retrofit retrofit = null;
    private static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static class Events {
        public static EventApiService getService() {
            return getClient().create(EventApiService.class);
        }
    }

    public static class Users {
        public static ClientApiService getService() {
            return getClient().create(ClientApiService.class);
        }
    }
}