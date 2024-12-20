package com.megatrex4.mixin;

import com.megatrex4.ArmorHelper;
import com.megatrex4.EnergyItemManager;
import com.megatrex4.EnergyUtil;
import earth.terrarium.adastra.common.items.armor.SpaceSuitItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SpaceSuitItem.class)
public class SpaceSuitItemMixin {

    @Inject(method = "hasFullSet", at = @At("HEAD"), cancellable = true)
    private static void injectHasFullSet(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (hasEnergySetWithEnergy(entity) || hasPassiveArmor(entity)) {
            cir.setReturnValue(true); // If the entity has energy or passive armor, return true immediately.
        }
    }

    @Inject(method = "hasFullNetheriteSet", at = @At("HEAD"), cancellable = true)
    private static void injectHasFullNetheriteSet(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (hasEnergySetWithEnergy(entity) || hasPassiveArmor(entity)) {
            cir.setReturnValue(true); // If the entity has energy or passive armor, return true immediately.
        }
    }

    @Inject(method = "hasFullJetSuitSet", at = @At("HEAD"), cancellable = true)
    private static void injectHasFullJetSuitSet(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (hasEnergySetWithEnergy(entity) || hasPassiveArmor(entity)) {
            cir.setReturnValue(true);
        }
    }

    private static boolean hasEnergySetWithEnergy(LivingEntity entity) {
        List<String> energyItems = ArmorHelper.ENERGY_ITEMS;
        for (ItemStack stack : entity.getArmorSlots()) {
            String itemId = stack.getItem().builtInRegistryHolder().key().location().toString();
            if (energyItems.contains(itemId) && EnergyUtil.hasEnoughEnergy(stack, EnergyItemManager.REQUIRED_ENERGY)) {
                return true; // Energy item has sufficient energy
            }
        }
        return false;
    }

    private static boolean hasPassiveArmor(LivingEntity entity) {
        List<String> passiveArmorItems = ArmorHelper.PASSIVE_ARMOR_ITEMS;
        for (ItemStack stack : entity.getArmorSlots()) {
            String itemId = stack.getItem().builtInRegistryHolder().key().location().toString();
            if (passiveArmorItems.contains(itemId)) {
                return true;
            }
        }
        return false;
    }
}
