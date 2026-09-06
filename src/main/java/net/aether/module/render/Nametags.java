package net.aether.module.render;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.BooleanSetting;
import net.aether.setting.NumberSetting;

public class Nametags extends Module
{
    public final NumberSetting scale = new NumberSetting("Scale", 1.0D, 0.5D, 3.0D, 0.1D);
    public final BooleanSetting health = new BooleanSetting("Health", true);
    public final BooleanSetting armor = new BooleanSetting("Armor", true);

    public Nametags()
    {
        super("Nametags", "Enlarges player nametags.", Category.RENDER);
        this.addSettings(this.scale, this.health, this.armor);
    }
}
