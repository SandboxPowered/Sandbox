package com.hrznstudio.sandbox.block;

import com.eclipsesource.v8.V8Object;
import com.hrznstudio.sandbox.server.SandboxServer;
import com.hrznstudio.sandbox.util.V8Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class JavascriptSlabBlock extends SlabBlock {
    private V8Object object;

    public JavascriptSlabBlock(V8Object object) {
        super(Settings.of(Material.METAL));
        this.object = object;
    }

    @Override
    public void onBreak(World world_1, BlockPos pos, BlockState blockState_1, PlayerEntity playerEntity_1) {
        super.onBreak(world_1, pos, blockState_1, playerEntity_1);

        if (!world_1.isClient) {
            if (object.contains("onBreak")) {
                V8Util.push(SandboxServer.INSTANCE.getEngine());
                object.executeVoidFunction("onBreak", V8Util.createV8Array(
                        V8Util.createV8Pos(pos),
                        V8Util.createV8Player(playerEntity_1)
                ));
                V8Util.pop();
            }
        }
    }

    @Override
    public void onPlaced(World world_1, BlockPos pos, BlockState blockState_1, @Nullable LivingEntity livingEntity_1, ItemStack itemStack_1) {
        super.onPlaced(world_1, pos, blockState_1, livingEntity_1, itemStack_1);

        if (!world_1.isClient) {
            if (object.contains("onPlace")) {
                V8Util.push(SandboxServer.INSTANCE.getEngine());
                object.executeVoidFunction("onPlace", V8Util.createV8Array(
                        V8Util.createV8Pos(pos),
                        livingEntity_1 instanceof PlayerEntity ? V8Util.createV8Player((PlayerEntity) livingEntity_1) : null,
                        V8Util.createV8ItemStack(itemStack_1)
                ));
                V8Util.pop();
            }
        }
    }
}