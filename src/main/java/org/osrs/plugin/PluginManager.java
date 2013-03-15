package org.osrs.plugin;

import org.jruby.CompatVersion;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

/**
 * User: Johan
 * Date: 2013-03-14
 * Time: 10:44
 */
public class PluginManager {
    private static PluginManager ourInstance = new PluginManager();

    public ScriptingContainer container;
    private Map<String, PluginDescriptor> plugins;

    public static PluginManager getInstance() {
        return ourInstance;
    }

    public void register(PluginDescriptor pluginDescriptor) {
        plugins.put(pluginDescriptor.getName(), pluginDescriptor);
        System.out.println("PLUGINS> Plugin '" + pluginDescriptor.getName() + "' Registered.");
    }

    public void unregister(PluginDescriptor pluginDescriptor) {
        plugins.remove(pluginDescriptor.getName());
    }

    public Collection<PluginDescriptor> getAll() {
        return plugins.values();
    }

    public void loadScripts() {
        File root = new File("./scripts");
        File files[] = root.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".rb");
            }
        });
        for(int i = 0; files != null && i < files.length; i++) {
            container.runScriptlet(PathType.ABSOLUTE, files[i].getAbsolutePath());
        }
    }

    private PluginManager() {
        container = new ScriptingContainer(LocalContextScope.CONCURRENT);
        container.setCompatVersion(CompatVersion.RUBY2_0);
        List<String> loadPaths = new ArrayList();
        loadPaths.add("./scripts");
        container.setLoadPaths(loadPaths);
        plugins = new HashMap<String, PluginDescriptor>();
    }
}
