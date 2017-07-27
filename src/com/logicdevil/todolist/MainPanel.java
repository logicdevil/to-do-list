package com.logicdevil.todolist;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        //centerPanel = dH.getTaskListPanel();
        //bottomPanel = getBottomPanel();
        add(topPanel, BorderLayout.NORTH);
        //add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    private JPanel getBottomPanel() {
        return null;
    }
    private void createTopPanel(DataHandler dataHandler) {
        topPanel = new JPanel();
        topPanel.setBackground(new Color(50, 50, 50));
        topPanel.setBorder(BorderFactory.createMatteBorder(0,0,2,0, new Color(30,30,30)));
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy    HH:mm");
        JLabel label = new JLabel(dataHandler.getTime().format(format));
        label.setFont(new Font("Serif", Font.BOLD, 45));
        label.setForeground(Color.BLACK);
        topPanel.add(label);
    }
    private void createBottomPanel(DataHandler dH) {
        bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(50,50,50));
        bottomPanel.setLayout(new GridBagLayout());
        JButton newTask = new JButton("Create new task");
        newTask.setBackground(new Color(150,150,150));//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        newTask.setForeground(Color.BLACK);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.insets = new Insets(10, 20, 10, 0);
        bottomPanel.add(newTask,c);
        JButton moveTask = new JButton("Postpone the task");
        c.gridy = 1;
        c.insets = new Insets(0, 20, 0, 0);
        bottomPanel.add(moveTask,c);
        JButton button = new JButton ("For future");
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
