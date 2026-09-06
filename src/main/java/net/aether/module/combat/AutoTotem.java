package net.aether.module.combat;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.BooleanSetting;
import net.aether.setting.NumberSetting;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class AutoTotem extends Module
{
    public final NumberSetting health = new NumberSetting("Health", 12.0D, 1.0D, 20.0D, 0.5D);
    public final BooleanSetting always = new BooleanSetting("Always", true);
    public final NumberSetting delay = new NumberSetting("Delay", 100.0D, 0.0D, 1000.0D, 50.0D);

    private long lastSwap;

    public AutoTotem()
    {
        super("AutoTotem", "Automatically uses totems.", Category.COMBAT);
        this.addSettings(this.health, this.always, this.delay);
    }

    public void onTick()
    {
        if (!this.inGame() || mc.currentScreen != null)
        {
            return;
        }

        if (System.currentTimeMillis() - this.lastSwap < (long)this.delay.getInt())
        {
            return;
        }

        if (!this.always.getValue() && this.player().getHealth() + this.player().getAbsorptionAmount() > this.health.getFloat())
        {
            return;
        }

        if (this.player().getHeldItem(EnumHand.OFF_HAND).getItem() == Items.TOTEM_OF_UNDYING)
        {
            return;
        }

        int slot = this.findTotemSlot();

        if (slot == -1)
        {
            return;
        }

        this.lastSwap = System.currentTimeMillis();
        mc.playerController.windowClick(this.player().inventoryContainer.windowId, slot, 0, ClickType.PICKUP, this.player());
        mc.playerController.windowClick(this.player().inventoryContainer.windowId, 45, 0, ClickType.PICKUP, this.player());
        mc.playerController.windowClick(this.player().inventoryContainer.windowId, slot, 0, ClickType.PICKUP, this.player());
    }

    private int findTotemSlot()
    {
        for (int i = 9; i < 45; ++i)
        {
            ItemStack stack = this.player().inventoryContainer.getSlot(i).getStack();

            if (!stack.isEmpty() && stack.getItem() == Items.TOTEM_OF_UNDYING)
            {
                return i;
            }
        }

        return -1;
    }
}
