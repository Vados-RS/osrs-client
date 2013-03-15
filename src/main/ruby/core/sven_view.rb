require 'java'

require 'string_ext'

module View

  module Components

    def label(&block)
      generate_rscript_helpers javax.swing.JLabel #Temporary
      c = javax.swing.JLabel.new
      c.instance_eval &block
      add c
    end

    def button(&block)
      generate_rscript_helpers javax.swing.JButton #Temporary
      c = javax.swing.JButton.new
      c.instance_eval &block
      add c
    end

    def checkbox(&block)
      generate_rscript_helpers javax.swing.JCheckBox #Temporary
      c = javax.swing.JCheckBox.new
      c.instance_eval &block
      add c
    end

    def generate_java_aliases(cls)
      setters = {}
      cls.instance_methods.each do |m|
        if m.match /\bset[A-Z][a-z]*/
          setters[m] = m.to_s.underscore.gsub("set_", "")
        end
      end
      cls.class_eval { setters.each_pair { |k, v| alias_method v.to_sym, k.to_sym } }
    end

    def generate_action_methods(cls)
      cls.instance_methods.each do |m|
        if m.to_s == "addActionListener"
          cls.class_eval do
            def bind(id)
              setActionCommand id
              addActionListener do |event|
                LISTENERS[event.getActionCommand].block.call
              end
            end
          end
        end
      end
    end

    def generate_rscript_helpers(cls)
      generate_java_aliases cls
      generate_action_methods cls
    end

  end

  class Panel < javax.swing.JPanel
    include Components

    def initialize
      generate_rscript_helpers javax.swing.JPanel
    end
  end

  class Frame < javax.swing.JFrame
    include Components

    def initialize
      generate_rscript_helpers javax.swing.JFrame
    end
  end

  def self.define(type, &block)
    if type == 0
      kls = View::Panel.new
    elsif type == 1
      kls = View::Frame.new
    end
    kls.instance_eval &block
    return kls
  end
end