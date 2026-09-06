package net.aether.module.movement;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.BooleanSetting;
import net.minecraft.client.settings.KeyBinding;

public class AutoWalk extends Module
{
    public final BooleanSetting sprint = new BooleanSetting("Sprint", true);

    public AutoWalk()
    {
        super("AutoWalk", "Walks forward automatically.", Category.MOVEMENT);
        this.addSettings(this.sprint);
    }

    public void onDisable()
    {
        if (mc.gameSettings != null)
        {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        }
    }

    public void onTick()
    {
        if (!this.inGame())
        {
            return;
        }

        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);

        if (this.sprint.getValue())
        {
            this.player().setSprinting(true);
        }
    }
}
