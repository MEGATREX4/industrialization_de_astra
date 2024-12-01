package com.megatrex4;

import earth.terrarium.adastra.api.systems.OxygenApi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class EnergyArmorChecker {

    private static final int ENERGY_CONSUMPTION_PER_TICK = 100; // Energy to consume per tick

    /**
     * Checks if the player can breathe using energy-based items.
     *
     * @param player The player to check.
     * @return True if the player can breathe, false otherwise.
     */
    public static boolean canBreathe(Player player) {
        for (ItemStack stack : player.getArmorSlots()) {
            if (stack.isEmpty()) continue;

            // Check if the item is in the energy items list
            if (EnergyUtil.isEnergyItem(stack)) {
                // Check and consume energy
                if (EnergyUtil.hasEnoughEnergy(stack, ENERGY_CONSUMPTION_PER_TICK)) {
                    EnergyUtil.consumeEnergy(stack, ENERGY_CONSUMPTION_PER_TICK);
                    return true; // Player can breathe
                }
            }
        }

        // Fallback to Oxygen API check
        return OxygenApi.API.hasOxygen(player);
    }
}
