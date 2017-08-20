package com.logicdevil.todolist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
}
