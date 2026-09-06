package net.aether.module.combat;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.BooleanSetting;
import net.aether.setting.NumberSetting;
import net.aether.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.EnumHand;

public class CrystalAura extends Module
{
    public final NumberSetting breakRange = new NumberSetting("Break Range", 4.5D, 1.0D, 6.0D, 0.1D);
    public final NumberSetting breakDelay = new NumberSetting("Break Delay", 50.0D, 0.0D, 500.0D, 10.0D);
    public final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    public final BooleanSetting antiWeakness = new BooleanSetting("Anti Weakness", false);

    private long lastBreak;

    public CrystalAura()
    {
        super("CrystalAura", "Automatically places and attacks crystals.", Category.COMBAT);
        this.addSettings(this.breakRange, this.breakDelay, this.rotate, this.antiWeakness);
    }

    public void onTick()
    {
        if (!this.inGame() || mc.currentScreen != null)
        {
            return;
        }

        if (System.currentTimeMillis() - this.lastBreak < (long)this.breakDelay.getInt())
        {
            return;
        }

        EntityEnderCrystal closest = null;
        double closestDistance = this.breakRange.getValue();

        for (Entity entity : this.world().loadedEntityList)
        {
            if (!(entity instanceof EntityEnderCrystal) || !entity.isEntityAlive())
            {
                continue;
            }

            double distance = (double)this.player().getDistanceToEntity(entity);

            if (distance < closestDistance)
            {
                closestDistance = distance;
                closest = (EntityEnderCrystal)entity;
            }
        }

        if (closest == null)
        {
            return;
        }

        if (this.rotate.getValue())
        {
            EntityUtil.faceEntity(closest, false);
        }

        this.lastBreak = System.currentTimeMillis();
        mc.playerController.attackEntity(this.player(), closest);
        this.player().swingArm(EnumHand.MAIN_HAND);
    }
}
