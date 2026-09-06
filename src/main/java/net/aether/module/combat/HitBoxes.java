package net.aether.module.combat;

import net.aether.module.Category;
import net.aether.module.Module;

public class HitBoxes extends Module
{
    public HitBoxes()
    {
        super("HitBoxes", "Renders hitboxes around entities.", Category.COMBAT);
    }

    public void onEnable()
    {
        if (mc.getRenderManager() != null)
        {
            mc.getRenderManager().setDebugBoundingBox(true);
        }
    }

    public void onDisable()
    {
        if (mc.getRenderManager() != null)
        {
            mc.getRenderManager().setDebugBoundingBox(false);
        }
    }
}
