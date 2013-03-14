require 'ruby_plugin'

configuration do | c|
  c.name = "Test Plugin"
  c.description = "A simple test plugin."
  c.author = "Speljohan"
  c.version = "1.0"
end
@var = 5

on :init do
  puts "Hello!"
end

on :rs_init do
  puts "RS Initialized!"
end

on :data_test do | args|
  puts "Passed value #{args}"
end

on :execute do
  puts "#{@var + 10}"
end

on :terminate do
  puts "Bye!"
end