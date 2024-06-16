package com.nubqol.state;

import com.nubqol.NubQol;
import com.nubqol.NubQolClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Hand;

public class EELStateMachine {
    private static volatile EELStateMachine instance;
    private final MinecraftClient client = MinecraftClient.getInstance();
    private State lastState = State.IDLE;
    private boolean hasUsedRocket = false;
    private boolean hasJumped = false;
    private boolean hasStartedFlying = false;
    private boolean hasFailed = false;
    private boolean shouldInit = false;
    private boolean inProgress = false;

    public static EELStateMachine getInstance() {
        if (instance == null) {
            synchronized (EELStateMachine.class) {
                if (instance == null) {
                    instance = new EELStateMachine();
                }
            }
        }
        return instance;
    }

    private State nextState() {
        return lastState = getNextState();
    }

    private State getNextState() {
        if (hasFailed) {
            return State.FAILED;
        }

        if (shouldInit) {
            return State.INIT;
        }

        if (lastState == State.IDLE || lastState == State.FAILED || lastState == State.DONE) {
            return State.IDLE;
        }

        if (client.player == null) {
            return fail("Player is null");
        }

        if (client.player.isTouchingWater()) {
            return fail("Cannot use easy elytra launch in water");
        }

        if (client.player.hasStatusEffect(StatusEffects.LEVITATION)) {
            return fail("Cannot use easy elytra launch when under levitation effect");
        }

        if (client.player.isOnGround() && !hasJumped) {
            return State.JUMP;
        } else if (client.player.isOnGround() && hasJumped) {
            if (!client.player.isSneaking()) {
                return fail("Unable to jump, try sneaking");
            } else {
                return fail("Unable to jump");
            }
        }

        if (!client.player.isFallFlying() && !hasStartedFlying) {
            return State.START_FLYING;
        } else if (!client.player.isFallFlying() && hasStartedFlying) {
            return fail("Flight attempt failed");
        }

        if (client.player.isFallFlying() && !hasUsedRocket && client.player.isHolding(Items.FIREWORK_ROCKET)) {
            return State.USE_ROCKET;
        } else if (client.player.isFallFlying() && !hasUsedRocket && !client.player.isHolding(Items.FIREWORK_ROCKET)) {
            return fail("You are not holding a firework rocket");
        }

        if (client.player.isFallFlying() && hasUsedRocket) {
            return State.DONE;
        }

        return fail("Something unexpected happened when using easy elytra launch");
    }

    private State fail(String errorMessage) {
        hasFailed = true;
        if (client.player != null && errorMessage != null && NubQolClient.CONFIG.EELMessagesEnabled.get()) {
            client.player.sendMessage(Text.translatable("nub-qol.error_message", errorMessage).withColor(Colors.LIGHT_YELLOW));
        }
        NubQol.LOGGER.warn(String.format("Easy elytra launch failed with message: %s", errorMessage));
        return State.IDLE;
    }

    private void jump() {
        if (client.player != null) {
            client.player.jump();
        }
        hasJumped = true;
    }

    private void startFlying() {
        if (client.player != null) {
            client.player.networkHandler.sendPacket(new ClientCommandC2SPacket(client.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            client.player.startFallFlying();
        }
        hasStartedFlying = true;
    }

    private void useRocket() {
        if (client.interactionManager != null && client.player != null) {
            Hand hand = client.player.getMainHandStack().isOf(Items.FIREWORK_ROCKET)
                    ? Hand.MAIN_HAND
                    : client.player.getOffHandStack().isOf(Items.FIREWORK_ROCKET)
                    ? Hand.OFF_HAND
                    : null;

            if (hand != null) {
                client.interactionManager.interactItem(client.player, hand);
            }
        }
        hasUsedRocket = true;
    }

    private void reset() {
        hasUsedRocket = false;
        hasJumped = false;
        hasStartedFlying = false;
        hasFailed = false;
        shouldInit = false;
        inProgress = false;
    }

    private void init() {
        shouldInit = false;
    }

    public void launch() {
        if (client.player == null) return;
        ItemStack itemStack = client.player.getEquippedStack(EquipmentSlot.CHEST);
        if (!inProgress && !client.player.isFallFlying() && itemStack.isOf(Items.ELYTRA) && ElytraItem.isUsable(itemStack)) {
            shouldInit = true;
            inProgress = true;
        }
    }

    public void tick() {
        switch (nextState()) {
            case INIT -> init();
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
