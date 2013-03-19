package org.osrs.ui;

import org.osrs.updater.ClientUpdater;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * User: Johan
 * Date: 2013-03-18
 * Time: 15:56
 */
public class UpdaterForm {
    private JPanel rootPanel;
    private JProgressBar downloadProgress;

    public void start() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Updater");
        rootPanel.setSize(new Dimension(300, 70));
        dialog.add(rootPanel);
        dialog.setSize(new Dimension(300, 70));
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://72.44.88.164/osrs/osrs.jar");
                    URLConnection conn = url.openConnection();
                    int length = conn.getContentLength();
                    downloadProgress.setMaximum(length);
                    BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
                    FileOutputStream fos = new FileOutputStream("./update.tmp");
                    BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);

                    byte data[] = new byte[1024];
                    int c;
                    int curBytes = 0;
                    while ((c = in.read(data, 0, 1024)) >= 0) {
                        bout.write(data, 0, c);
                        curBytes += c;
                        downloadProgress.setValue(curBytes);
                    }
                    bout.close();
                    in.close();
                    ClientUpdater.getInstance().flagUpdate();
                    ClientUpdater.getInstance().restart();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        new UpdaterForm().start();
    }

}
