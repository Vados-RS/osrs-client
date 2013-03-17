package org.osrs.ui;

import javax.swing.*;

/**
 * User: Johan
 * Date: 2013-03-16
 * Time: 15:54
 */
public class MainForm {
    private JPanel rootPanel;
    private JPanel appletPanel;
    private JPanel southPanel;
    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel northPanel;

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JPanel getAppletPanel() {
        return appletPanel;
    }

    public JPanel getSouthPanel() {
        return southPanel;
    }

    public JPanel getEastPanel() {
        return eastPanel;
    }

    public JPanel getWestPanel() {
        return westPanel;
    }

    public JPanel getNorthPanel() {
        return northPanel;
    }

    public JTabbedPane getPreferredTab() {
        JPanel[] panels = new JPanel[] {southPanel, eastPanel, northPanel, westPanel};

        for (int i = 0; i < panels.length; i++) {
            JTabbedPane m = ((JTabbedPane)((JPanel) panels[i].getComponent(0)).getComponent(0));
            boolean horizontal = (i == 0 || i == 2);
            int maxTabs;
            if (horizontal) {
                maxTabs = 8;
            } else {
                maxTabs = 3;
            }
            if (m.getTabCount() >= maxTabs)
                continue;

            return m;
        }
        return ((JTabbedPane)((JPanel) southPanel.getComponent(0)).getComponent(0));
    }
}
