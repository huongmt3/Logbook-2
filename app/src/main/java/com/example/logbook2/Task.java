package com.example.logbook2;

import java.io.Serializable;

public class Task implements Serializable {
    private String name;
    private String date;
    private String time;

    public Task(String name, String date, String time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
