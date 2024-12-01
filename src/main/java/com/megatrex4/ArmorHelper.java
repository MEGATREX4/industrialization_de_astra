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
            "modern_industrialization:gravichestplate",
            "modern_industrialization:quantum_leggings",
            "modern_industrialization:quantum_helmet",
            "modern_industrialization:quantum_boots"
    );

    private static final List<String> PASSIVE_ARMOR_ITEMS = Arrays.asList(
            "modern_industrialization:quantum_chestplate"

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
        return hasPassiveArmor(player) || hasSpaceSuitWithOxygen(player) || hasEnergySetWithEnergy(player);
    }

    public static boolean hasPassiveArmor(Player player) {
        for (ItemStack stack : player.getArmorSlots()) {
            String itemId = stack.getItem().builtInRegistryHolder().key().location().toString();
            if (PASSIVE_ARMOR_ITEMS.contains(itemId)) {
                return true; // Passive armor detected
            }
        }
        return false;
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
}
