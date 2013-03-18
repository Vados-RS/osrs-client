package org.osrs;

import com.jgoodies.forms.layout.CellConstraints;
import org.osrs.event.EventManager;
import org.osrs.plugin.PluginManager;
import org.osrs.prop.DefaultProperties;
import org.osrs.prop.Properties;
import org.osrs.prop.Section;
import org.osrs.rs.upd.FieldIdentifier;
import org.osrs.ui.MainFrame;

import javax.swing.*;
import java.applet.Applet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Logger;

/**
 * osrs-client
 * 15.3.2013
 */
public class Launcher {

    private static final Logger log = Logger.getLogger(Launcher.class.getSimpleName());

    private static Properties props;
    private static URLClassLoader classLoader;
    private static ClientReader clientReader;
    private static Thread clientThread, scriptThread;

    public static void main(String args[]) throws Exception {// still no exception handling 4u

        try {
            java.util.logging.LogManager.getLogManager().readConfiguration(
                    new FileInputStream("logging.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        props = new Properties();
        try {
            props.load("oldrsclient.properties");
        } catch (FileNotFoundException ex) {
            log.warning("Properties file not found! Generating one with default settings");
            props = DefaultProperties.get();
            props.save("oldrsclient.properties");
            FieldIdentifier.update();
            props.reload();
        }

        setupLAF();

        PluginManager.getInstance().reloadScripts();
        EventManager.getInstance().trigger("init");

        Section lsec = props.getSection("launcher");
        URL baseURL = new URL(lsec.getProperty("base_url"));

        boolean alreadyUpdated = false;
        Applet applet = null;
        while (applet == null) {
            try {
                if (!new File("gamepack.jar").exists())
                    throw new IOException();
                applet = loadGame(new ClientStub(props.getSection("applet").getEntries(), baseURL, baseURL));
                EventManager.getInstance().trigger("rs_init");
            } catch (IOException ex) {
                if (alreadyUpdated) {
                    throw new Exception("Unable to load game after update! Wait patiently for an update by the developers.");
                }
                alreadyUpdated = true;
                log.warning("LAUNCHER> Unable to load game, retrying...");
                FieldIdentifier.update();
                props.reload();
                lsec = props.getSection("launcher");
                baseURL = new URL(lsec.getProperty("base_url"));
            }
        }

        MainFrame f = new MainFrame("Old School RuneScape Client");
        f.init();
        CellConstraints constraints = new CellConstraints(1, 1);
        f.getMainForm().getAppletPanel().add(applet, constraints);
        f.setResizable(false);
        f.showFrame();
        log.info("Client initialized.");

        clientReader = new ClientReader(applet);
        clientThread = new Thread(clientReader);
        scriptThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        EventManager.getInstance().tick();
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static Applet loadGame(AbstractAppletStub stub) throws IOException {
        classLoader = null;
        try {
            classLoader = new URLClassLoader(new URL[]{
                    new File("gamepack.jar").toURI().toURL()
            });
            Class<?> client = classLoader.loadClass("client");
            Applet applet = (Applet) client.newInstance();
            applet.setStub(stub);
            applet.init();
            applet.start();
            return applet;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (classLoader != null)
                classLoader.close();
            throw new IOException("Error when loading game");
        }
    }

    public static Properties getProps() {
        return props;
    }

    public static URLClassLoader getClassLoader() {
        return classLoader;
    }

    private static void setupLAF() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (info.getName().equals("Nimbus")) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            setupDefaultLAF();
        }
    }

    private static void setupDefaultLAF() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}