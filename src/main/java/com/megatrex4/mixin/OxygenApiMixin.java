package com.megatrex4.mixin;

import com.megatrex4.ArmorHelper;
import com.megatrex4.EnergyItemManager;
import earth.terrarium.adastra.common.items.armor.SpaceSuitItem;
import earth.terrarium.adastra.common.registry.ModDamageSources;
import earth.terrarium.adastra.common.systems.OxygenApiImpl;
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

@Mixin(OxygenApiImpl.class)
public class OxygenApiMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("IndustrializationDeAstra");

    static {
        System.out.println("[MDA] OxygenApiMixin is being loaded!");
        LOGGER.info("[MDA] OxygenApiMixin successfully loaded!");
    }

    @Inject(method = "entityTick", at = @At("HEAD"), cancellable = true)
    private void customEntityTick(ServerLevel level, LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof Player player) {
            LOGGER.info("[MDA] Player detected: {}", player.getName().getString());

            // Ensure the player has full armor coverage
            if (!ArmorHelper.isArmorComplete(player)) {
                LOGGER.warn("[MDA] Player {} is missing armor pieces. No protection applied.", player.getName().getString());
                return;
            }

            // Check if the player is protected
            if (ArmorHelper.isProtected(player)) {
                LOGGER.info("[MDA] Player {} is fully protected.", player.getName().getString());

                // Handle oxygen consumption
                ItemStack chestItem = player.getItemBySlot(EquipmentSlot.CHEST);
                if (chestItem.getItem() instanceof SpaceSuitItem spaceSuit) {
                    if (SpaceSuitItem.hasOxygen(player)) {
                        LOGGER.info("[MDA] Player {} has sufficient oxygen. Preventing suffocation...", player.getName().getString());
                        spaceSuit.consumeOxygen(chestItem, 1L); // Use the instance method
                        ci.cancel();
                        return;
                    }
                }

                // Handle energy consumption
                if (EnergyItemManager.hasSufficientEnergy(player)) {
                    LOGGER.info("[MDA] Player {} has sufficient energy. Preventing suffocation...", player.getName().getString());
                    EnergyItemManager.consumeEnergy(player);
                    ci.cancel();
                    return;
                }
            }

            // Apply damage if not protected
            LOGGER.warn("[MDA] Player {} is not protected. Applying suffocation damage.", player.getName().getString());
            entity.hurt(ModDamageSources.create(level, ModDamageSources.OXYGEN), 2.0F);
            player.setAirSupply(-80);
            ci.cancel();
        }
    }
}
