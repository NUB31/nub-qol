package com.nubqol.mixin.client.hitMobsThroughTransparentBlocks;

import com.nubqol.NubQolClient;
import com.nubqol.utils.BlockUtils;
import com.nubqol.utils.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

	@Unique
	public boolean skipBlockBreaking = false;

	// Redirect attacks to mobs if a mob is present in a non-solid block
	@Inject(at = @At("HEAD"), method = "doAttack")
	private void doAttack(CallbackInfoReturnable<Boolean> cir) {
		if (!NubQolClient.CONFIG.hitMobsThroughTransparentBlocksEnabled.value()) return;

		skipBlockBreaking = false;

		if (this.world != null
				&& this.crosshairTarget != null
				&& this.interactionManager != null
				&& this.player != null
		) {
			BlockUtils blockHelper = new BlockUtils(this.world);
			PlayerUtils playerUtils = new PlayerUtils(this.player);

			if (this.world.isClient && blockHelper.isSolid(this.crosshairTarget)) {
				Entity mobInPlayerRange = playerUtils.findMobInPlayerCrosshair(this.world, this.interactionManager);

				if (mobInPlayerRange != null) {
					skipBlockBreaking = true;
					this.crosshairTarget = new EntityHitResult(mobInPlayerRange);
				}
			}
		}
	}

	// Make sure blocks are not broken if a block attack is redirected to an entity
	@Inject(at = @At("HEAD"), method = "handleBlockBreaking", cancellable = true)
	private void handleBlockBreaking(boolean breaking, CallbackInfo ci) {
		if (skipBlockBreaking) ci.cancel();
	}
}
