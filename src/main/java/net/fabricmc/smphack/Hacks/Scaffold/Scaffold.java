package net.fabricmc.smphack.Hacks.Scaffold;

import net.fabricmc.smphack.MainGui;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EmptyBlockView;

import java.util.Arrays;
import java.util.Objects;

public class Scaffold extends MainGui {

    MinecraftClient MC = MinecraftClient.getInstance();


    public void setItemUseCooldown(int itemUseCooldown) {
        for (int i=1;i<=itemUseCooldown;i++){}
    }

    public static void rightClickBlock(PlayerEntity player, BlockPos pos, Direction side, Vec3d hitVec) {
        if (player instanceof ClientPlayerEntity) {
            ClientPlayerInteractionManager interactionManager = MinecraftClient.getInstance().interactionManager;
            if (interactionManager != null) {
                BlockHitResult hitResult = new BlockHitResult(hitVec, side, pos, false);
                interactionManager.interactBlock((ClientPlayerEntity) player, Hand.MAIN_HAND, hitResult);
            }
        }
    }


    @Override
    public void toggled() {
        enabled = !enabled;
    }

    @Override
    public void update() {
        assert MC.player != null;
        BlockPos belowPlayer = BlockPos.ofFloored(MC.player.getPos()).down();

        // check if block is already placed
        if (!BlockUtils.getState(belowPlayer).getMaterial().isReplaceable())
            return;

        // search blocks in hotbar
        int newSlot = -1;
        for (int i = 0; i < 9; i++) {
            // filter out non-block items
            ItemStack stack = MC.player.getInventory().getStack(i);
            if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem))
                continue;

            // filter out non-solid blocks
            Block block = Block.getBlockFromItem(stack.getItem());
            BlockState state = block.getDefaultState();
            if (!state.isFullCube(EmptyBlockView.INSTANCE, BlockPos.ORIGIN))
                continue;

            // filter out blocks that would fall
            if (block instanceof FallingBlock && FallingBlock
                    .canFallThrough(BlockUtils.getState(belowPlayer.down())))
                continue;

            newSlot = i;
            break;
        }

        // check if any blocks were found
        if (newSlot == -1)
            return;

        // set slot
        int oldSlot = MC.player.getInventory().selectedSlot;
        MC.player.getInventory().selectedSlot = newSlot;

        scaffoldTo(belowPlayer);

        // reset slot
        MC.player.getInventory().selectedSlot = oldSlot;
    }

    private void scaffoldTo(BlockPos belowPlayer) {
        // tries to place a block directly under the player
        if (placeBlock(belowPlayer))
            return;

        // if that doesn't work, tries to place a block next to the block that's
        // under the player
        Direction[] sides = Direction.values();
        for (Direction side : sides) {
            BlockPos neighbor = belowPlayer.offset(side);
            if (placeBlock(neighbor))
                return;
        }

        // if that doesn't work, tries to place a block next to a block that's
        // next to the block that's under the player
        for (Direction side : sides)
            for (Direction side2 : Arrays.copyOfRange(sides, side.ordinal(), 6)) {
                if (side.getOpposite().equals(side2))
                    continue;

                BlockPos neighbor = belowPlayer.offset(side).offset(side2);
                if (placeBlock(neighbor))
                    return;
            }
    }

    private boolean placeBlock(BlockPos pos) {
        Vec3d eyesPos = new Vec3d(MC.player.getX(),
                MC.player.getY() + MC.player.getEyeHeight(MC.player.getPose()),
                MC.player.getZ());

        for (Direction side : Direction.values()) {
            BlockPos neighbor = pos.offset(side);
            Direction side2 = side.getOpposite();

            // check if side is visible (facing away from player)
            if (eyesPos.squaredDistanceTo(Vec3d.ofCenter(pos)) >= eyesPos
                    .squaredDistanceTo(Vec3d.ofCenter(neighbor)))
                continue;

            // check if neighbor can be right clicked
            if (!BlockUtils.canBeClicked(neighbor))
                continue;

            Vec3d hitVec = Vec3d.ofCenter(neighbor)
                    .add(Vec3d.of(side2.getVector()).multiply(0.5));

            // check if hitVec is within range (4.25 blocks)
            if (eyesPos.squaredDistanceTo(hitVec) > 18.0625)
                continue;

            // place block
            sendRotationPacket(0, 90);
                rightClickBlock(MC.player,neighbor, side2, hitVec);
                MC.player.swingHand(Hand.MAIN_HAND);
                setItemUseCooldown(4);

            return true;
        }

        return false;
    }
    public static void sendRotationPacket(float yaw, float pitch) {
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.player != null;
        PlayerMoveC2SPacket packet = new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, client.player.isOnGround());
        Objects.requireNonNull(client.getNetworkHandler()).sendPacket(packet);
    }
}