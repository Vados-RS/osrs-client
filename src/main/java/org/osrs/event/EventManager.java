package org.osrs.event;

import org.jruby.RubyProc;
import org.jruby.RubyString;
import org.jruby.RubySymbol;
import org.osrs.plugin.PluginManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * User: Johan
 * Date: 2013-03-14
 * Time: 12:22
 */
public class EventManager {

    private static EventManager ourInstance = new EventManager();

    public static EventManager getInstance() {
        return ourInstance;
    }

    private final Queue<Trigger> queue;
    private final ArrayList<Trigger> triggers;

    public EventManager() {
        this.queue = new LinkedList<Trigger>();
        this.triggers = new ArrayList<Trigger>();
    }

    public void register(RubyString id, RubySymbol trigger, RubyProc proc) {
        triggers.add(new Trigger(trigger.asJavaString(), id.asJavaString(), proc));
    }

    public void trigger(String trigger, Object... args) {
        for (Trigger t : triggers) {
            if (trigger.equals(t.getTrigger())) {
                if (args != null) {
                    t.setArgs(args);
                }
                queue.add(t);
            }
        }
    }

    public void tick() {
        synchronized (queue) {
            Trigger e;

            while ((e = queue.poll()) != null) {
                PluginManager.getInstance().container.callMethod(e.getBlock(), "call", e.getArgs());
            }
        }
    }

}
