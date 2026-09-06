package net.aether.module.misc;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.BooleanSetting;
import net.aether.setting.ModeSetting;

public class Hud extends Module
{
    public final ModeSetting colour = new ModeSetting("Colour", "Rainbow", "Rainbow", "Accent", "White");
    public final BooleanSetting background = new BooleanSetting("Background", true);

    public Hud()
    {
        super("HUD", "Renders the on-screen overlay. Element visibility and position live in the HUD tab.", Category.MISC);
        this.addSettings(this.colour, this.background);
        this.setEnabled(true);
    }
}
