package net.aether.setting;

public class NumberSetting extends Setting
{
    private final double min;
    private final double max;
    private final double increment;
    private double value;

    public NumberSetting(String name, double value, double min, double max, double increment)
    {
        super(name);
        this.value = value;
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public double getValue()
    {
        return this.value;
    }

    public float getFloat()
    {
        return (float)this.value;
    }

    public int getInt()
    {
        return (int)Math.round(this.value);
    }

    public void setValue(double value)
    {
        double clamped = Math.max(this.min, Math.min(this.max, value));
        this.value = Math.round(clamped / this.increment) * this.increment;
    }

    /**
     * Sets the value from a 0..1 slider position.
     */
    public void setFromFraction(double fraction)
    {
        this.setValue(this.min + (this.max - this.min) * Math.max(0.0D, Math.min(1.0D, fraction)));
    }

    public double getFraction()
    {
        if (this.max - this.min == 0.0D)
        {
            return 0.0D;
        }

        return (this.value - this.min) / (this.max - this.min);
    }

    public boolean isInteger()
    {
        return this.increment >= 1.0D;
    }

    public String getDisplayValue()
    {
        return this.isInteger() ? Integer.toString(this.getInt()) : String.format("%.1f", this.value);
    }

    public double getMin()
    {
        return this.min;
    }

    public double getMax()
    {
        return this.max;
    }

    public String serialize()
    {
        return Double.toString(this.value);
    }

    public void deserialize(String value)
    {
        try
        {
            this.setValue(Double.parseDouble(value));
        }
        catch (NumberFormatException numberformatexception)
        {
        }
    }
}
