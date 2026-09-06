package net.aether.module.movement;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.NumberSetting;
import net.minecraft.util.math.MathHelper;

public class Speed extends Module
{
    public final NumberSetting multiplier = new NumberSetting("Multiplier", 1.5D, 1.0D, 5.0D, 0.1D);

    public Speed()
    {
        super("Speed", "Moves faster than vanilla.", Category.MOVEMENT);
        this.addSettings(this.multiplier);
    }

    public void onTick()
    {
        if (!this.inGame())
        {
            return;
        }

        float forward = this.player().movementInput.moveForward;
        float strafe = this.player().movementInput.moveStrafe;

        if (forward == 0.0F && strafe == 0.0F)
        {
            return;
        }

        double speed = 0.2873D * this.multiplier.getValue();
        float yaw = this.player().rotationYaw;
        this.player().motionX = forward * speed * (double)(-MathHelper.sin(yaw * 0.017453292F)) + strafe * speed * (double)MathHelper.cos(yaw * 0.017453292F);
        this.player().motionZ = forward * speed * (double)MathHelper.cos(yaw * 0.017453292F) - strafe * speed * (double)(-MathHelper.sin(yaw * 0.017453292F));
    }
}
