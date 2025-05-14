package com.example.myapplication.exepion;

public class NotFoundUserExepcion extends RuntimeException {
    public NotFoundUserExepcion(String message) {
        super(message);
    }
}
