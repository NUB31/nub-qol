package com.nubqol.mixin.easyElytraLaunch;

import com.nubqol.NubQol;
import com.nubqol.config.ModConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireworkRocketItem.class)
abstract class FireworkRocketItemMixin {
	@Inject(at = @At("HEAD"), method = "use")
	private void useItem(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		if (!world.isClient) {
			serverHandler((ServerWorld) world, (ServerPlayerEntity) player, hand, cir);
		}
	}

	@Unique
	private void serverHandler(ServerWorld world, ServerPlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		ModConfig config = NubQol.CONFIG_SYNC.configs.get(player.getUuid());
		if (config == null || !config.easyElytraLaunchEnabled.value()) return;

		if (!player.isFallFlying()) {
			player.startFallFlying();
		}
	}
}

