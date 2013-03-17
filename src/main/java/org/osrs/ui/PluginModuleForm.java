package org.osrs.ui;

import org.osrs.ui.components.DnDTabbedPane;

import javax.swing.*;

/**
 * User: Johan
 * Date: 2013-03-16
 * Time: 15:38
 */
public class PluginModuleForm {
    private JTabbedPane pluginPane;
    private JPanel panel1;

    public void addTab(String name, JPanel panel) {
        pluginPane.addTab(name, panel);
    }

    public JPanel getRootPanel() {
        return panel1;
    }

    public JTabbedPane getTabPane() {
        return pluginPane;
    }
}
