local component = require("component")
local table = require("table")

for c, t in component.list() do
    computer.pushSignal("component_added", c, t)
end
os.sleep(0.5) -- Allow signal processing by libraries.
computer.pushSignal("init") -- so libs know components are initialized.
local supportedComponentTypes = {}
io.write("AutoOS initialized.")
io.write("Locating components.")
local components = {}
local i = 0
for c, t in component.list() do
    if table:hasValue(supportedComponentTypes, t) then
        components[i] = component.proxy(c)
    end
end
for index, component in pairs(components) do
    
end