configuration do
  name "Test Plugin"
  description "A simple test plugin."
  author "Speljohan"
  version "1.0"
end

on :init do
  puts "Ohai!"
end

on :swing_ui, :threaded => false do | ui|
  view ui do
    label do
      text "Hai Der!"
    end
    checkbox "my_checkbox" do
      text "Check me!"
      selected false
    end
    button do
      text "Click me!"
      bind "wut"
    end
  end
end

action :clicked, "wut" do
  check = get "my_checkbox"
  message "You clicked me, the checkbox is #{check.selected?}"
end

on :terminate do
  puts "Bye!"
end