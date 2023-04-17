package net.fabricmc.smphack.Hacks.Nofall;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public final class NoFallHack {
    MinecraftClient mc = MinecraftClient.getInstance();
    public void cancelFallDamage(ClientPlayerEntity player) {
        assert mc.player != null;

        if (player.fallDistance <= (player.isFallFlying() ? 1 : 2))
            return;

        if (player.isFallFlying() && player.isSneaking()
                && !isFallingFastEnoughToCauseDamage(player))
            return;

        // Create a new player position packet with the same position as the player,
        // but with the onGround flag set to true to prevent fall damage.
        PlayerMoveC2SPacket packet = new PlayerMoveC2SPacket.PositionAndOnGround(
                player.getX(),
                player.getY(),
                player.getZ(),
                true
        );

        // Send the packet to the server to update the player's position.
        try {
            Thread.sleep(10); // introduce a delay of 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        player.networkHandler.sendPacket(packet);
    }
    private boolean isFallingFastEnoughToCauseDamage(ClientPlayerEntity player) {
        return player.getVelocity().y < -0.5;
    }
    }


