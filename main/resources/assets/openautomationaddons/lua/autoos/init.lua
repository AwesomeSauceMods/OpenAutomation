local component = require("component")

for c, t in component.list() do
    computer.pushSignal("component_added", c, t)
end
os.sleep(0.5) -- Allow signal processing by libraries.
computer.pushSignal("init") -- so libs know components are initialized.

io.write("AutoOS initialized.")
io.write("Finding auto components.")
