package org.osrs.ui.modules;

import org.osrs.util.TextAreaHandler;

import javax.swing.*;
import java.io.ByteArrayInputStream;

/**
 * User: Johan
 * Date: 2013-03-16
 * Time: 15:42
 */
public class LogModuleForm {
    private JPanel logPanel;
    private JTextArea logTextArea;

    public LogModuleForm() {
        super();
    }

    public JTextArea getLogArea() {
        return logTextArea;
    }

    public JPanel getRootPanel() {
        return logPanel;
    }

}
