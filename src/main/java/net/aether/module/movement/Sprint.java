package net.aether.module.movement;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.BooleanSetting;

public class Sprint extends Module
{
    public final BooleanSetting omni = new BooleanSetting("Omnidirectional", false);

    public Sprint()
    {
        super("Sprint", "Always sprints.", Category.MOVEMENT);
        this.addSettings(this.omni);
    }

    public void onTick()
    {
        if (!this.inGame())
        {
            return;
        }

        boolean moving = this.omni.getValue()
                ? this.player().movementInput.moveForward != 0.0F || this.player().movementInput.moveStrafe != 0.0F
                : this.player().movementInput.moveForward > 0.0F;

        if (moving && !this.player().isSneaking() && !this.player().isCollidedHorizontally)
        {
            this.player().setSprinting(true);
        }
    }
}
