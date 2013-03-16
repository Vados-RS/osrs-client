package org.osrs.ui;

import org.osrs.plugin.PluginDescriptor;
import org.osrs.plugin.PluginManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * User: Johan
 * Date: 2013-03-16
 * Time: 12:55
 */
public class PluginManagerForm {
    private JPanel mainPanel;
    private JPanel controlPanel;
    private JTabbedPane tabPane;
    private JPanel tabPlugins;
    private JTable pluginTable;
    private JPanel tabDownloadPlugins;
    private JScrollPane scrollPane;
    private JButton buttonRefresh;

    private DefaultTableModel pluginModel = new DefaultTableModel();

    public PluginManagerForm() {
        super();
        pluginModel.addColumn("Name");
        pluginModel.addColumn("Description");
        pluginModel.addColumn("Version");
        pluginTable.setModel(pluginModel);
        refreshData();
    }

    public static void main(String[] args) {
        JDialog frame = new JDialog();
        frame.setContentPane(new PluginManagerForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Plugin Manager");
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void refreshData() {
        clearData();
        for (PluginDescriptor d : PluginManager.getInstance().getAll()) {
            setData(d);
        }
    }

    public void clearData() {
        for (int i = 0; i < pluginModel.getRowCount(); i++) {
            pluginModel.removeRow(i);
        }
    }

    public void setData(PluginDescriptor data) {
        pluginModel.addRow(new Object[] {data.getName(),  data.getDescription(), data.getVersion()});
    }

    public void getData(PluginDescriptor data) {

    }

    public boolean isModified(PluginDescriptor data) {
        return false;
    }
}
