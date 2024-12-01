package com.megatrex4;

import earth.terrarium.adastra.common.items.armor.SpaceSuitItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ArmorHelper {
    private static final List<String> ENERGY_ITEMS = Arrays.asList(
            "techreborn:quantum_boots",
            "techreborn:quantum_helmet",
            "techreborn:quantum_leggings",
            "techreborn:quantum_chestplate",
            "techreborn:nano_helmet",
            "techreborn:nano_chestplate",
            "techreborn:nano_leggings",
            "techreborn:nano_boots",
            "modern_industrialization:gravichestplate"
    );

    public static boolean isArmorComplete(Player player) {
        for (ItemStack stack : player.getArmorSlots()) {
            if (stack.isEmpty()) {
                return false; // At least one slot is empty
            }
        }
        return true; // All slots are filled
    }

    public static boolean isProtected(Player player) {
        return hasSpaceSuitWithOxygen(player) || hasEnergySetWithEnergy(player);
    }

    private static boolean hasSpaceSuitWithOxygen(Player player) {
        for (ItemStack stack : player.getArmorSlots()) {
            if (stack.getItem() instanceof SpaceSuitItem && SpaceSuitItem.hasOxygen(player)) {
                return true; // SpaceSuit has oxygen
            }
        }
        return false;
    }

    private static boolean hasEnergySetWithEnergy(Player player) {
        for (ItemStack stack : player.getArmorSlots()) {
            String itemId = stack.getItem().builtInRegistryHolder().key().location().toString();
            if (ENERGY_ITEMS.contains(itemId) && EnergyUtil.hasEnoughEnergy(stack, EnergyItemManager.REQUIRED_ENERGY)) {
                return true; // Energy item has sufficient energy
            }
        }
        return false;
    }
    public static boolean isWearingFullSpaceSuit(Player player) {
        for (ItemStack stack : player.getArmorSlots()) {
            if (!(stack.getItem() instanceof SpaceSuitItem)) {
                return false; // At least one slot does not have a SpaceSuit item
            }
        }
        return true; // All armor slots have SpaceSuit items
    }
}
