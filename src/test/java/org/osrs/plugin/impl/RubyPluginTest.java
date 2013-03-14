package org.osrs.plugin.impl;

import org.jruby.CompatVersion;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;
import org.osrs.event.EventManager;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Johan
 * Date: 2013-03-14
 * Time: 11:02
 */
public class RubyPluginTest {

    public static void main(String[] args) {
        new RubyPluginTest();
    }

    public RubyPluginTest() {
        ScriptingContainer container = new ScriptingContainer(LocalContextScope.SINGLETON);
        container.setCompatVersion(CompatVersion.RUBY2_0);

        List<String> loadPaths = new ArrayList();
        loadPaths.add("src/main/ruby");
        container.setLoadPaths(loadPaths);

        container.runScriptlet(PathType.RELATIVE, "src/test/ruby/plugin_test.rb");
        EventManager.getInstance().trigger("data_test", 600);
        EventManager.getInstance().trigger("init");
        EventManager.getInstance().tick();
    }
}
