package com.nubqol.utils;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.Objects;

public class PlayerUtils {
	private final PlayerEntity player;

	public PlayerUtils(PlayerEntity player) {
		this.player = player;
	}

	public boolean isHoldingItem(Item item) {
		return (player.getStackInHand(Hand.MAIN_HAND).getItem() == item || player.getStackInHand(Hand.OFF_HAND).getItem() == item);
	}

	public Entity findMobInPlayerCrosshair(ClientWorld world, ClientPlayerEntity player) {
		double playerReachDistance = player.getEntityInteractionRange();

		Vec3d camera = player.getCameraPosVec(1.0F);
		Vec3d rotation = player.getRotationVec(1.0F);

		HitResult hitResult = world.raycast(
				new RaycastContext(
						camera,
						camera.add(
								rotation.x * playerReachDistance,
								rotation.y * playerReachDistance,
								rotation.z * playerReachDistance
						),
						RaycastContext.ShapeType.COLLIDER,
						RaycastContext.FluidHandling.NONE,
						player
				)
		);

		Vec3d end = hitResult.getType() != HitResult.Type.MISS
				? hitResult.getPos()
				: camera.add(
				rotation.x * playerReachDistance,
				rotation.y * playerReachDistance,
				rotation.z * playerReachDistance
		);

		EntityHitResult result = ProjectileUtil.getEntityCollision(
				world,
				player,
				camera,
				end,
				new Box(camera, end),
				// Don't attack spectators, non-hittable entities and pets of the player
				entity -> !entity.isSpectator() && entity.canHit() && !(entity instanceof Tameable tameable && Objects.equals(tameable.getOwner(), player))
		);

		if (result != null) {
			return result.getEntity();
		} else {
			return null;
		}
	}
}
