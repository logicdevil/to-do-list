package com.logicdevil.todolist;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
    static ItemListener createFrequencyComboBoxListener(DataHandler dH, NewTaskLabelTemplate nTLT, JTextArea jTA, JComboBox jCB) {
        return (ItemEvent e) -> {
            dH.frequencyWasChanged(nTLT, jTA, jCB);
        };
    }
}
