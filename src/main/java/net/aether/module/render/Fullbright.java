package net.aether.module.render;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.NumberSetting;

public class Fullbright extends Module
{
    public final NumberSetting brightness = new NumberSetting("Brightness", 10.0D, 1.0D, 20.0D, 0.5D);

    private float previousGamma;

    public Fullbright()
    {
        super("Fullbright", "Removes darkness from the world.", Category.RENDER);
        this.addSettings(this.brightness);
    }

    public void onEnable()
    {
        if (mc.gameSettings != null)
        {
            this.previousGamma = mc.gameSettings.gammaSetting;
        }
    }

    public void onDisable()
    {
        if (mc.gameSettings != null)
        {
            mc.gameSettings.gammaSetting = this.previousGamma;
        }
    }

    public void onTick()
    {
        if (mc.gameSettings != null)
        {
            mc.gameSettings.gammaSetting = this.brightness.getFloat();
        }
    }
}
