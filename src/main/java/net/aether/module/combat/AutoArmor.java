package net.aether.module.combat;

import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.NumberSetting;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module
{
    public final NumberSetting delay = new NumberSetting("Delay", 150.0D, 0.0D, 1000.0D, 50.0D);

    private long lastAction;

    public AutoArmor()
    {
        super("AutoArmor", "Automatically equips armor.", Category.COMBAT);
        this.addSettings(this.delay);
    }

    public void onTick()
    {
        if (!this.inGame() || mc.currentScreen != null)
        {
            return;
        }

        if (System.currentTimeMillis() - this.lastAction < (long)this.delay.getInt())
        {
            return;
        }

        for (EntityEquipmentSlot equipmentSlot : new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET})
        {
            ItemStack equipped = this.player().getItemStackFromSlot(equipmentSlot);
            int equippedValue = equipped.getItem() instanceof ItemArmor ? ((ItemArmor)equipped.getItem()).damageReduceAmount : -1;
            int bestSlot = -1;
            int bestValue = equippedValue;

            for (int i = 9; i < 45; ++i)
            {
                ItemStack stack = this.player().inventoryContainer.getSlot(i).getStack();

                if (stack.isEmpty() || !(stack.getItem() instanceof ItemArmor))
                {
                    continue;
                }

                ItemArmor armor = (ItemArmor)stack.getItem();

                if (armor.armorType != equipmentSlot)
                {
                    continue;
                }

                if (armor.damageReduceAmount > bestValue)
                {
                    bestValue = armor.damageReduceAmount;
                    bestSlot = i;
                }
            }

            if (bestSlot != -1)
            {
                this.lastAction = System.currentTimeMillis();
                int armorSlot = 8 - equipmentSlot.getIndex();
                mc.playerController.windowClick(this.player().inventoryContainer.windowId, bestSlot, 0, ClickType.PICKUP, this.player());
                mc.playerController.windowClick(this.player().inventoryContainer.windowId, armorSlot, 0, ClickType.PICKUP, this.player());
                mc.playerController.windowClick(this.player().inventoryContainer.windowId, bestSlot, 0, ClickType.PICKUP, this.player());
                return;
            }
        }
    }
}
