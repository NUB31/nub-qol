package com.nubqol.mixin.client.easyElytraLaunch;

import com.nubqol.NubQolClient;
import com.nubqol.state.EELStateMachine;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireworkRocketItem.class)
abstract class FireworkRocketItemMixin {
    @Inject(at = @At("HEAD"), method = "use")
    private void useItem(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (world.isClient && NubQolClient.CONFIG.EELEnabled.get()) {
            EELStateMachine.getInstance().ifPresent(EELStateMachine::launch);
        }
    }
}
