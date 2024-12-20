package com.nubqol.mixin.client.easyElytraLaunch;

import com.nubqol.NubQolClient;
import com.nubqol.state.EELStateMachine;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireworkRocketItem.class)
abstract class FireworkRocketItemMixin {
    @Inject(at = @At("HEAD"), method = "use")
    private void useItem(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (world.isClient && NubQolClient.CONFIG.EELEnabled.get()) {
            EELStateMachine.getInstance().launch();
        }
    }
}
