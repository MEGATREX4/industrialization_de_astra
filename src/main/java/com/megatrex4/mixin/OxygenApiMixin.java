package com.megatrex4.mixin;

import com.megatrex4.ArmorHelper;
import com.megatrex4.EnergyItemManager;
import earth.terrarium.adastra.common.config.AdAstraConfig;
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

    private long lastOxygenConsumeTime = 0;

    @Inject(method = "entityTick", at = @At("HEAD"), cancellable = true)
    private void customEntityTick(ServerLevel level, LivingEntity entity, CallbackInfo ci) {
        if (!AdAstraConfig.disableOxygen) {
            if (entity instanceof Player player && PlanetHandler.hasOxygen(level, player.blockPosition())) {
                ci.cancel();
                return;
            }

            if (entity instanceof Player player) {

                ItemStack chestItem = player.getItemBySlot(EquipmentSlot.CHEST);

                if (!ArmorHelper.isArmorComplete(player)) {
                    return;
                }

            if (ArmorHelper.hasPassiveArmor(player)) {
                ci.cancel();
                return;
            }

            if (ArmorHelper.isProtected(player)) {
                if (chestItem.getItem() instanceof SpaceSuitItem spaceSuit) {
                    if (EnergyItemManager.hasSufficientEnergy(player) && !SpaceSuitItem.hasOxygen(player)) {
                        EnergyItemManager.consumeEnergy(player);
                        ci.cancel();
                        return;
                    }
                }
            }

                if (chestItem.getItem() instanceof SpaceSuitItem spaceSuit) {
                    long currentTime = System.currentTimeMillis();

                    if (currentTime - lastOxygenConsumeTime >= 2000) {
                        if (SpaceSuitItem.getOxygenAmount(player) <= 0) {
                            entity.hurt(ModDamageSources.create(level, ModDamageSources.OXYGEN), 2.0F);
                        } else {
                            spaceSuit.consumeOxygen(chestItem, 1);
                            lastOxygenConsumeTime = currentTime;
                        }
                    }
                }


                ci.cancel();
            }
        }
    }

}
