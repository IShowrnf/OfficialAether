package net.aether.module.world;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.NumberSetting;

public class AutoRejoin extends Module
{
    public final NumberSetting delay = new NumberSetting("Delay", 3.0D, 1.0D, 30.0D, 1.0D);

    public AutoRejoin()
    {
        super("AutoRejoin", "Reconnects to the server after a disconnect.", Category.WORLD);
        this.addSettings(this.delay);
    }
}
