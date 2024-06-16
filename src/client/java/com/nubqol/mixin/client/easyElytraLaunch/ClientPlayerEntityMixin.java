package com.nubqol.mixin.client.easyElytraLaunch;

import com.nubqol.state.EELStateMachine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
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

    @Inject(at = @At("HEAD"), method = "init")
    private void init(CallbackInfo ci) {
        EELStateMachine.createInstance(me(), networkHandler, client);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci) {
        EELStateMachine.getInstance().ifPresent(EELStateMachine::tick);
    }
}
