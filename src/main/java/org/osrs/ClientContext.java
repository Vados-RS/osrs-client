package org.osrs;

import org.osrs.rs.upd.FieldIdentifier;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * oldrsclient
 * 4.3.2013
 */
public class ClientContext implements AppletContext {

    public AudioClip getAudioClip(URL url) {
        System.out.println("APPLET> ClientContext.getAudioClip(\"" + url + "\")");
        return null;
    }

    public Image getImage(URL url) {
        return Toolkit.getDefaultToolkit().getImage(url);
    }

    public Applet getApplet(String name) {
        System.out.println("APPLET> ClientContext.getApplet(\"" + name + "\")");
        return null;
    }

    public Enumeration<Applet> getApplets() {
        System.out.println("APPLET> ClientContext.getApplets()");
        return null;
    }

    public void showDocument(URL url) {
        System.out.println("APPLET> ClientContext.showDocument(\"" + url + "\")");
        if (url.getFile().indexOf("js5connect_outofdate") > 0) {
            try {
                System.err.println("LAUNCHER> Game has been updated!");
                FieldIdentifier.update();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public void showDocument(URL url, String target) {
        showDocument(url);
    }

    public void showStatus(String status) {
        System.out.println("APPLET> ClientContext.showStatus(\"" + status + "\")");
    }

    public void setStream(String key, InputStream stream) throws IOException {
        System.out.println("APPLET> ClientContext.setStream(\"" + key + "\", " + stream + ")");
    }

    public InputStream getStream(String key) {
        System.out.println("APPLET> ClientContext.getStream(\"" + key + "\")");
        return null;
    }

    public Iterator<String> getStreamKeys() {
        System.out.println("APPLET> ClientContext.getStreamKeys()");
        return null;
    }
}
