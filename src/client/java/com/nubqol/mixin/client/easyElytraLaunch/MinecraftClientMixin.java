package com.nubqol.mixin.client.easyElytraLaunch;

import com.nubqol.NubQolClient;
import com.nubqol.utils.BlockUtils;
import com.nubqol.utils.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
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

        if (world == null || player == null || crosshairTarget == null) {
            return instance.getType();
        }

        if (PlayerUtils.canUseEasyElytraLaunch(player) && !BlockUtils.hasCollision(world, crosshairTarget)) {
            return HitResult.Type.MISS;
        }

        return instance.getType();
    }
}
