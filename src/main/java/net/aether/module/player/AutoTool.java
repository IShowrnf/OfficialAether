package net.aether.module.player;

import net.aether.module.Category;
import net.aether.module.Module;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class AutoTool extends Module
{
    public AutoTool()
    {
        super("AutoTool", "Swaps to the best tool for the block you mine.", Category.PLAYER);
    }

    public void onTick()
    {
        if (!this.inGame() || mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK)
        {
            return;
        }

        if (!mc.gameSettings.keyBindAttack.isKeyDown())
        {
            return;
        }

        BlockPos pos = mc.objectMouseOver.getBlockPos();
        IBlockState state = this.world().getBlockState(pos);
        int bestSlot = -1;
        float bestSpeed = 1.0F;

        for (int i = 0; i < 9; ++i)
        {
            ItemStack stack = this.player().inventory.getStackInSlot(i);

            if (stack.isEmpty())
            {
                continue;
            }

            float speed = stack.getStrVsBlock(state);

            if (speed > bestSpeed)
            {
                bestSpeed = speed;
                bestSlot = i;
            }
        }

        if (bestSlot != -1)
        {
            this.player().inventory.currentItem = bestSlot;
        }
    }
}
