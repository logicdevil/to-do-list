package com.logicdevil.todolist;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * Created by daymond on 7/28/17.
 */
public class MainButton extends JButton {
    MainButton(String s) {
        super(s);
        setBackground(new Color(10,10,10));
        setForeground(new Color(130,130,130));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        setFocusPainted(false);
    }
}
