package com.example.myapplication.exepion;

public class NotFoundEventExecion extends RuntimeException {
    public NotFoundEventExecion(String message) {
        super(message);
    }
}
