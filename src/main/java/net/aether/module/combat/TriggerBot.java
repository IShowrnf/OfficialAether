package net.aether.module.combat;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.BooleanSetting;
import net.aether.setting.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;

public class TriggerBot extends Module
{
    public final NumberSetting delay = new NumberSetting("Delay", 100.0D, 0.0D, 1000.0D, 10.0D);
    public final BooleanSetting playersOnly = new BooleanSetting("Players Only", false);

    private long lastAttack;

    public TriggerBot()
    {
        super("TriggerBot", "Attacks when crosshair is on target.", Category.COMBAT);
        this.addSettings(this.delay, this.playersOnly);
    }

    public void onTick()
    {
        if (!this.inGame() || mc.currentScreen != null)
        {
            return;
        }

        if (System.currentTimeMillis() - this.lastAttack < (long)this.delay.getInt())
        {
            return;
        }

        RayTraceResult result = mc.objectMouseOver;

        if (result == null || result.typeOfHit != RayTraceResult.Type.ENTITY)
        {
            return;
        }

        Entity entity = result.entityHit;

        if (!(entity instanceof EntityLivingBase) || entity == this.player())
        {
            return;
        }

        if (this.playersOnly.getValue() && !(entity instanceof EntityPlayer))
        {
            return;
        }

        this.lastAttack = System.currentTimeMillis();
        mc.playerController.attackEntity(this.player(), entity);
        this.player().swingArm(EnumHand.MAIN_HAND);
    }
}
