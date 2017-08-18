package com.logicdevil.todolist;

import javax.swing.*;
import java.awt.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.sql.*;

/**
 * Created by daymond on 7/26/17.
 */
public class DataHandler {
    private ArrayList<Task> tasks;
    private ArrayList<String> birthdays;
    private ArrayList<JCheckBox> checkBoxes;
    /*
    ---------------------------------Create string for tooltip text for tasks----------------------
     */
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
        s.append(";      Priority: ").append(t.getPriority()).append("</html>");
        return s.toString();
    }
    /*
    -------------------------------------------Update time of topPanel's label------------------------------
     */
    void updateDateTime(Component component) {
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy    HH:mm");
            ((JLabel) component).setText(LocalDateTime.now().format(format));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    LocalDateTime getTime() {
        return LocalDateTime.now();
    }
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
        ResultSet rs = null;
        try {
            // Подгружаем драйвер
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //export CLASSPATH=/home/daymond/Documents/Java/mysql-connector-java-5.1.43-bin.jar:$CLASSPATH
        } catch (Exception e) {
            //System.out.println("failed to load jdbc driver class");
            JOptionPane.showMessageDialog(null, "failed to load jdbc driver class");
            System.exit(0);
        }
        try {
            // Работаем с БД
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/to_do_list?useSSL=false","to_do_list","");
            st = conn.createStatement();
            LocalDate currentDate = LocalDate.now();
            int currentDay = currentDate.getDayOfMonth(), currentMonth = currentDate.getMonthValue(), currentYear = currentDate.getYear();
            rs = st.executeQuery("SELECT * FROM futureTasks WHERE (day=" + currentDay + " AND month=" + currentMonth +
                    " AND year=" + currentYear + ") OR (year<" + currentYear + ") OR (year =" + currentYear + " AND month<" +
                    currentMonth + ") OR (year=" + currentYear + " AND month=" + currentMonth + " AND day<" + currentDay
                    + ") ORDER BY priority");
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
                boolean isBirthday = rs.getBoolean(10);
                if(isBirthday) {
                    tasks.add(0, new Task("Birthday of " + title + " (" + LocalDate.of(year, month, day).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ")", description, LocalDate.of(year, month, day),
                            LocalTime.of(Integer.parseInt(time.substring(0, 2)), Integer.parseInt(time.substring(3))),
                            1, frequency, id));
                } else
                tasks.add(new Task(title, description, LocalDate.of(year,month,day),
                        LocalTime.of(Integer.parseInt(time.substring(0,2)), Integer.parseInt(time.substring(3))),
                        priority, frequency, id));
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

                    String query = "INSERT INTO futureTasks (title, description, priority, frequency, day, month, year, time, isBirthday)"
                            + " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    // create the mysql insert preparedstatement
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setString (1, title);
                    preparedStmt.setString (2, description);
                    preparedStmt.setInt(3, priority);
                    preparedStmt.setString(4, frequency);
                    preparedStmt.setInt(5, date.getDayOfMonth());
                    preparedStmt.setInt(6, date.getMonthValue());
                    preparedStmt.setInt(7, date.getYear());
                    preparedStmt.setString(8, time);
                    preparedStmt.setBoolean(9, isBirthday);
                    // execute the preparedstatement
                    preparedStmt.execute();
                }
            }
            LocalDate futureDate = LocalDate.now().plusMonths(1); //here will be executed birthdays
            int futureDay = futureDate.getDayOfMonth(), futureMonth = futureDate.getMonthValue(), futureYear = futureDate.getYear();
            rs = st.executeQuery("SELECT * FROM futureTasks WHERE isBirthday=TRUE AND ((day=" + futureDay + " AND month=" + futureMonth +
                    " AND year=" + futureYear + ") OR (year<" + futureYear + ") OR (year =" + futureYear + " AND month<" +
                    futureMonth + ") OR (year=" + futureYear + " AND month=" + futureMonth + " AND day<" + futureDay
                    + ")) ORDER BY year, month, day");
            while (rs.next()) {
                LocalDate date = LocalDate.of(rs.getInt("year"), rs.getInt("month"), rs.getInt("day"));
                birthdays.add(date.format(DateTimeFormatter.ofPattern("ddMMyyyy")) + rs.getString("title"));
            }
        } catch (SQLException e) {
            if (conn == null) {
                //System.out.println("failed to connect to database");
                JOptionPane.showMessageDialog(null, "failed to connect to database");

            } else
            if (st == null) {
                //System.out.println("failed to create statement");
                JOptionPane.showMessageDialog(null, "failed to create statement");

            } else
            if (rs == null) {
                //System.out.println("failed to fetch resultset");
                JOptionPane.showMessageDialog(null, "failed to fetch resultset");

            } else {
                //System.out.println("error while fetching results");
                JOptionPane.showMessageDialog(null, "error " + e.getMessage());
            }

        } finally {
            if (rs != null) {
                try { rs.close(); } catch (Exception e) {JOptionPane.showMessageDialog(null, "Can't close ResultSet: " + e.getMessage());}
            }
            if (st != null) {
                try { st.close(); } catch (Exception e) {JOptionPane.showMessageDialog(null, "Can't close Statement: " + e.getMessage());}
            }
            if (conn != null) {
                try { conn.close(); } catch (Exception e) {JOptionPane.showMessageDialog(null, "Can't close Connection: " + e.getMessage());}
            }
        }
        return tasks;
    }

    void setCheckBoxes(ArrayList<JCheckBox> cB) {
        checkBoxes = cB;
    }
}
