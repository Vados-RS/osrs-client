package org.osrs.plugin;

/**
 * User: Johan
 * Date: 2013-03-14
 * Time: 10:38
 */
public class PluginDescriptor {

    private String id, name, description, version;

    public PluginDescriptor(String id, String name, String description, String version) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

}
