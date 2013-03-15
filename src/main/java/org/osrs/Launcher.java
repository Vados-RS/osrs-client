package org.osrs;

import org.osrs.event.EventManager;
import org.osrs.plugin.PluginManager;
import org.osrs.prop.DefaultProperties;
import org.osrs.prop.Properties;
import org.osrs.prop.Section;
import org.osrs.upd.Updater;

import java.applet.Applet;
import java.io.File;
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
        props = new Properties();
        try {
            props.load("oldrsclient.properties");
        } catch (FileNotFoundException ex) {
            log.warning("Properties file not found! Generating one with default settings");
            props = DefaultProperties.get();
            props.save("oldrsclient.properties");
            Updater.update();
            props.reload();
        }

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
                Updater.update();
                props.reload();
                lsec = props.getSection("launcher");
                baseURL = new URL(lsec.getProperty("base_url"));
            }
        }

        Frame f = new Frame("Old School RuneScape Game");
        f.init();
        f.add(applet);
        f.showFrame();

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
}