package com.logicdevil.todolist;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Created by daymond on 7/25/17.
 */
public class MainPanel extends JPanel {
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel centerPanel;
    MainPanel(DataHandler dH) {
        setLayout(new BorderLayout());
        createTopPanel(dH);
        createBottomPanel(dH);
        createCenterPanel(dH);
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    private void createCenterPanel(DataHandler dH) {
        centerPanel = new JPanel();
        centerPanel.setBackground(new Color(50, 50, 50));
        centerPanel.setBorder(BorderFactory.createMatteBorder(2,0,2,0, new Color(30,30,30)));
        JPanel panelForScrolling = new JPanel();
        panelForScrolling.setLayout(new BoxLayout(panelForScrolling, BoxLayout.Y_AXIS));
        ArrayList<Task> tasks = dH.getTaskList();
        int i = 1;
        for (Task t: tasks) {
            JLabel label = new JLabel("" + i++);
            label.setFont(new Font("Serif", Font.PLAIN, 14));
            label.setForeground(new Color(150,150,150));
            panelForScrolling.add(label);
        }







        JScrollPane scrollPane = new JScrollPane(panelForScrolling,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        centerPanel.add(scrollPane);
    }
    private void createTopPanel(DataHandler dataHandler) {
        topPanel = new JPanel();
        topPanel.setBackground(new Color(50, 50, 50));
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy    HH:mm");
        JLabel label = new JLabel(dataHandler.getTime().format(format));
        label.setFont(new Font("Serif", Font.BOLD, 45));
        label.setForeground(new Color(150,150,150));
        topPanel.add(label);
    }
    private void createBottomPanel(DataHandler dH) {
        bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(new Color(50,50,50));
        MainButton newTask = new MainButton("Create new task");
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.insets = new Insets(10, 20, 10, 0);
        bottomPanel.add(newTask,c);
        MainButton moveTask = new MainButton("Postpone the task");
        c.gridy = 1;
        c.insets = new Insets(0, 20, 0, 0);
        bottomPanel.add(moveTask,c);
        MainButton button = new MainButton ("For future");
        c.gridy = 2;
        c.insets = new Insets(10, 20, 10, 0);
        bottomPanel.add(button,c);
        JLabel label = new JLabel(dH.getBirthdaysString());
        label.setFont(new Font("Serif", Font.BOLD, 13));
        label.setForeground(Color.BLACK);
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.LEFT);
        c.gridx =  1;
        c.gridy = 0;
        c.gridheight = 3;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new Insets(10, 20, 10, 0);
        bottomPanel.add(label, c);
    }
    void updateDateTime(DataHandler dH) {
        dH.updateDateTime(topPanel.getComponent(0));
    }
}
