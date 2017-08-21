package com.logicdevil.todolist;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by daymond on 7/29/17.
 */
public class Task {
    private int id;
    private LocalDate date;
    private String time;
    private String title;
    private String description;
    private int priority;
    private String frequency;
    private boolean isComplete;
    Task(String title, String description, LocalDate date, String time, int priority, String frequency, int id) {
        isComplete = false;
        this.id = id;
        this.date = date;
        this.time = time;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.frequency = frequency;
    }
    public String getTime() {
        return time;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public int getPriority() {
        return priority;
    }
    public String getFrequency() {
        return frequency;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }
}
