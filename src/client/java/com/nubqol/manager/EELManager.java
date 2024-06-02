package com.nubqol.manager;

import com.nubqol.NubQolClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.LinkedList;
import java.util.Queue;

public class EELManager {
    public static Queue<QueueItem> QUEUE = new LinkedList<>();

    public static void tryEnqueueEEL(ClientPlayerEntity player) {
        if (canUseEEL(player)) {
            if (player.isOnGround()) {
                QUEUE.add(QueueItem.JUMP);
            }

            QUEUE.add(QueueItem.START_FALL_FLYING);
            QUEUE.add(QueueItem.NOOP);
            QUEUE.add(QueueItem.USE_ROCKET);
        }
    }

    public static boolean canUseEEL(ClientPlayerEntity player) {
        ItemStack chestPiece = player.getEquippedStack(EquipmentSlot.CHEST);

        return !player.isFallFlying()
                && !player.isTouchingWater()
                && !player.hasStatusEffect(StatusEffects.LEVITATION)
                && QUEUE.isEmpty()
                && chestPiece.isOf(Items.ELYTRA)
                && ElytraItem.isUsable(chestPiece)
                && NubQolClient.CONFIG.easyElytraLaunchEnabled.get();
    }

    public enum QueueItem {
        JUMP,
        START_FALL_FLYING,
        NOOP,
        USE_ROCKET
    }
}
