package com.logicdevil.todolist;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by daymond on 7/26/17.
 */
public class ToDoList {
    DataHandler dH;
    MainPanel mainPanel;
    public static void main(String[] args) {
        ToDoList controller = new ToDoList();
        controller.init();
        controller.run();


    }

    void init() {
        ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
        // time to wait before showing tooltip when mouse enters component
        toolTipManager.setInitialDelay(2000);
        // time to wait before dismissing tooltip (but overridden if
        // mouse moves outside of components bounds)
        toolTipManager.setDismissDelay(20000);
        // time to wait before stop immediately showing tooltip when mouse re-enters component (hard to explain)
        toolTipManager.setReshowDelay(100);
        JFrame mainFrame = new JFrame("TODO List by logicdevil");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(500, 700);
        mainFrame.setResizable(false);
        dH = new DataHandler(mainFrame);
        mainPanel = new MainPanel(dH);
        dH.setMainPanel(mainPanel);
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
