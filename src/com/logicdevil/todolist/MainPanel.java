package com.logicdevil.todolist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        add(createTopPanel(dH), BorderLayout.NORTH);
        add(createCenterPanel(dH), BorderLayout.CENTER);
        add(createBottomPanel(dH), BorderLayout.SOUTH);
    }
    public void update(DataHandler dH) {
        this.removeAll();
        add(createTopPanel(dH), BorderLayout.NORTH);
        add(createCenterPanel(dH), BorderLayout.CENTER);
        add(createBottomPanel(dH), BorderLayout.SOUTH);
    }
    /*
    --------------------------------------------Create panel with tasks-----------------------
    */
    private JPanel createCenterPanel(DataHandler dH) {
        ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createMatteBorder(2,0,2,0, new Color(30,30,30)));
        centerPanel.setBackground(new Color(50, 50, 50));
        JPanel panelForScrolling = new JPanel(new GridBagLayout());
        panelForScrolling.setBackground(new Color(50, 50, 50));
        panelForScrolling.setBorder(BorderFactory.createEmptyBorder(5,18,5,0));
        GridBagConstraints c = new GridBagConstraints();
        ArrayList<Task> tasks = dH.getTaskList();
        int i = 1;
        c.gridy = 0;
        for (Task t: tasks) {
            String text = (i++ + ". " + t.getTitle());
            JLabel labelTask = new JLabel(text);
            labelTask.setFont(new Font("Serif", Font.PLAIN, 15));
            labelTask.setForeground(new Color(150,150,150));
            labelTask.setPreferredSize(new Dimension(300,1));
            labelTask.setBorder(BorderFactory.createEmptyBorder(0,0,0,30));
            String toolTipText = dH.getToolTipText(t);
            labelTask.setToolTipText(toolTipText);
            JLabel labelTime = new JLabel(t.getTime());
            labelTime.setFont(new Font("Serif", Font.PLAIN, 15));
            labelTime.setForeground(new Color(150,150,150));
            JCheckBox checkBox = new JCheckBox("", false);
            checkBox.setBorder(BorderFactory.createEmptyBorder(0,0,0,-10));
            checkBox.setToolTipText("Note if the task is complete");
            checkBox.setOpaque(false);
            c.insets = ((i-1) == tasks.size()) ? new Insets(0, 0, 5, 0) : new Insets(0, 0, 6, 0);
            c.weightx = 3;
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.anchor = GridBagConstraints.NORTHEAST;
            panelForScrolling.add(labelTask, c);
            c.weightx = 1.5;
            c.gridx = 1;
            panelForScrolling.add(labelTime, c);
            c.gridx = 2;
            c.weightx = 1;
            panelForScrolling.add(checkBox, c);
            checkBoxes.add(checkBox);
            c.gridy++;
        }
        if(tasks.size() == 0) {
            JLabel label = new JLabel("Today there are no tasks", JLabel.CENTER);
            label.setFont(new Font("Serif", Font.PLAIN, 15));
            label.setForeground(new Color(150,150,150));
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            panelForScrolling.add(label, c);
        }
        else dH.setCheckBoxes(checkBoxes);
        JScrollPane scrollPane = new JScrollPane(panelForScrolling,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(9);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
        scrollPane.setOpaque(false);
        centerPanel.add(scrollPane, BorderLayout.PAGE_START);
        return centerPanel;
    }
    /*
    -------------------------------------Create panel with date and time----------------------------
    */
    private JPanel createTopPanel(DataHandler dataHandler) {
        topPanel = new JPanel();
        topPanel.setBackground(new Color(50, 50, 50));
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy    HH:mm");
        JLabel label = new JLabel(dataHandler.getTime().format(format));
        label.setFont(new Font("Serif", Font.BOLD, 45));
        label.setForeground(new Color(150,150,150));
        topPanel.add(label);
        return topPanel;
    }
    /*
    --------------------------------------Create panel with buttons and birthday's list--------------
    */
    private JPanel createBottomPanel(DataHandler dH) {
        bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(new Color(50,50,50));
        MainButton newTask = new MainButton("Create new task");
        newTask.addActionListener(ListenerFactory.createNewTaskListener(dH));
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
        return bottomPanel;
    }
    void updateDateTime(DataHandler dH) {
        dH.updateDateTime(topPanel.getComponent(0));
    }
}
