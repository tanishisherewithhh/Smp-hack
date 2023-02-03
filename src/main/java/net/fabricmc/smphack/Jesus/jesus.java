package net.fabricmc.smphack.Jesus;

import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import java.util.ArrayList;
import java.util.stream.Collectors;
import net.minecraft.block.Material;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;

public class jesus extends MainGui {
    private int tickTimer = 10;
    private int packetTimer = 0;
    MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void update()
    {
        // check if sneaking
        if(mc.options.sneakKey.isPressed())
            return;

        ClientPlayerEntity player = mc.player;
        // move up in liquid
        assert player != null;
        if(player.isTouchingWater() || player.isInLava())
        {
            Vec3d velocity = player.getVelocity();
            player.setVelocity(velocity.x, 0.11, velocity.z);
            tickTimer = 0;
            return;
        }

        // simulate jumping out of water
        Vec3d velocity = player.getVelocity();
        if(tickTimer == 0)
            player.setVelocity(velocity.x, 0.30, velocity.z);
        else if(tickTimer == 1)
            player.setVelocity(velocity.x, 0, velocity.z);

        // update timer
        tickTimer++;
    }
    public void onSentPacket(PacketOutputListener.PacketOutputEvent event) {
        // check packet type
        if (!(event.getPacket() instanceof PlayerMoveC2SPacket))
            return;

        PlayerMoveC2SPacket packet = (PlayerMoveC2SPacket) event.getPacket();

        // check if packet contains a position
        if (!(packet instanceof PlayerMoveC2SPacket.PositionAndOnGround
                || packet instanceof PlayerMoveC2SPacket.Full))
            return;

        // check inWater
        assert mc.player != null;
        if (mc.player.isTouchingWater())
            return;

        // check fall distance
        if (mc.player.fallDistance > 3F)
            return;

        // if not actually moving, cancel packet
        if (mc.player.input == null) {
            event.cancel();
            return;
        }

        // wait for timer
        packetTimer++;
        if (packetTimer < 4)
            return;

        // cancel old packet
        event.cancel();

        // get position
        double x = packet.getX(0);
        double y = packet.getY(0);
        double z = packet.getZ(0);

        // offset y
        if (mc.player.age % 2 == 0)
            y -= 0.05;
        else
            y += 0.05;

        // create new packet
        Packet<?> newPacket;
        if (packet instanceof PlayerMoveC2SPacket.PositionAndOnGround)
            newPacket =
                    new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, true);
        else
            newPacket = new PlayerMoveC2SPacket.Full(x, y, z, packet.getYaw(0),
                    packet.getPitch(0), true);

        // send new packet
        mc.player.networkHandler.getConnection().send(newPacket);

    }
    public boolean shouldBeSolid()
    {
        return mc.player != null && mc.player.fallDistance <= 3
                && !mc.options.sneakKey.isPressed() && !mc.player.isTouchingWater()
                && !mc.player.isInLava();
    }

}
