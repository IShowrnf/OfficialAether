package net.aether.module.player;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.NumberSetting;

public class NoFall extends Module
{
    public final NumberSetting threshold = new NumberSetting("Threshold", 3.0D, 1.0D, 10.0D, 0.5D);

    public NoFall()
    {
        super("NoFall", "Prevents fall damage.", Category.PLAYER);
        this.addSettings(this.threshold);
    }

    public void onTick()
    {
        if (this.inGame() && this.player().fallDistance > this.threshold.getFloat())
        {
            this.player().fallDistance = 0.0F;
            this.player().onGround = true;
        }
    }
}
