configuration do
  name "Test Plugin"
  description "A simple test plugin."
  author "Speljohan"
  version "1.0"
end

on :init do
  puts "Hello!"
end

frame do
  title "Hello There!"
  size 300, 100
  always_on_top true
  label do
    text "Hai Der!"
  end

  checkbox do
    text "Check me!"
    selected false
  end

  button do
    text "Click me!"
    bind "wut"
  end

end

action :clicked, "wut" do
  message "You clicked me!"
end

on :terminate do
  puts "Bye!"
end