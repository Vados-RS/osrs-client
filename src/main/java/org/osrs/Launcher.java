package org.osrs;

import org.osrs.event.EventManager;
import org.osrs.plugin.PluginManager;
import org.osrs.prop.DefaultProperties;
import org.osrs.prop.Properties;
import org.osrs.prop.Section;
import org.osrs.upd.Updater;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * oldrsclient
 * 4.3.2013
 */
public class Launcher {

    private static Properties props;
    private static ClientReader clientReader;
    private static Thread clientThread;
    private static URLClassLoader classLoader;
    private static Thread scriptThread;

    public static void main(String args[]) throws Exception {// no exceptio nhandling 4 u
        PluginManager.getInstance().loadScripts();
        scriptThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        EventManager.getInstance().tick();
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        scriptThread.start();
        EventManager.getInstance().trigger("init");
        props = new Properties();

        try {
            props.load("oldrsclient.properties");
        } catch(FileNotFoundException ex) {
            System.err.println("Properties file not found! Generating default settings");
            props = DefaultProperties.get();
            props.save("oldrsclient.properties");
            Updater upd = new Updater();
            upd.update();
        }

        Section launcherSection = props.getSection("launcher");

        URL baseURL = new URL(launcherSection.getProperty("base_url"));

        Launcher launcher = new Launcher();

        Applet applet = null;
        while(applet == null) {
            try {
                if(!new File("gamepack.jar").exists())
                    throw new Exception("");
                applet = launcher.loadGame(new ClientStub(props.getSection("applet").getEntries(), baseURL, baseURL));
            } catch(Exception ex) {
                if(classLoader != null)
                    classLoader.close();
                System.err.println("Unable to load applet!");
            }
        }

        clientReader = new ClientReader(applet);
        clientThread = new Thread(clientReader);
        clientThread.start();

        JFrame frame = new JFrame("Old School RuneScape Client");

        Container frameContainer = frame.getContentPane();
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.CENTER;
        gridBagLayout.setConstraints(frameContainer, gridBagConstraints);
        frameContainer.setLayout(gridBagLayout);

        frameContainer.add(applet);
        frameContainer.setBackground(Color.black);
        frame.setContentPane(frameContainer);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                EventManager.getInstance().trigger("terminate");
                System.exit(0);
            }
        });
        EventManager.getInstance().trigger("rs_init");
    }

    public Applet loadGame(AbstractAppletStub appletStub)
            throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        classLoader = new URLClassLoader(new URL[] {new File("gamepack.jar").toURI().toURL()});
        Class<?> appletClass = classLoader.loadClass("client");
        Applet applet = (Applet) appletClass.newInstance();
        applet.setStub(appletStub);
        applet.init();
        applet.start();
        return applet;
    }

    public static Properties getProps() {
        return props;
    }

    public static URLClassLoader getClassLoader() {
        return classLoader;
    }
}
