package com.megatrex4.mixin;

import earth.terrarium.adastra.common.items.armor.SpaceSuitItem;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpaceSuitItem.class)
public class SpaceSuitItemMixin {

    @Inject(method = "hasFullSet", at = @At("HEAD"), cancellable = true)
    private static void injectHasFullSet(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (player != null) {
            // Custom logic: Check if the player is wearing the spacesuit bib
            boolean hasSpaceSuitBib = player.getInventory().getArmor(2).getItem() instanceof SpaceSuitItem;

            // If bib is detected, signal that the full set is complete
            if (hasSpaceSuitBib) {
                cir.setReturnValue(true); // Tell the mod the full set is complete
                return;
            }
        }

        // If no bib is found, let the original logic execute
    }
}

