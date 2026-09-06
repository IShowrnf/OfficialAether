package net.aether.setting;

public abstract class Setting
{
    private final String name;
    private String group = "General";

    public Setting(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public String getGroup()
    {
        return this.group;
    }

    public Setting group(String group)
    {
        this.group = group;
        return this;
    }

    public abstract String serialize();

    public abstract void deserialize(String value);
}
