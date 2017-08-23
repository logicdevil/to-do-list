package com.logicdevil.todolist;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.text.Format;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.sql.*;
import java.util.Formatter;
import java.util.HashMap;

/**
 * Created by daymond on 7/26/17.
 */
public class DataHandler {
    private MainPanel mainPanel;
    private NewTaskPanel newTaskPanel;
    private JFrame mainFrame;
    private ArrayList<Task> tasks;
    private ArrayList<String> birthdays;
    private ArrayList<JCheckBox> checkBoxes;
    public DataHandler(JFrame mF) {mainFrame = mF;}
    void setMainPanel(MainPanel mP) {mainPanel = mP;}
    void hideMainPanel() {mainPanel.setVisible(false);}
    void showMainPanel() {mainPanel.setVisible(true);}
    void addNewTaskPanelToFrame(NewTaskPanel nTP) {
        newTaskPanel = nTP;
        mainFrame.add(nTP);
    }
    void removeNewTaskPanelFromFrame() {
        mainFrame.remove(newTaskPanel);
        newTaskPanel = null;
    }
    void removeCompletedTasks() {
        ArrayList<Integer> ids = new ArrayList<>();
        for(JCheckBox cB : checkBoxes)
            if(cB.isSelected())
                ids.add(tasks.get(checkBoxes.indexOf(cB)).getId());
        if(ids.size() != 0) {
            StringBuilder idsString = new StringBuilder();
            for (int id : ids)
                idsString.append(id).append(", ");
            idsString.delete(idsString.length() - 2, idsString.length());
            Connection conn = null;
            PreparedStatement ps = null;
            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list?useSSL=false", "to_do_list", "");
                ps = conn.prepareStatement("INSERT INTO completedTasks (SELECT * FROM futureTasks WHERE task_id IN (" + idsString + "))");
                ps.execute();
                ps = conn.prepareStatement("DELETE FROM futureTasks WHERE task_id IN (" + idsString + ")");
                ps.execute();
            } catch (SQLException e) {
                if (conn == null)
                    JOptionPane.showMessageDialog(null, "failed to connect to database");
                else if (ps == null)
                    JOptionPane.showMessageDialog(null, "failed to fetch resultset");
                else
                    JOptionPane.showMessageDialog(null, "error: " + e.getMessage());
            } finally {
                if (ps != null)
                    try { ps.close(); }
                    catch (Exception e) { JOptionPane.showMessageDialog(null, "Can't close PreparedStatement: " + e.getMessage());  }
                if (conn != null)
                    try { conn.close(); }
                    catch (Exception e) { JOptionPane.showMessageDialog(null, "Can't close Connection: " + e.getMessage()); }
            }
        }
    }
    /*
    ---------------------------------Create string for tooltip text for tasks----------------------
     */
    String getToolTipText(Task t) {
        StringBuilder s = new StringBuilder("<html>" + t.getTitle() + "<br>");
        String description = t.getDescription();
        if(description.length() > 80) {     //separates the description text into several parts
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
        else s.append(description).append("<br>");
        s.append("Frequency: ").append(t.getFrequency());
        s.append(";      Priority: ").append(t.getPriority());
        s.append(";      Date:  ").append(t.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).append("</html>");
        return s.toString();
    }
    /*
    -------------------------------------------Update time of topPanel's label------------------------------
     */
    void updateDateTime(JLabel dateLabel, JLabel timeLabel) {
        timeLabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        int hour = (timeLabel.getText().charAt(0) - 48) * 10 + (timeLabel.getText().charAt(1) - 48);
        if(hour == 0)
            dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    LocalTime getTime() {
        return LocalTime.now();
    }
    LocalDate getDate() { return LocalDate.now(); }
    /*
    -------------------------------------------Create string of nearest birthdays--------------------------
     */
    String getBirthdaysString() {
        StringBuilder stringBuilder = new StringBuilder("<html>");
        LocalDate date = LocalDate.now();
        for(String s : birthdays) {
            stringBuilder.append("Birthday of ").append(s.substring(8)).append(" in ");
            stringBuilder.append(date.until(LocalDate.parse(s.subSequence(0,8), DateTimeFormatter.ofPattern("ddMMyyyy"))).getDays());
            stringBuilder.append(" days<br><p style='margin-top:2'>");
        }
        if(birthdays.size() == 0)
            stringBuilder.append("Within 30 days no birthday<br> is expected");
        return stringBuilder.toString();
    }
    /*
    -------------------------------------------Create array of tasks from database--------------------------
     */
    ArrayList<Task> getTaskList() {
        tasks = new ArrayList<>();
        birthdays = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        Statement st2 = null;
        ResultSet rs = null;
        ResultSet dopRS = null;
        PreparedStatement preparedStmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();   //load the driver
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "failed to load jdbc driver class");
            System.exit(0);
        }
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list?useSSL=false","to_do_list","");
            st = conn.createStatement();
            st2=conn.createStatement();
            LocalDate currentDate = LocalDate.now();
            int currentDay = currentDate.getDayOfMonth(), currentMonth = currentDate.getMonthValue(), currentYear = currentDate.getYear();
            rs = st.executeQuery("SELECT * FROM futureTasks WHERE (day=" + currentDay + " AND month=" + currentMonth +
                    " AND year=" + currentYear + ") OR (year<" + currentYear + ") OR (year =" + currentYear + " AND month<" +
                    currentMonth + ") OR (year=" + currentYear + " AND month=" + currentMonth + " AND day<" + currentDay
                    + ") ORDER BY year, month, day, priority");
            while (rs.next()) {
                int id = rs.getInt(1);
                String title = rs.getString(2);
                String description = rs.getString(3);
                int priority = rs.getInt(4);
                String frequency = rs.getString(5);
                int day = rs.getInt(6);
                int month = rs.getInt(7);
                int year = rs.getInt(8);
                String time = rs.getString(9);
                if("birthday".equals(frequency)) {
                    tasks.add(0, new Task("Birthday of " + title + " (" +
                            LocalDate.of(year, month, day).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ")",
                            description, LocalDate.of(year, month, day), time,1, frequency, id));
                } else
                    tasks.add(new Task(title, description, LocalDate.of(year,month,day), time, priority, frequency, id));
                if(!frequency.equals("one time")) {
                    LocalDate  date = LocalDate.now();
                    switch(frequency) {
                        case "every year":
                        case "birthday":
                            date = date.plusYears(1);
                            break;
                        case "every month":
                            date = date.plusMonths(1);
                            break;
                        case "every week":
                            date = date.plusWeeks(1);
                            break;
                        case "every day":
                            date = date.plusDays(1);
                            break;
                        default:break;
                    }
                    dopRS = st2.executeQuery("SELECT task_id FROM futureTasks WHERE title='" + title + "' AND frequency='" + frequency +
                    "' AND day=" + date.getDayOfMonth() + " AND month=" + date.getMonthValue() + " AND year=" + date.getYear());
                    if(!dopRS.next()) {
                        String query = "INSERT INTO futureTasks (title, description, priority, frequency, day, month, year, time)"
                                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                        preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setString(1, title);
                        preparedStmt.setString(2, description);
                        preparedStmt.setInt(3, priority);
                        preparedStmt.setString(4, frequency);
                        preparedStmt.setInt(5, date.getDayOfMonth());
                        preparedStmt.setInt(6, date.getMonthValue());
                        preparedStmt.setInt(7, date.getYear());
                        preparedStmt.setString(8, time);
                        preparedStmt.execute();
                    }
                }
            }
            LocalDate futureDate = LocalDate.now().plusMonths(1); //here will be executed birthdays
            int futureDay = futureDate.getDayOfMonth(), futureMonth = futureDate.getMonthValue(), futureYear = futureDate.getYear();
            rs = st.executeQuery("SELECT * FROM futureTasks WHERE frequency='birthday' AND ((day=" + futureDay + " AND month=" + futureMonth +
                    " AND year=" + futureYear + ") OR (year<" + futureYear + ") OR (year =" + futureYear + " AND month<" +
                    futureMonth + ") OR (year=" + futureYear + " AND month=" + futureMonth + " AND day<" + futureDay
                    + ")) ORDER BY year, month, day");
            while (rs.next()) {
                LocalDate date = LocalDate.of(rs.getInt("year"), rs.getInt("month"), rs.getInt("day"));
                birthdays.add(date.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + rs.getString("title"));
            }
        } catch (SQLException e) {
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "failed to connect to database");
                System.exit(0);

            } else
            if (st == null) {
                JOptionPane.showMessageDialog(null, "failed to create statement");
                System.exit(0);
            } else
            if (rs == null || preparedStmt == null || dopRS == null) {
                JOptionPane.showMessageDialog(null, "failed to fetch resultset");

            } else {
                JOptionPane.showMessageDialog(null, "error: " + e.getMessage());
            }

        } finally {
            if (rs != null)
                try { rs.close(); }
                catch (Exception e) {JOptionPane.showMessageDialog(null, "Can't close ResultSet: " + e.getMessage());}
            if (dopRS != null)
                try { dopRS.close(); }
                catch (Exception e) {JOptionPane.showMessageDialog(null, "Can't close ResultSet2: " + e.getMessage());}
            if (preparedStmt != null)
                try { preparedStmt.close(); }
                catch (Exception e) {JOptionPane.showMessageDialog(null, "Can't close PreparedStatement: " + e.getMessage());}
            if (st != null)
                try { st.close(); }
                catch (Exception e) {JOptionPane.showMessageDialog(null, "Can't close Statement: " + e.getMessage());}
            if (conn != null)
                try { conn.close(); }
                catch (Exception e) {JOptionPane.showMessageDialog(null, "Can't close Connection: " + e.getMessage());}
        }
        return tasks;
    }

    void setCheckBoxes(ArrayList<JCheckBox> cB) {
        checkBoxes = cB;
    }
    void createNewTask(HashMap<String, JComponent> map) {
        boolean isTimeNeeded = ((JCheckBox)map.get("timeCheckBox")).isSelected();
        boolean isCanBeCreated = true;
        String hour = ((JComboBox<String>) map.get("hourComboBox")).getSelectedItem().toString();
        String minute = ((JComboBox<String>) map.get("minuteComboBox")).getSelectedItem().toString();
        String time = (isTimeNeeded) ? hour + ":" + minute : "nn:nn";
        int day = Integer.parseInt(((JComboBox<Integer>) map.get("dayComboBox")).getSelectedItem().toString());
        int month = Integer.parseInt(((JComboBox<Integer>) map.get("monthComboBox")).getSelectedItem().toString());
        int year = Integer.parseInt(((JComboBox<Integer>) map.get("yearComboBox")).getSelectedItem().toString());

        if(isTimeNeeded && LocalTime.parse(time).isBefore(LocalTime.now()) && LocalDate.of(year, month, day).isEqual(LocalDate.now())) {
            isCanBeCreated = false;
            (map.get("invalidTimeLabel")).setForeground(new Color(255,100,100));
        }
        else  (map.get("invalidTimeLabel")).setForeground(new Color(50, 50, 50));

        if(LocalDate.of(year, month, day).isBefore(LocalDate.now())) {
            isCanBeCreated = false;
            (map.get("invalidDateLabel")).setForeground(new Color(255,100,100));
        }
        else (map.get("invalidDateLabel")).setForeground(new Color(50, 50, 50));

        String frequency = ((JComboBox<String>)map.get("frequencyComboBox")).getSelectedItem().toString();
        boolean isBirthday = ("birthday".equals(frequency));
        int priority = Integer.parseInt(((JComboBox<String>)map.get("priorityComboBox")).getSelectedItem().toString().substring(0, 1));
        String title = ((JTextArea)map.get("titleTextArea")).getText();
        String description = ((JTextArea)map.get("descriptionTextArea")).getText();
        description = description.substring(0, (description.length() > 400) ? 400 : description.length());
        title = (isBirthday) ? title.substring(0, ((title.length() > 20) ? 20 : title.length()))
                : title.substring(0, (title.length() > 80) ? 80 : title.length());
        if(isCanBeCreated) {
            Connection conn = null;
            Statement st = null;
            ResultSet rs = null;
            PreparedStatement preparedStmt = null;
           /* try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "failed to load jdbc driver class");
                System.exit(0);
            }*/
            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list?useSSL=false","to_do_list","");
                st = conn.createStatement();
                rs = st.executeQuery("SELECT title, day, month, year, frequency FROM futureTasks WHERE day=" + day + " AND month=" + month +
                        " AND year=" + year + " AND title='" + title + "' AND frequency='" + frequency + "'");
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "A similar task is exist: " +
                            "\ntitle: " + rs.getString("title") +
                            "\ndate: " + rs.getInt("day") + "." + rs.getInt("month") + "." + rs.getInt("year") +
                            "\nfrequency: " + rs.getString("frequency") +
                            "\ntry another date/title/frequency");
                }
                else {
                        String query = "INSERT INTO futureTasks (title, description, priority, frequency, day, month, year, time)"
                                + " values (?, ?, ?, ?, ?, ?, ?, ?)";

                        preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setString (1, title);
                        preparedStmt.setString (2, description);
                        preparedStmt.setInt(3, priority);
                        preparedStmt.setString(4, frequency);
                        preparedStmt.setInt(5, day);
                        preparedStmt.setInt(6, month);
                        preparedStmt.setInt(7, year);
                        preparedStmt.setString(8, time);
                        preparedStmt.execute();
                    mainPanel.update(this);
                    this.showMainPanel();
                    this.removeNewTaskPanelFromFrame();

                }
            } catch (SQLException e) {
                if (conn == null) {
                    JOptionPane.showMessageDialog(null, "failed to connect to database");
                } else
                if (st == null) {
                    JOptionPane.showMessageDialog(null, "failed to create statement");
                } else
                if (rs == null) {
                    JOptionPane.showMessageDialog(null, "failed to fetch resultset");
                } else
                    JOptionPane.showMessageDialog(null, "error " + e.getMessage());
            } finally {
                if (rs != null)
                    try { rs.close(); }
                    catch (Exception e) {JOptionPane.showMessageDialog(null, "Can't close ResultSet: " + e.getMessage());}
                if (preparedStmt != null)
                    try { preparedStmt.close(); }
                    catch (Exception e) {JOptionPane.showMessageDialog(null, "Can't close PreparedStatement: " + e.getMessage());}
                if (st != null)
                    try { st.close(); }
                    catch (Exception e) {JOptionPane.showMessageDialog(null, "Can't close Statement: " + e.getMessage());}
                if (conn != null)
                    try { conn.close(); }
                    catch (Exception e) {JOptionPane.showMessageDialog(null, "Can't close Connection: " + e.getMessage());}
            }
        }
    }

    void frequencyWasChanged(NewTaskLabelTemplate titleLabel, JTextArea titleTextArea, JComboBox<String> frequencyComboBox) {
        if("birthday".equals(frequencyComboBox.getSelectedItem().toString())) {
            titleLabel.setText("Enter the name and (or) the surname (up to 20 symbols):");
            String text = titleTextArea.getText();
            text = text.substring(0, (text.length() > 20) ? 20 : text.length());
            titleTextArea.setDocument(new PlainDocument() {
                @Override
                public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                    if (getLength() < 20) {
                        super.insertString(offs, str, a);
                    }
                }
            });
            titleTextArea.setText(text);
        }
        else {
            titleLabel.setText("Enter a title (up to 80 symbols):");
            String text = titleTextArea.getText();
            text = text.substring(0, (text.length() > 80) ? 80 : text.length());
            titleTextArea.setDocument(new PlainDocument() {
                @Override
                public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                    if (getLength() < 80) {
                        super.insertString(offs, str, a);
                    }
                }
            });
            titleTextArea.setText(text);
        }
    }
}
