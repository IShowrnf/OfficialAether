package net.aether.module.player;

import net.aether.module.Category;
import net.aether.module.Module;
import net.minecraft.client.gui.GuiGameOver;

public class AutoRespawn extends Module
{
    public AutoRespawn()
    {
        super("AutoRespawn", "Instantly respawns after death.", Category.PLAYER);
    }

    public void onTick()
    {
        if (this.inGame() && mc.currentScreen instanceof GuiGameOver)
        {
            this.player().respawnPlayer();
            mc.displayGuiScreen((net.minecraft.client.gui.GuiScreen)null);
        }
    }
}
