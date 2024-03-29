package com.nubqol.mixin.client.easyElytraLaunch;

import com.nubqol.NubQolClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
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
		if (world.isClient) {
			clientHandler((ClientWorld) world, (ClientPlayerEntity) player, hand, cir);
		}
	}

	@Unique
	private void clientHandler(ClientWorld world, ClientPlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		if (!NubQolClient.CONFIG.easyElytraLaunchEnabled.value()) return;

		if (player.isOnGround()) {
			player.jump();
		}

//		player.networkHandler.sendPacket(new ClientCommandC2SPacket(player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
	}
}
