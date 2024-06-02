package com.nubqol.mixin.client.easyElytraLaunch;

import com.nubqol.manager.EELManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Shadow
    @Final
    public ClientPlayNetworkHandler networkHandler;
    @Shadow
    @Final
    protected MinecraftClient client;

    @Unique
    private ClientPlayerEntity me() {
        return (ClientPlayerEntity) (Object) this;
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci) {
        EELManager.QueueItem currentQueue = EELManager.QUEUE.poll();

        if (currentQueue == null) return;
        switch (currentQueue) {
            case JUMP -> {
                me().jump();
            }
            case START_FALL_FLYING -> {
                networkHandler.sendPacket(new ClientCommandC2SPacket(me(), ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            }
            case USE_ROCKET -> {
                if (client.interactionManager != null) {
                    ItemStack mainHandStack = me().getMainHandStack();
                    ItemStack offHandStack = me().getOffHandStack();

                    if (mainHandStack.getItem() == Items.FIREWORK_ROCKET) {
                        client.interactionManager.interactItem(me(), Hand.MAIN_HAND);
                    } else if (offHandStack.getItem() == Items.FIREWORK_ROCKET) {
                        client.interactionManager.interactItem(me(), Hand.OFF_HAND);
                    }
                }
            }
        }
    }
}
