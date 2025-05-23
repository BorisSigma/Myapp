package com.example.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.domain.Client;
import com.example.myapplication.domain.Event;
import com.example.myapplication.fragments.LoginFragment;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app_database.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "login TEXT NOT NULL UNIQUE," +
                        "city TEXT," +
                        "starsvalue REAL DEFAULT 5.0," +
                        "username TEXT NOT NULL," +
                        "password TEXT NOT NULL);"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS events (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "user_id INTEGER NOT NULL," +
                        "event_name TEXT NOT NULL," +
                        "event_description TEXT," +
                        "event_location TEXT NOT NULL," +
                        "event_stars_value TEXT DEFAULT 5," +
                        "timestamp DATETIME DEFAULT (datetime('now', '+3 hours'))," +
                        "category TEXT NOT NULL," +
                        "eventUrl TEXT," +
                        "eventUsername TEXT NOT NULL," +
                        "FOREIGN KEY(user_id) REFERENCES users(id));"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS events");
        onCreate(db);
    }
    public boolean updateEventStars(long eventId, String newStarsValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("event_stars_value", newStarsValue);
        int rows = db.update("events",
                values,
                "id = ?",
                new String[]{String.valueOf(eventId)});
        db.close();
        return rows > 0;
    }
    public boolean updateUserStars(long userId, String newStarsValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("starsvalue", newStarsValue);
        int rows = db.update("users",
                values,
                "id = ?",
                new String[]{String.valueOf(userId)});
        db.close();
        return rows > 0;
    }

    public long addUser(String login, String password, String city, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("login", login);
        values.put("password", password);
        values.put("city", city);
        values.put("username", username);
        long result = -1;
        try {
            result = db.insertOrThrow("users", null, values);
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error adding user: " + e.getMessage());
        } finally {
            db.close();
        }
        return result;
    }
    public int deleteEvent(long eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = db.delete("events",
                "id = ?",
                new String[]{String.valueOf(eventId)});
        db.close();
        return count;
    }
    public int deleteAllEvents() {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = db.delete("events", null, null);
        db.close();
        return count;
    }

    public long getUserIdByLogin(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        long userId = -1;
        try (Cursor cursor = db.query("users",
                new String[]{"id"},
                "login = ?",
                new String[]{login},
                null, null, null)) {

            if (cursor.moveToFirst()) {
                userId = cursor.getLong(0);
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error getting user ID: " + e.getMessage());
        }
        return userId;
    }
    public long getUserIdByEventId(long eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        long userId = -1;

        try (Cursor cursor = db.query(
                "events",
                new String[]{"user_id"},
                "id = ?",
                new String[]{String.valueOf(eventId)},
                null, null, null)) {

            if (cursor.moveToFirst()) {
                userId = cursor.getLong(0);
            }
        }
        return userId;
    }
    public Integer getEventIdByTimestamp(String timestamp) {
        SQLiteDatabase db = this.getReadableDatabase();
        Integer eventId = null;

        try (Cursor cursor = db.query(
                "events",
                new String[]{"id"},
                "timestamp = ?",
                new String[]{timestamp},
                null, null, null)) {

            if (cursor.moveToFirst()) {
                eventId = cursor.getInt(0);
            }
        }
        return eventId;
    }
    public long getEventIdByLocation(String location) {
        SQLiteDatabase db = this.getReadableDatabase();
        long eventId = -1;

        try (Cursor cursor = db.query("events", new String[]{"id"}, "event_location = ?", new String[]{location}, null, null, null)) {

            if (cursor.moveToFirst()) {
                eventId = cursor.getLong(0);
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error getting event ID: " + e.getMessage());
        }

        return eventId;
    }

    public String getPasswordByLogin(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        String password = null;
        try (Cursor cursor = db.query("users",
                new String[]{"password"},
                "login = ?",
                new String[]{login},
                null, null, null)) {

            if (cursor.moveToFirst()) {
                password = cursor.getString(0);
            }
        }
        return password;
    }

    public ArrayList<Client> findAllClients() {
        ArrayList<Client> clients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.query("users", null, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                clients.add(new Client(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getString(4), cursor.getString(5)
                ));
            }
        }
        return clients;
    }

    public boolean updateUserCity(long userId, String newCity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("city", newCity);
        int rows = db.update("users", values, "id = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rows > 0;
    }

    public boolean updateUserLogin(long userId, String newLogin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("login", newLogin);
        int rows = db.update("users", values, "id = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rows > 0;
    }

    public boolean updateUsername(long userId, String newUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", newUsername);
        int rows = db.update("users", values, "id = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rows > 0;
    }

    public boolean isLoginUnique(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.query("users",
                new String[]{"id"},
                "login = ?",
                new String[]{login},
                null, null, null)) {

            return cursor.getCount() == 0;
        }
    }

    public long addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", event.getUserId());
        values.put("event_name", event.getEventName());
        values.put("event_description", event.getEventDescription());
        values.put("event_location", event.getEventLocation());
        values.put("category", event.getCategory());
        values.put("event_stars_value", event.getEvent_stars_value());
        values.put("eventUrl", event.getEventUrl());
        values.put("eventUsername", event.getEventUsername());

        long id = db.insert("events", null, values);
        db.close();
        return id;
    }
    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> events = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.query("events", null, null, null, null, null, "timestamp DESC")) {
            while (cursor.moveToNext()) {
                events.add(new Event(cursor.getLong(0),cursor.getLong(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getDouble(8)));
            }
        }
        return events;
    }
    public double getEventStarsValue(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(
                "SELECT event_stars_value FROM events WHERE id = ?",
                new String[]{String.valueOf(id)})) {

            return cursor.moveToFirst() ? cursor.getDouble(0) : 5.0;
        }

    }

    public String getEventField(long eventId, String column) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.query("events",
                new String[]{column},
                "id = ?",
                new String[]{String.valueOf(eventId)},
                null, null, null)) {

            return cursor.moveToFirst() ? cursor.getString(0) : null;
        }
    }

    public ArrayList<Event> getEventsByCategory(String category) {
        ArrayList<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.query("events",
                null,
                "category = ?",
                new String[]{category},
                null, null, "timestamp DESC")) {

            while (cursor.moveToNext()) {
                events.add(new Event(cursor.getLong(0), cursor.getLong(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getDouble(8)));
            }
        }
        return events;
    }

    private int getEventIntField(long eventId, String column) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.query("events",
                new String[]{column},
                "id = ?",
                new String[]{String.valueOf(eventId)},
                null, null, null)) {

            return cursor.moveToFirst() ? cursor.getInt(0) : 0;
        }
    }


    public String getUserLoginById(long userId) {
        return getSingleValue("users", "login", "id = " + userId);
    }

    public String getUserCityById(long userId) {
        return getSingleValue("users", "city", "id = " + userId);
    }

    public double getUserStarsById(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(
                "SELECT starsvalue FROM users WHERE id = ?",
                new String[]{String.valueOf(userId)})) {

            return cursor.moveToFirst() ? cursor.getDouble(0) : 5.0;
        }
    }

    public String getUserPasswordById(long userId) {
        return getSingleValue("users", "password", "id = " + userId);
    }

    public String getUsernameById(long userId) {
        return getSingleValue("users", "username", "id = " + userId);
    }

    private String getSingleValue(String table, String column, String whereClause) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.query(table,
                new String[]{column},
                whereClause,
                null, null, null, null)) {

            return cursor.moveToFirst() ? cursor.getString(0) : null;
        }
    }


    public String getEventNameById(long eventId) {
        return getEventField(eventId, "event_name");
    }

    public String getEventDescriptionById(long eventId) {
        return getEventField(eventId, "event_description");
    }

    public String getEventLocationById(long eventId) {
        return getEventField(eventId, "event_location");
    }

    public String getEventTimestampById(long eventId) {
        return getEventField(eventId, "timestamp");
    }

    public String getEventCategoryById(long eventId) {
        return getEventField(eventId, "category");
    }

    public String getEventUrlById(long eventId) {
        return getEventField(eventId, "eventUrl");
    }

    public String getEventUsernameById(long eventId) {
        return getEventField(eventId, "eventUsername");
    }
}