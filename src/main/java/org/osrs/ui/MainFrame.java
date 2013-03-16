package org.osrs.ui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.osrs.event.EventManager;
import org.osrs.plugin.PluginManager;
import org.osrs.ui.modules.LogModuleForm;
import org.osrs.util.TextAreaHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;

/**
 * osrs-client
 * 15.3.2013
 */
public class MainFrame extends JFrame {

    //private PanelBuilder builder;
    //private CellConstraints constraints;
    private Container container;
    private MenuBar menuBar;

    private MainForm mainForm;
    private PluginManagerForm pluginManagerForm;
    private PluginModuleForm pluginModule_north, pluginModule_west, pluginModule_east, pluginModule_south;

    public MainFrame(String title) {
        super(title);
        mainForm = new MainForm();
        pluginManagerForm = new PluginManagerForm();
        pluginModule_south = new PluginModuleForm();
        pluginModule_south.getRootPanel().setPreferredSize(new Dimension(503, 200));
    }

    public MainForm getMainForm() {
        return mainForm;
    }

    public void init() {
        CellConstraints constraints = new CellConstraints(1, 1, CellConstraints.FILL, CellConstraints.FILL);
        LogModuleForm log = new LogModuleForm();
        pluginModule_south.addTab("Log", log.getRootPanel());

        EventManager.getInstance().trigger("swing_ui", pluginModule_south);

        TextAreaHandler.setTextArea(log.getLogArea());
        mainForm.getSouthPanel().add(pluginModule_south.getRootPanel(), constraints);
        add(mainForm.getRootPanel());


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
                JDialog frame = new JDialog(MainFrame.this);
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

        Menu menuView = new Menu("View");
        CheckboxMenuItem menuItemNorth, menuItemSouth, menuItemWest, menuItemEast;

        menuItemNorth = new CheckboxMenuItem("Show North Module", false);
        menuItemWest = new CheckboxMenuItem("Show West Module", false);
        menuItemSouth = new CheckboxMenuItem("Show South Module", true);
        menuItemEast = new CheckboxMenuItem("Show East Module", false);

        menuItemSouth.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                mainForm.getSouthPanel().setVisible(e.getStateChange() == ItemEvent.SELECTED);
                setSize(new Dimension(785, (e.getStateChange() == ItemEvent.SELECTED) ? getSize().height + 200 : getSize().height - 200));
            }
        });

        //menuView.add(menuItemNorth);
        //menuView.add(menuItemWest);
        menuView.add(menuItemSouth);
        //menuView.add(menuItemEast);

        menuBar.add(menuFile);
        menuBar.add(menuPlugins);
        menuBar.add(menuView);
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
