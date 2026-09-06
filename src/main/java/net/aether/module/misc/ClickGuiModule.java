package net.aether.module.misc;

import net.aether.gui.clickgui.ClickGui;
import net.aether.module.Category;
import net.aether.module.Module;

public class ClickGuiModule extends Module
{
    public ClickGuiModule()
    {
        super("ClickGUI", "Opens the Aether interface.", Category.MISC);
    }

    public void onEnable()
    {
        mc.displayGuiScreen(new ClickGui());
        this.setEnabled(false);
    }
}
