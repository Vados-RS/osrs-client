package org.osrs.ui;

import org.osrs.event.EventManager;
import org.osrs.plugin.PluginManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * osrs-client
 * 15.3.2013
 */
public class MainFrame extends JFrame {

    //private PanelBuilder builder;
    //private CellConstraints constraints;
    private Container container;
    private MenuBar menuBar;

    private PluginManagerForm pluginManagerForm;

    public MainFrame(String title) {
        super(title);
        //builder = new PanelBuilder(new FormLayout());
        //constraints = new CellConstraints();
    }

    public void init() {
        pluginManagerForm = new PluginManagerForm();
        //setContentPane(builder.getContainer());
        container = getContentPane();
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.CENTER;
        gridBagLayout.setConstraints(container, gridBagConstraints);
        container.setLayout(gridBagLayout);
        container.setBackground(Color.black);
        setContentPane(container);

        menuBar = new MenuBar();
        buildMenu();
        setMenuBar(menuBar);
    }

    public void buildMenu() {
        Menu menuFile = new Menu("File");
        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventManager.getInstance().trigger("terminate");
                System.exit(0);
            }
        });

        menuFile.add(menuItemExit);

        Menu menuPlugins = new Menu("Plugins");
        MenuItem menuItemManage = new MenuItem("Manage");
        MenuItem menuItemReload = new MenuItem("Reload");
        menuItemManage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog frame = new JDialog();
                frame.setContentPane(pluginManagerForm.getMainPanel());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setTitle("Plugin Manager");
                frame.setResizable(false);
                frame.setAlwaysOnTop(true);
                frame.pack();
                frame.setVisible(true);
            }
        });
        menuItemReload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PluginManager.getInstance().reloadScripts();
            }
        });

        menuPlugins.add(menuItemManage);
        menuPlugins.add(menuItemReload);


        menuBar.add(menuFile);
        menuBar.add(menuPlugins);
    }

    public void showFrame() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                EventManager.getInstance().trigger("terminate");
                System.exit(0);
            }
        });
    }
}
