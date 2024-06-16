package com.nubqol.state;

import com.nubqol.NubQolClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EELStateMachine {
    @Nullable
    private static volatile EELStateMachine instance;
    private final ClientPlayerEntity player;
    private final ClientPlayNetworkHandler networkHandler;
    private final MinecraftClient minecraftClient;
    private boolean hasUsedRocket = false;
    private boolean hasJumped = false;
    private boolean hasStartedFlying = false;
    private boolean hasFailed = false;
    private State currentState = State.IDLE;

    private EELStateMachine(ClientPlayerEntity player, ClientPlayNetworkHandler networkHandler, MinecraftClient client) {
        this.player = player;
        this.networkHandler = networkHandler;
        this.minecraftClient = client;
        reset();
    }

    public static void createInstance(ClientPlayerEntity player, ClientPlayNetworkHandler networkHandler, MinecraftClient client) {
        if (instance == null) {
            synchronized (EELStateMachine.class) {
                if (instance == null) {
                    instance = new EELStateMachine(player, networkHandler, client);
                }
            }
        }
    }

    public static Optional<EELStateMachine> getInstance() {
        return Optional.ofNullable(instance);
    }

    private State getNextState() {
        if (hasFailed) {
            return State.FAILED;
        }

        if (currentState == State.IDLE || currentState == State.FAILED || currentState == State.DONE) {
            return State.IDLE;
        }

        if (currentState == State.INIT) {
            // The player is already flying
            // The client will handle this case
            if (player.isFallFlying()) {
                return State.DONE;
            }
        }

        if (player.isOnGround() && !player.isTouchingWater() && !hasJumped) {
            return State.JUMP;
        } else if (player.isOnGround() && hasJumped) {
            if (player.isSneaking()) {
                return fail("Not enough space to jump");
            } else {
                return fail("Not enough space to jump, try sneaking");
            }
        }

        if (!player.isTouchingWater() && !player.isFallFlying() && !hasStartedFlying) {
            return State.START_FLYING;
        } else if (!player.isFallFlying() && !hasStartedFlying) {
            if (player.isTouchingWater()) {
                return fail("Cannot initiate flight mode underwater");
            } else if (player.hasStatusEffect(StatusEffects.LEVITATION)) {
                return fail("Cannot initiate flight mode when under the levitation effect");
            } else {
                return fail("Fly attempt failed");
            }
        }

        if (player.isFallFlying() && !hasUsedRocket && player.isHolding(Items.FIREWORK_ROCKET)) {
            return State.USE_ROCKET;
        } else if (player.isFallFlying() && !hasUsedRocket && !player.isHolding(Items.FIREWORK_ROCKET)) {
            return fail("You are not holding a firework rocket");
        }

        if (player.isFallFlying() && hasUsedRocket) {
            return State.DONE;
        }

        return fail();
    }

    private void jump() {
        player.jump();
        hasJumped = true;
    }

    private State fail() {
        return fail(null);
    }

    private State fail(@Nullable String errorMessage) {
        hasFailed = true;
        if (errorMessage != null && NubQolClient.CONFIG.EELMessagesEnabled.get()) {
            player.sendMessage(Text.translatable("nub-qol.error_message", errorMessage).withColor(Colors.LIGHT_YELLOW));
        }
        return State.FAILED;
    }

    private void startFlying() {
        networkHandler.sendPacket(new ClientCommandC2SPacket(player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        hasStartedFlying = true;
    }

    private void useRocket() {
        if (minecraftClient.interactionManager != null) {
            Hand hand = player.getMainHandStack().isOf(Items.FIREWORK_ROCKET)
                    ? Hand.MAIN_HAND
                    : player.getOffHandStack().isOf(Items.FIREWORK_ROCKET)
                    ? Hand.OFF_HAND
                    : null;

            if (hand != null) {
                minecraftClient.interactionManager.interactItem(player, hand);
            }
        }

        hasUsedRocket = true;
    }

    private void reset() {
        hasUsedRocket = false;
        hasJumped = false;
        hasStartedFlying = false;
        hasFailed = false;
    }

    public void launch() {
        currentState = State.INIT;
    }

    public void tick() {
        currentState = getNextState();
        switch (currentState) {
            case JUMP -> jump();
            case START_FLYING -> startFlying();
            case USE_ROCKET -> useRocket();
            case FAILED, DONE -> reset();
        }
    }

    private enum State {
        INIT,
        IDLE,
        DONE,
        FAILED,
        JUMP,
        START_FLYING,
        USE_ROCKET,
    }
}
