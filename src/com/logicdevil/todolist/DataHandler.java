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
        for(int i = 0; i < 30; i++)
        tasks.add(new Task(String.format("%70s", "s").replace(' ', 'w'), "Смазать куллер ноутбука, " +
                "Смазать куллер ноутбука, Смазать куллер ноутбука, Смазать куллер ноутбука, Смазать куллер ноутбука, Смазать куллер ноутбука, " +
                "Смазать куллер ноутбука, Смазать куллер ноутбука, Смазать куллер ноутбука, Смазать куллер ноутбука, " +
                "Смазать куллер ноутбука, Смазать куллер ноутбука, Смазать куллер ноутбука, Смазать куллер ноутбука, " +
                "Смазать куллер ноутбука... .", LocalDate.of(2017,12,21),
                LocalTime.of(10, 20), 5, "One time", false));
        return tasks;
    }
    String getToolTipText(Task t) {
        StringBuilder s = new StringBuilder("<html>" + t.getTitle() + "<br>");
        String description = t.getDescription();
        if(description.length() > 80) {
            String temp;
            int start = 0, end = 79;
            while (true) {
                if (end > description.length()) {
                    end = description.length();
                    temp = description.substring(start, end);
                    s.append(temp);
                    s.append("<br>");
                    break;
                }
                int tempInt = description.substring(0, end).lastIndexOf(' ');
                end = (tempInt == -1) ? end : tempInt;
                temp = description.substring(start, end);
                s.append(temp);
                s.append("<br>");
                start = end;
                if(start >= description.length())
                    break;
                end += 79;
            }
        }
        s.append("Frequency: ").append(t.getFrequency());
        s.append(";  Priority: ").append(t.getPriority()).append("</html>");
        return s.toString();
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
