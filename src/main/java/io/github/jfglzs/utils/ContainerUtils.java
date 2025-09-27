package io.github.jfglzs.utils;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class ContainerUtils
{
    public static int getInventorySlotAmount(ItemStack itemStack)
    {
        if (itemStack.getItem() instanceof BlockItem)
        {
            Block block = ((BlockItem)itemStack.getItem()).getBlock();
            if (block instanceof ShulkerBoxBlock || block instanceof ChestBlock || block instanceof BarrelBlock)
            {
                return 27;
            }
            else if (block instanceof AbstractFurnaceBlock)
            {
                return 3;
            }
            else if (block instanceof DispenserBlock)
            {
                return 9;
            }
            else if (block instanceof HopperBlock || block instanceof BrewingStandBlock)
            {
                return 5;
            }
        }
        return -1;
    }
}
