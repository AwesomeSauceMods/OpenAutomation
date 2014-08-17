local component = require("component")
local table = require("table")

for c, t in component.list() do
    computer.pushSignal("component_added", c, t)
end
os.sleep(0.5) -- Allow signal processing by libraries.
computer.pushSignal("init") -- so libs know components are initialized.
io.write("AutoOS initialized.")
io.write("Locating components.")
local components = {}
local i = 0
for c, t in component.list() do
    local driver = require(t)
    components[i] = { driver, component.proxy(c) }
    driver.register(components[i][1])
end
io.write("Components loaded.")
