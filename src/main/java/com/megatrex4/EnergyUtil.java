package com.megatrex4;

import net.minecraft.world.item.ItemStack;

public class EnergyUtil {

    public static boolean hasEnoughEnergy(ItemStack stack, int amount) {
        if (stack.hasTag() && stack.getTag().contains("energy")) {
            int currentEnergy = stack.getTag().getInt("energy");
            return currentEnergy >= amount;
        }
        return false;
    }

    public static void consumeEnergy(ItemStack stack, int amount) {
        if (stack.hasTag() && stack.getTag().contains("energy")) {
            int currentEnergy = stack.getTag().getInt("energy");
            stack.getTag().putInt("energy", Math.max(0, currentEnergy - amount));
        }
    }

    public static boolean isEnergyItem(ItemStack stack) {
        String itemId = stack.getItem().builtInRegistryHolder().key().location().toString();
        return EnergyItems.ENERGY_ITEMS.contains(itemId);
    }

    public static int getEnergy(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("energy")) {
            return stack.getTag().getInt("energy");
        }
        return 0;
    }

}
