package com.logicdevil.todolist;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Created by daymond on 7/26/17.
 */
public class DataHandler {


    ArrayList<Task> getTaskList() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Something", "Some text", LocalDate.of(2017,12,21),
                LocalTime.of(10, 20), 5, "One time", false));
        return tasks;
    }

    void updateDateTime(Component component) {
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy    HH:mm");
            ((JLabel) component).setText(LocalDateTime.now().format(format));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    LocalDateTime getTime() {
        return LocalDateTime.now();
    }
    String getBirthdaysString() {
        return "<html>" +
                "Birthday of Lesha Poshivaylo in 2 days" + "<br>" +
                "<p style='margin-top:2'>Birthday of Vanya Romanenko in 5 days" + "<br>" +
                "<p style='margin-top:2'>Birthday of Lesha Poshivaylo in 2 days" + "<br>" +
                "<p style='margin-top:2'>Birthday of Lesha Poshivaylo in 2 days" + "<br>" +
                "<p style='margin-top:2'>Birthday of Lesha Poshivaylo in 2 days" + "<br>";
    }
}
