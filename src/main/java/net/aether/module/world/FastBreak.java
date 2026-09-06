package net.aether.module.world;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.NumberSetting;

public class FastBreak extends Module
{
    public final NumberSetting damage = new NumberSetting("Damage", 1.0D, 0.1D, 1.0D, 0.05D);

    public FastBreak()
    {
        super("FastBreak", "Breaks blocks faster.", Category.WORLD);
        this.addSettings(this.damage);
    }

    public void onDisable()
    {
        if (mc.playerController != null)
        {
            mc.playerController.setBlockHitDelay(0);
        }
    }

    public void onTick()
    {
        if (mc.playerController != null)
        {
            mc.playerController.setBlockHitDelay(0);
        }
    }
}
