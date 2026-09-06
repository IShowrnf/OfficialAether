package net.aether.module.combat;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.BooleanSetting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class AntiBot extends Module
{
    public final BooleanSetting tabCheck = new BooleanSetting("Tab Check", true);
    public final BooleanSetting healthCheck = new BooleanSetting("Health Check", true);

    private final Set<UUID> bots = new HashSet<UUID>();

    public AntiBot()
    {
        super("AntiBot", "Removes bots from your world.", Category.COMBAT);
        this.addSettings(this.tabCheck, this.healthCheck);
    }

    public void onDisable()
    {
        this.bots.clear();
    }

    public void onTick()
    {
        if (!this.inGame() || mc.getConnection() == null)
        {
            return;
        }

        this.bots.clear();

        for (Entity entity : this.world().loadedEntityList)
        {
            if (!(entity instanceof EntityPlayer) || entity == this.player())
            {
                continue;
            }

            EntityPlayer other = (EntityPlayer)entity;

            if (this.tabCheck.getValue())
            {
                NetworkPlayerInfo info = mc.getConnection().getPlayerInfo(other.getUniqueID());

                if (info == null)
                {
                    this.bots.add(other.getUniqueID());
                    continue;
                }
            }

            if (this.healthCheck.getValue() && other.getHealth() <= 0.0F)
            {
                this.bots.add(other.getUniqueID());
            }
        }
    }

    public boolean isBot(EntityPlayer player)
    {
        return this.bots.contains(player.getUniqueID());
    }
}
