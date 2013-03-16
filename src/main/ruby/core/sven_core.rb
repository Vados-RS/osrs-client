# sven_core.rb

require 'java'
require 'jruby'

require_relative 'config_struct'

require_relative 'sven_view'

java_import 'org.osrs.event.EventManager'
java_import 'org.osrs.plugin.PluginManager'
java_import 'org.osrs.plugin.PluginDescriptor'

# Core

LISTENERS = {}

def configuration(&block)
  @config = ConfigStruct.new
  @config.instance_eval &block
  @config.id @config.name.downcase.gsub(" ", "_")
  PluginManager.getInstance.register(PluginDescriptor.new(@config.id, @config.name, @config.description, @config.version))

end

def on(what, args = {:threaded => true}, &block)
  threaded = args.delete(:threaded)
  EventManager.getInstance.register(@config.id, what, threaded, &block)
end

def action(what, id)
  LISTENERS[id] = OpenStruct.new(:type => what, :id => id, :block => Proc.new { yield })
end

def view(ui, &block)
  view = View.define 0, &block
  ui.addTab @config.name, view
end

def frame(&block)
  view = View.define 1, &block
  view.setVisible true
end

def message(msg)
  javax.swing.JOptionPane.show_message_dialog nil, msg
end

module Kernel
  def puts (s)
    org.osrs.util.TextAreaHandler.jTextArea.append "[#{@config.id}]: #{s}\n" rescue ""
  end
end