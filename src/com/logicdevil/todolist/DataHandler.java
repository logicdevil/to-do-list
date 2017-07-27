package com.logicdevil.todolist;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by daymond on 7/26/17.
 */
public class DataHandler {


    JPanel getTaskListPanel() {
        return null;
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
