package org.osrs.util;

import java.util.logging.*;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class TextAreaHandler extends Handler {

    public static JTextArea jTextArea = null;

    static public void setTextArea(JTextArea jTextArea) {
        TextAreaHandler.jTextArea = jTextArea;
    }

    private Level level = Level.INFO;

    public TextAreaHandler() {
        Filter filter = new Filter() {
            public boolean isLoggable(LogRecord record) {
                return record.getLevel().intValue() >= level.intValue();
            }};
        this.setFilter(filter);
    }

    @Override
    public void publish(LogRecord logRecord) {
        if (!getFilter().isLoggable(logRecord)) return;
        final String message = new LogFormatter().format(logRecord);
        if (jTextArea != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    jTextArea.append(message);
                }
            });
        }
    }
    @Override
    public void close() throws SecurityException {}
    @Override
    public void flush() {}

    @Override
    public void setLevel(Level level) {
        this.level = level;
        super.setLevel(level);
    }

    class LogFormatter extends Formatter {
        public String format(LogRecord record) {
            return String.format("%s [%s]: %s\n", record.getLoggerName(), record.getLevel().getName().substring(0, 4), record.getMessage());
        }
    }

}
