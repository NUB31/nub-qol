package com.nubqol.mixin.client.ignoreStemsWithAxe;

import com.nubqol.NubQol;
import com.nubqol.NubQolClient;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.StemBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.AxeItem;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
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

    // Runs once when attack key is pressed
    @Inject(at = @At("HEAD"), method = "doAttack", cancellable = true)
    private void doAttack(CallbackInfoReturnable<Boolean> cir) {
        isTargetingStemWithAxe(() -> {
            if (player != null) {
                player.sendMessage(Text.literal(String.format("%s: The `%s` option is enabled, preventing you from destroying stems with an axe", NubQol.MOD_ID, Text.translatable("nub-qol.config.pswa_enabled.title").getString())));
            }
            cir.setReturnValue(true);
            cir.cancel();
        });
    }

    // Runs every tick
    @Inject(at = @At("HEAD"), method = "handleBlockBreaking", cancellable = true)
    private void handleBlockBreaking(boolean breaking, CallbackInfo ci) {
        isTargetingStemWithAxe(ci::cancel);
    }

    @Unique
    private void isTargetingStemWithAxe(Runnable cb) {
        if (!NubQolClient.CONFIG.ignoreStemsWithAxe.get()) return;

        if (world != null && player != null) {
            if (crosshairTarget instanceof BlockHitResult blockHitResult) {
                BlockPos blockPos = blockHitResult.getBlockPos();
                BlockState blockState = world.getBlockState(blockPos);

                if (blockState.getBlock() instanceof StemBlock || blockState.getBlock() instanceof AttachedStemBlock) {
                    if (player.getMainHandStack().getItem() instanceof AxeItem) {
                        cb.run();
                    }
                }
            }
        }
    }
}
