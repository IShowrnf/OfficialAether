package net.aether.module.movement;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.NumberSetting;
import net.minecraft.util.math.MathHelper;

public class Flight extends Module
{
    public final NumberSetting speed = new NumberSetting("Speed", 1.0D, 0.1D, 5.0D, 0.1D);

    public Flight()
    {
        super("Flight", "Lets you fly freely.", Category.MOVEMENT);
        this.addSettings(this.speed);
    }

    public void onDisable()
    {
        if (this.inGame())
        {
            this.player().capabilities.isFlying = false;
        }
    }

    public void onTick()
    {
        if (!this.inGame())
        {
            return;
        }

        this.player().motionY = 0.0D;

        if (this.player().movementInput.jump)
        {
            this.player().motionY = this.speed.getValue() * 0.5D;
        }

        if (this.player().movementInput.sneak)
        {
            this.player().motionY = -this.speed.getValue() * 0.5D;
        }

        float forward = this.player().movementInput.moveForward;
        float strafe = this.player().movementInput.moveStrafe;

        if (forward == 0.0F && strafe == 0.0F)
        {
            this.player().motionX = 0.0D;
            this.player().motionZ = 0.0D;
            return;
        }

        double s = this.speed.getValue() * 0.5D;
        float yaw = this.player().rotationYaw;
        this.player().motionX = forward * s * (double)(-MathHelper.sin(yaw * 0.017453292F)) + strafe * s * (double)MathHelper.cos(yaw * 0.017453292F);
        this.player().motionZ = forward * s * (double)MathHelper.cos(yaw * 0.017453292F) - strafe * s * (double)(-MathHelper.sin(yaw * 0.017453292F));
        this.player().fallDistance = 0.0F;
    }
}
