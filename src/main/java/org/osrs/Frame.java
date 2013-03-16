package org.osrs;

import org.osrs.event.EventManager;

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
public class Frame extends JFrame {

    //private PanelBuilder builder;
    //private CellConstraints constraints;
    private Container container;
    private MenuBar menuBar;

    public Frame(String title) {
        super(title);
        //builder = new PanelBuilder(new FormLayout());
        //constraints = new CellConstraints();
    }

    public void init() {
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
        Menu menu = new Menu("File");
        MenuItem menuItem = new MenuItem("Exit");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventManager.getInstance().trigger("terminate");
                System.exit(0);
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        setMenuBar(menuBar);
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
