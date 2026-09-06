package net.aether.setting;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting
{
    private final List<String> modes;
    private int index;

    public ModeSetting(String name, String defaultMode, String... modes)
    {
        super(name);
        this.modes = Arrays.asList(modes);
        this.index = Math.max(0, this.modes.indexOf(defaultMode));
    }

    public String getMode()
    {
        return this.modes.get(this.index);
    }

    public boolean is(String mode)
    {
        return this.getMode().equalsIgnoreCase(mode);
    }

    public void setMode(String mode)
    {
        int i = this.modes.indexOf(mode);

        if (i != -1)
        {
            this.index = i;
        }
    }

    public void cycle()
    {
        this.index = (this.index + 1) % this.modes.size();
    }

    public List<String> getModes()
    {
        return this.modes;
    }

    public String serialize()
    {
        return this.getMode();
    }

    public void deserialize(String value)
    {
        this.setMode(value);
    }
}
