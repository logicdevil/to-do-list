package com.logicdevil.todolist;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;

/**
 * Created by daymond on 8/20/17.
 */
public final class ListenerFactory {
    static ActionListener createNewTaskListener(DataHandler dH) {
        return (ActionEvent e)-> {
            dH.addNewTaskPanelToFrame(new NewTaskPanel(dH));
            dH.hideMainPanel();
        };
    }
    static ActionListener createCancelNewTaskListener(DataHandler dH) {
        return (ActionEvent e)->{
            dH.showMainPanel();
            dH.removeNewTaskPanelFromFrame();
        };
    }
    static ActionListener createCreateNewTaskListener(DataHandler dH, HashMap<String, JComponent> map) {
        return (ActionEvent e) -> {
            dH.createNewTask(map);
        };
    }
    static ItemListener createFrequencyComboBoxListener(DataHandler dH, NewTaskLabelTemplate nTLT, JTextArea jTA, JComboBox<String> jCB) {
        return (ItemEvent e) -> {
            dH.frequencyWasChanged(nTLT, jTA, jCB);
        };
    }
    static WindowListener createWindowListener(DataHandler dH, JFrame frame) {
        return new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to exit the program?", "Exit Program Message Box",
                        JOptionPane.YES_NO_OPTION);
                if (confirmed == JOptionPane.YES_OPTION) {
                    dH.removeCompletedTasks();
                    frame.dispose();
                    System.exit(0);
                }
            }
        };
    }
}
