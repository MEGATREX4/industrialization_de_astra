package com.megatrex4.mixin;

import com.megatrex4.ArmorHelper;
import com.megatrex4.EnergyItemManager;
import earth.terrarium.adastra.common.items.armor.SpaceSuitItem;
import earth.terrarium.adastra.common.systems.TemperatureApiImpl;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mixin(TemperatureApiImpl.class)
public class TemperatureApiMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("IndustrializationDeAstra");

    static {
//        System.out.println("[MDA] TemperatureApiMixin is being loaded!");
        LOGGER.info("[MDA] TemperatureApiMixin successfully loaded!");
    }

    @Inject(method = "entityTick", at = @At("HEAD"), cancellable = true)
    private void customTemperatureTick(ServerLevel level, LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof Player player) {
//            LOGGER.info("[MDA] Player detected: {}", player.getName().getString());

            // Check if the player has full armor coverage
            if (!ArmorHelper.isArmorComplete(player)) {
                return;
            }

            // Check if the player is protected by passive armor
            if (ArmorHelper.hasPassiveArmor(player)) {
                ci.cancel(); // Passive armor provides protection
                return;
            }

            // Apply protection logic
            boolean isProtected = ArmorHelper.isProtected(player);

            if (isProtected) {
//                LOGGER.info("[MDA] Player {} is fully protected from temperature effects.", player.getName().getString());

                // Handle oxygen consumption if applicable
                ItemStack chestItem = player.getItemBySlot(EquipmentSlot.CHEST);
                if (chestItem.getItem() instanceof SpaceSuitItem spaceSuit) {
                    if (SpaceSuitItem.hasOxygen(player)) {
//                        LOGGER.info("[MDA] Player {} has sufficient oxygen. Preventing temperature damage...", player.getName().getString());
//                        spaceSuit.consumeOxygen(chestItem, 1L);
                        ci.cancel(); // Cancel default temperature logic
                        return;
                    }
                }

                // Handle energy consumption if applicable
                if (EnergyItemManager.hasSufficientEnergy(player)) {
//                    LOGGER.info("[MDA] Player {} has sufficient energy. Preventing temperature damage...", player.getName().getString());
                    EnergyItemManager.consumeEnergy(player);
                    ci.cancel(); // Cancel default temperature logic
                    return;
                }
            }

            // Log potential damage if no protection
//            LOGGER.warn("[MDA] Player {} is not protected from temperature effects.", player.getName().getString());
        }
    }
}
