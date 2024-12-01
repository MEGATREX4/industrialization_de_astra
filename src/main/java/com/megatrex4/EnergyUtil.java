package com.megatrex4;

import net.minecraft.world.item.ItemStack;

public class EnergyUtil {

    /**
     * Checks if the given ItemStack has enough energy.
     *
     * @param stack  The ItemStack to check.
     * @param amount The amount of energy required.
     * @return True if the stack has enough energy, false otherwise.
     */
    public static boolean hasEnoughEnergy(ItemStack stack, int amount) {
        if (stack.hasTag() && stack.getTag().contains("energy")) {
            int currentEnergy = stack.getTag().getInt("energy");
            return currentEnergy >= amount;
        }
        return false;
    }

    /**
     * Consumes energy from the given ItemStack.
     *
     * @param stack  The ItemStack to modify.
     * @param amount The amount of energy to consume.
     */
    public static void consumeEnergy(ItemStack stack, int amount) {
        if (stack.hasTag() && stack.getTag().contains("energy")) {
            int currentEnergy = stack.getTag().getInt("energy");
            stack.getTag().putInt("energy", Math.max(0, currentEnergy - amount));
        }
    }

    /**
     * Checks if the ItemStack is in the list of energy items.
     *
     * @param stack The ItemStack to check.
     * @return True if the item is in the energy items list, false otherwise.
     */
    public static boolean isEnergyItem(ItemStack stack) {
        String itemId = stack.getItem().builtInRegistryHolder().key().location().toString();
        return EnergyItems.ENERGY_ITEMS.contains(itemId);
    }

    /**
     * Retrieves the current energy level of the ItemStack.
     *
     * @param stack The ItemStack to check.
     * @return The current energy level, or 0 if none.
     */
    public static int getEnergy(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("energy")) {
            return stack.getTag().getInt("energy");
        }
        return 0;
    }

}
