package com.nubqol.utils;

import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockUtils {
    public static boolean hasCollision(World world, HitResult crosshairTarget) {
        if (crosshairTarget.getType() != HitResult.Type.BLOCK) return false;

        BlockHitResult blockHitResult = (BlockHitResult) crosshairTarget;
        BlockPos blockPos = blockHitResult.getBlockPos();

        return hasCollision(world, blockPos);
    }

    public static boolean hasCollision(World world, BlockPos blockPos) {
        return !world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty();
    }
}