package com.logicdevil.todolist;

import javax.swing.*;
import java.awt.*;

/**
 * Created by daymond on 8/17/17.
 */
public class NewTaskLabelTemplate extends JLabel{
    public NewTaskLabelTemplate(String text, int horizontalAligment) {
        super(text, horizontalAligment);
        setFont(new Font("Serif", Font.PLAIN, 15));
        setForeground(new Color(150,150,150));
    }
}
