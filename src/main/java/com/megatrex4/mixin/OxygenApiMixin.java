package com.megatrex4.mixin;

import com.megatrex4.ArmorHelper;
import com.megatrex4.EnergyItemManager;
import earth.terrarium.adastra.common.handlers.PlanetHandler;
import earth.terrarium.adastra.common.items.armor.SpaceSuitItem;
import earth.terrarium.adastra.common.registry.ModDamageSources;
import earth.terrarium.adastra.common.systems.OxygenApiImpl;
import net.minecraft.core.BlockPos;
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
//        System.out.println("[MDA] OxygenApiMixin is being loaded!");
        LOGGER.info("[MDA] OxygenApiMixin successfully loaded!");
    }


    @Inject(method = "entityTick", at = @At("HEAD"), cancellable = true)
    private void customEntityTick(ServerLevel level, LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof Player player) {
            if (!ArmorHelper.isArmorComplete(player)) {
                return;
            }

            if (ArmorHelper.hasPassiveArmor(player)) {
                ci.cancel(); // Passive armor provides protection
                return;
            }

            // Check if the player is protected
            if (ArmorHelper.isProtected(player)) {

                ItemStack chestItem = player.getItemBySlot(EquipmentSlot.CHEST);
                if (chestItem.getItem() instanceof SpaceSuitItem spaceSuit) {

                    // Get the player's current position
                    BlockPos playerPos = player.blockPosition();

                    // Get the planet data for the player's position
                    PlanetHandler planetHandler = PlanetHandler.read(level);
                    boolean hasOxygen = PlanetHandler.hasOxygen(level, playerPos); // Check if the planet has oxygen at this position

                    // If the planet has oxygen, cancel the energy consumption logic
                    if (hasOxygen) {
                        ci.cancel(); // If the planet has oxygen, cancel the damage logic
                        return;
                    }

                    // Handle energy consumption if the planet lacks oxygen and no oxygen is supplied by the armor
                    if (EnergyItemManager.hasSufficientEnergy(player) && !SpaceSuitItem.hasOxygen(player)) {
                        EnergyItemManager.consumeEnergy(player); // Consume energy if enough is available and the armor doesn't supply oxygen
                        ci.cancel();
                        return;
                    }
                }
            }
            // In case of no oxygen and no energy, force player to suffocate or lose air supply
            player.setAirSupply(-80);
            ci.cancel();
        }
    }
}
