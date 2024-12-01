package com.megatrex4;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class EnergyItemManager {
    public static final int REQUIRED_ENERGY = 100; // Energy consumed per tick

    public static boolean hasSufficientEnergy(Player player) {
        return getAvailableEnergy(player) >= REQUIRED_ENERGY;
    }

    public static int getAvailableEnergy(Player player) {
        int totalEnergy = 0;
        for (ItemStack stack : player.getArmorSlots()) {
            if (EnergyUtil.isEnergyItem(stack)) {
                totalEnergy += EnergyUtil.getEnergy(stack);
            }
        }
        return totalEnergy;
    }

    public static void consumeEnergy(Player player) {
        for (ItemStack stack : player.getArmorSlots()) {
            if (EnergyUtil.isEnergyItem(stack) && EnergyUtil.hasEnoughEnergy(stack, REQUIRED_ENERGY)) {
                EnergyUtil.consumeEnergy(stack, REQUIRED_ENERGY);
                return; // Consume from one item per tick
            }
        }
    }
}
