package com.nubqol.mixin.client.easyElytraLaunch;

import com.nubqol.NubQolClient;
import com.nubqol.utils.BlockUtils;
import com.nubqol.utils.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.util.hit.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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

	// If using firework rockets and targeting a non-solid block, ignore the non-solid block
	@Redirect(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"))
	private HitResult.Type getType(HitResult instance) {
		if (!NubQolClient.CONFIG.easyElytraLaunchEnabled.get()) return instance.getType();

		if (this.world == null || this.player == null || this.crosshairTarget == null) {
			return instance.getType();
		}

		PlayerUtils playerUtils = new PlayerUtils(this.player);
		BlockUtils blockHelper = new BlockUtils(this.world);

		if (this.world.isClient && playerUtils.isHoldingItem(Items.FIREWORK_ROCKET) && blockHelper.isSolid(this.crosshairTarget)) {
			return HitResult.Type.MISS;
		} else {
			return instance.getType();
		}
	}
}
