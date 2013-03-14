require 'java'
require 'ostruct'

java_import 'org.osrs.event.EventManager'
java_import 'org.osrs.plugin.PluginManager'
java_import 'org.osrs.plugin.PluginDescriptor'

@config = OpenStruct.new

def configuration
  yield @config
  @config.id = @config.name.downcase.gsub(" ", "_")
  PluginManager.getInstance.register(PluginDescriptor.new(@config.id, @config.name, @config.description, @config.version))

end

def on(what, &block)
    EventManager.getInstance.register(@config.id, what, &block)
end