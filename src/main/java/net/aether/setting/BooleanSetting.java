package net.aether.setting;

public class BooleanSetting extends Setting
{
    private boolean value;

    public BooleanSetting(String name, boolean value)
    {
        super(name);
        this.value = value;
    }

    public boolean getValue()
    {
        return this.value;
    }

    public void setValue(boolean value)
    {
        this.value = value;
    }

    public void toggle()
    {
        this.value = !this.value;
    }

    public String serialize()
    {
        return Boolean.toString(this.value);
    }

    public void deserialize(String value)
    {
        this.value = Boolean.parseBoolean(value);
    }
}
