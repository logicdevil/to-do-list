package com.logicdevil.todolist;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

/**
 * Created by daymond on 8/5/17.
 */
public class NewTaskPanel extends JPanel {
    JPanel panel;
    Color  backgroundColor, redFontColor;
    DataHandler dH;
    public NewTaskPanel(DataHandler dH) {
        this.dH = dH;
        backgroundColor = new Color(50, 50, 50);
        setLayout(new BorderLayout());
        add(createPanel(), BorderLayout.CENTER);
    }
    private JPanel createPanel() {
        panel = new JPanel(new GridBagLayout());
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20,30,10,30));
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        NewTaskLabelTemplate selectDateLabel = new NewTaskLabelTemplate("Select a date:", JLabel.LEFT);
        panel.add(selectDateLabel, c);

        NewTaskLabelTemplate timeLabel = new NewTaskLabelTemplate("Select a time:", JLabel.LEFT);
        c.insets = new Insets(0,60,0, 0);
        c.gridx = 3;
        panel.add(timeLabel, c);

        HashMap<String, JComponent> map = new HashMap<>();

        JCheckBox timeCheckBox = new JCheckBox();
        map.put("timeCheckBox", timeCheckBox);
        timeCheckBox.setOpaque(false);          //don't like this terrible white area around the checkbox
        c.insets = new Insets(0,10,0, 0);
        c.gridwidth = 1;
        c.gridx = 6;
        panel.add(timeCheckBox, c);

        NewTaskLabelTemplate dayLabel = new NewTaskLabelTemplate("Day", JLabel.LEFT);
        c.gridy++;
        c.gridx = 0;
        c.insets = new Insets(0,0,0, 10);
        panel.add(dayLabel, c);

        NewTaskLabelTemplate monthLabel = new NewTaskLabelTemplate("Month", JLabel.LEFT);
        c.gridx++;
        panel.add(monthLabel, c);

        NewTaskLabelTemplate yearLabel = new NewTaskLabelTemplate("Year", JLabel.LEFT);
        c.gridx++;
        c.insets = new Insets(0,0,0, 0);
        panel.add(yearLabel, c);

        String hours[] = new String[24];
        for(int i = 0; i < 24; i++) hours[i] = (i < 10) ? "0" + i : "" + i; //hours from 00 to 23
        JComboBox<String> hourComboBox = new JComboBox<>(hours);
        map.put("hourComboBox", hourComboBox);
        c.gridx++;
        c.insets = new Insets(0,60,0,55);
        panel.add(hourComboBox, c);

        NewTaskLabelTemplate colonLabel = new NewTaskLabelTemplate(":", JLabel.CENTER);
        colonLabel.setFont(new Font(Font.SERIF, Font.BOLD, 16));
        c.weightx = 0.3;
        c.insets = new Insets(0,-55,0, 40); //It's probably awful, but I don't know how to do it better
        c.gridx++;
        panel.add(colonLabel, c);

        String minutes[] = new String[60];  //minutes from 00 to 59
        for(int i = 0; i < 60; i++) minutes[i] = (i < 10) ? "0" + i : "" + i;
        JComboBox<String> minuteComboBox = new JComboBox<>(minutes);
        map.put("minuteComboBox", minuteComboBox);
        c.weightx = 1;
        c.gridx++;
        c.insets = new Insets(0,-40,0,45);
        panel.add(minuteComboBox, c);

        Integer days[] = new Integer[31];   //days from 1 to 31
        for(int i = 0; i < 31; i++)  days[i] = i + 1;
        JComboBox<Integer> dayComboBox = new JComboBox<>(days);
        map.put("dayComboBox", dayComboBox);
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0,0,0, 10);
        c.gridy++;
        c.gridx = 0;
        panel.add(dayComboBox, c);

        Integer months[] = new Integer[12];     //months from 1 to 12
        for(int i = 0; i < 12; i++)  months[i] = i + 1;
        JComboBox<Integer> monthComboBox = new JComboBox<>(months);
        map.put("monthComboBox", monthComboBox);
        c.gridx++;
        panel.add(monthComboBox, c);

        Integer years[] = {2017, 2018, 2019, 2020};
        JComboBox<Integer> yearComboBox = new JComboBox<>(years);
        map.put("yearComboBox", yearComboBox);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,0, 0);
        c.gridx++;
        panel.add(yearComboBox, c);

        /*These labels will be displayed if the user has selected an invalid time or
                                                    date (the time in the past or the date "February 30", for example)*/
        JLabel invalidTimeLabel = new JLabel("<html><sup><small>*</small></sup> Invalid time", JLabel.LEFT);
        map.put("invalidTimeLabel", invalidTimeLabel);
        invalidTimeLabel.setFont(new Font("Serif", Font.PLAIN, 15));
        invalidTimeLabel.setForeground(backgroundColor);
        c.insets = new Insets(0,60,0, 0);
        c.gridwidth = 4;
        c.gridx++;
        panel.add(invalidTimeLabel, c);

        JLabel invalidDateLabel = new JLabel("<html><sup><small>*</small></sup> Invalid date", JLabel.LEFT);
        map.put("invalidDateLabel", invalidDateLabel);
        invalidDateLabel.setFont(new Font("Serif", Font.PLAIN, 15));
        invalidDateLabel.setForeground(backgroundColor);
        c.insets = new Insets(0,0,0, 0);
        c.gridwidth = 7;
        c.gridx = 0;
        c.gridy++;
        panel.add(invalidDateLabel, c);

        NewTaskLabelTemplate frequencyLabel = new NewTaskLabelTemplate("Select a frequency:", JLabel.LEFT);
        c.gridwidth = 3;
        c.gridy++;
        panel.add(frequencyLabel, c);

        NewTaskLabelTemplate priorityLabel = new NewTaskLabelTemplate("Select a priority:", JLabel.LEFT);
        c.insets = new Insets(0,60,0, 0);
        c.gridwidth = 4;
        c.gridx = 3;
        panel.add(priorityLabel,c);

        String frequencyArray[] = {"one time", "every day", "every week", "every month", "every year", "birthday"};
        JComboBox<String> frequencyComboBox = new JComboBox<>(frequencyArray);
        map.put("frequencyComboBox", frequencyComboBox);
        c.insets = new Insets(0,0,20, 0);
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy++;
        panel.add(frequencyComboBox, c);

        String priority[] = {"1 (highest priority)", "2", "3", "4", "5 (medium priority)", "6", "7", "8", "9 (lowest priority)"};
        JComboBox<String> priorityComboBox = new JComboBox<>(priority);
        map.put("priorityComboBox", priorityComboBox);
        priorityComboBox.setSelectedIndex(4);
        c.insets = new Insets(0,60,20, 0);
        c.gridx = 3;
        c.gridwidth = 3;
        panel.add(priorityComboBox, c);

        NewTaskLabelTemplate titleLabel = new NewTaskLabelTemplate("Enter a title (up to 80 symbols):", JLabel.LEFT);
        c.insets = new Insets(0,0,0, 0);
        c.gridwidth = 7;
        c.gridy++;
        c.gridx = 0;
        panel.add(titleLabel,c);

        JTextArea titleTextArea = new JTextArea(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                    if (getLength() < 80) {
                        super.insertString(offs, str, a);
                    }
            }
        }, "", 2, 50);
        map.put("titleTextArea", titleTextArea);
        titleTextArea.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
        titleTextArea.setFont(new Font(Font.SERIF, Font.BOLD, 14));
        titleTextArea.setLineWrap(true);
        titleTextArea.setWrapStyleWord(true);
        titleTextArea.setMinimumSize(new Dimension(0, 60));
        titleTextArea.setMaximumSize(new Dimension(999, 60));
        c.insets = new Insets(0,0,20, 0);
        c.gridy++;
        panel.add(titleTextArea,c);

        NewTaskLabelTemplate descriptionLabel = new NewTaskLabelTemplate("Enter a description (up to 400 characters):", JLabel.LEFT);
        c.insets = new Insets(0,0,0, 0);
        c.gridy++;
        panel.add(descriptionLabel,c);

        JTextArea descriptionTextArea = new JTextArea(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (getLength() < 400) {
                    super.insertString(offs, str, a);
                }
            }
        }, "", 10, 50);
        map.put("descriptionTextArea", descriptionTextArea);
        descriptionTextArea.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setMinimumSize(new Dimension(0, 190));
        descriptionTextArea.setMaximumSize(new Dimension(9999, 190));
        descriptionTextArea.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
        c.insets = new Insets(0,0,20, 0);
        c.gridy++;
        panel.add(descriptionTextArea,c);

        MainButton cancelButton = new MainButton("Cancel");
        cancelButton.addActionListener(ListenerFactory.createCancelNewTaskListener(dH));
        c.insets = new Insets(0,0,0, 0);
        c.gridy++;
        c.gridwidth = 2;
        panel.add(cancelButton, c);

        MainButton createTaskButton = new MainButton("Create task");
        createTaskButton.addActionListener(ListenerFactory.createCreateNewTaskListener(dH, map));
        c.insets = new Insets(0,-27,0, 0); //It's probably awful, but I don't know how to do it better
        c.gridx = 4;
        c.gridwidth = 3;
        panel.add(createTaskButton, c);

        frequencyComboBox.addItemListener(ListenerFactory.createFrequencyComboBoxListener(dH, titleLabel, titleTextArea, frequencyComboBox));
        return panel;
    }

}
