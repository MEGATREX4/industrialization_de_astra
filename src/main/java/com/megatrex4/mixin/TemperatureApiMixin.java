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
        LOGGER.info("[MDA] TemperatureApiMixin successfully loaded!");
    }

    @Inject(method = "entityTick", at = @At("HEAD"), cancellable = true)
    private void customTemperatureTick(ServerLevel level, LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof Player player) {
            if (!ArmorHelper.isArmorComplete(player)) {
                return;
            }

            if (ArmorHelper.hasPassiveArmor(player)) {
                ci.cancel();
                return;
            }

            // Apply protection logic
            boolean isProtected = ArmorHelper.isProtected(player);

            if (isProtected) {
                ItemStack chestItem = player.getItemBySlot(EquipmentSlot.CHEST);
                if (chestItem.getItem() instanceof SpaceSuitItem spaceSuit) {
                    if (SpaceSuitItem.hasOxygen(player)) {
                        ci.cancel();
                        return;
                    }
                }

                if (EnergyItemManager.hasSufficientEnergy(player)) {
                    EnergyItemManager.consumeEnergy(player);
                    ci.cancel();
                    return;
                }
            }
        }
    }
}
