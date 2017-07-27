package com.logicdevil.todolist;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by daymond on 7/26/17.
 */
public class ToDoList {
    MainPanel mainPanel;
    DataHandler dH;
    public static void main(String[] args) {
        ToDoList controller = new ToDoList();
        controller.run();


    }

    ToDoList() {
        JFrame mainFrame = new JFrame("TODO List by  logicDevil");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(500, 700);
        mainFrame.setResizable(false);
        dH = new DataHandler();
        mainPanel = new MainPanel(dH);
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }
    void run() {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::loop, 60 - dH.getTime().getSecond(), 60, TimeUnit.SECONDS);
        //Some code, that execute method 'loop' every 60 sec with init delay - number of sec to new minute.
    }
    void loop() {
        mainPanel.updateDateTime(dH);
    }
}
