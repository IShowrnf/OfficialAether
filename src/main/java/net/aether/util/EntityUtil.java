package net.aether.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class EntityUtil
{
    private static final Minecraft mc = Minecraft.getMinecraft();

    /**
     * Returns {yaw, pitch} needed to look at the given entity's upper body.
     */
    public static float[] getRotationsTo(Entity target)
    {
        double dx = target.posX - mc.player.posX;
        double dz = target.posZ - mc.player.posZ;
        double dy = target.posY + (double)target.getEyeHeight() - (mc.player.posY + (double)mc.player.getEyeHeight());
        double dist = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0D);
        float pitch = (float)(-Math.toDegrees(Math.atan2(dy, dist)));
        return new float[] {yaw, pitch};
    }

    public static void faceEntity(Entity target, boolean smooth)
    {
        float[] rotations = getRotationsTo(target);

        if (!smooth)
        {
            mc.player.rotationYaw = rotations[0];
            mc.player.rotationPitch = rotations[1];
            return;
        }

        float yawDelta = MathHelper.wrapDegrees(rotations[0] - mc.player.rotationYaw);
        float pitchDelta = MathHelper.wrapDegrees(rotations[1] - mc.player.rotationPitch);
        float step = 0.35F;
        mc.player.rotationYaw += yawDelta * step;
        mc.player.rotationPitch += pitchDelta * step;
    }

    /**
     * Absolute yaw difference between where the player is looking and the target, in degrees.
     */
    public static float getAngleTo(Entity target)
    {
        float[] rotations = getRotationsTo(target);
        return Math.abs(MathHelper.wrapDegrees(rotations[0] - mc.player.rotationYaw));
    }
}
