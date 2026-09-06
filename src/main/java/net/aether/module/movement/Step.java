package net.aether.module.movement;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.NumberSetting;

public class Step extends Module
{
    public final NumberSetting height = new NumberSetting("Height", 1.0D, 0.5D, 2.5D, 0.5D);

    public Step()
    {
        super("Step", "Step up full blocks instantly.", Category.MOVEMENT);
        this.addSettings(this.height);
    }

    public void onDisable()
    {
        if (this.inGame())
        {
            this.player().stepHeight = 0.6F;
        }
    }

    public void onTick()
    {
        if (this.inGame())
        {
            this.player().stepHeight = this.height.getFloat();
        }
    }
}
