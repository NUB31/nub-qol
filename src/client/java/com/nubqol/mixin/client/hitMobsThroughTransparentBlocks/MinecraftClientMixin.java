package com.nubqol.mixin.client.hitMobsThroughTransparentBlocks;

import com.nubqol.NubQolClient;
import com.nubqol.utils.BlockUtils;
import com.nubqol.utils.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(MinecraftClient.class)
abstract class MinecraftClientMixin {
    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Shadow
    @Nullable
    public ClientWorld world;

    @Shadow
    @Nullable
    public HitResult crosshairTarget;

    @Shadow
    @Nullable
    public ClientPlayerInteractionManager interactionManager;

    // Runs once when attack key is pressed
    @Inject(at = @At("HEAD"), method = "doAttack", cancellable = true)
    private void doAttack(CallbackInfoReturnable<Boolean> cir) {
        if (interactionManager == null) return;

        isLookingAtEntityThroughNonSolidBlock(entity -> {
            interactionManager.attackEntity(player, entity);
            if (player != null) {
                player.swingHand(Hand.MAIN_HAND);
            }
            cir.setReturnValue(true);
            cir.cancel();
        });
    }

    // Runs every tick
    @Inject(at = @At("HEAD"), method = "handleBlockBreaking", cancellable = true)
    private void handleBlockBreaking(boolean breaking, CallbackInfo ci) {
        isLookingAtEntityThroughNonSolidBlock(entity -> ci.cancel());
    }

    @Unique
    private void isLookingAtEntityThroughNonSolidBlock(Consumer<Entity> cb) {
        if (!NubQolClient.CONFIG.hitMobsThroughTransparentBlocksEnabled.get()) return;

        if (crosshairTarget != null && player != null && world != null) {
            if (!BlockUtils.hasCollision(world, crosshairTarget)) {
                PlayerUtils
                        .findMobInPlayerCrosshair(player, world)
                        .ifPresent(cb);
            }
        }
    }
}
