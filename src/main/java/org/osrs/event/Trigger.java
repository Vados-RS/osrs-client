package org.osrs.event;

import org.jruby.RubyProc;

/**
 * User: Johan
 * Date: 2013-03-14
 * Time: 13:42
 */
public class Trigger {
    private String trigger, owner;
    private RubyProc block;
    private Object[] args;

    public Trigger(String trigger, String owner, RubyProc block) {
        this.trigger = trigger;
        this.owner = owner;
        this.block = block;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getTrigger() {
        return trigger;
    }

    public String getOwner() {
        return owner;
    }

    public RubyProc getBlock() {
        return block;
    }
}
