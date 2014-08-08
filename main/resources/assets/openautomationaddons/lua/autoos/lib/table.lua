local table = {}
function table:hasValue(t, value)
    for k, v in pairs(t) do
        if (v == value) then
            return true
        end
    end
    return false
end

return table