package net.aether.hud;

import net.minecraft.client.Minecraft;

public abstract class HudElement
{
    protected static final Minecraft mc = Minecraft.getMinecraft();

    private final String name;
    private double x;
    private double y;
    private boolean enabled;

    public HudElement(String name, double x, double y, boolean enabled)
    {
        this.name = name;
        this.x = x;
        this.y = y;
        this.enabled = enabled;
    }

    public abstract void render();

    public String getName()
    {
        return this.name;
    }

    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public void setPosition(double x, double y)
    {
        this.x = Math.max(0.0D, x);
        this.y = Math.max(0.0D, y);
    }

    public boolean isEnabled()
    {
        return this.enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
