package net.aether.module.combat;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.NumberSetting;

public class Velocity extends Module
{
    public final NumberSetting horizontal = new NumberSetting("Horizontal", 0.0D, 0.0D, 100.0D, 1.0D);
    public final NumberSetting vertical = new NumberSetting("Vertical", 0.0D, 0.0D, 100.0D, 1.0D);

    public Velocity()
    {
        super("Velocity", "Reduces knockback.", Category.COMBAT);
        this.addSettings(this.horizontal, this.vertical);
    }

    public void onTick()
    {
        if (!this.inGame() || this.player().hurtTime <= 0)
        {
            return;
        }

        double h = this.horizontal.getValue() / 100.0D;
        double v = this.vertical.getValue() / 100.0D;
        this.player().motionX *= h;
        this.player().motionZ *= h;

        if (this.player().motionY > 0.0D)
        {
            this.player().motionY *= v;
        }
    }
}
