package net.fabricmc.smphack.Hacks.Nofall;

import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;


public class Nofall extends MainGui {
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final NoFallHack noFall = new NoFallHack();


    @Override
    public void toggled()
    {
        enabled=!enabled;
    }

    @Override
    public void update()
    {
        ClientPlayerEntity player = mc.player;
        if (player == null) {
            return;
        }
        // Check if the player is about to take fall damage.
        if (player.fallDistance >= 3.0f) {
            // Cancel the fall damage.
            noFall.cancelFallDamage(player);

            // Do something else, like teleport the player to a safe location.
            BlockPos safePos = new BlockPos(player.getX(), player.getY() + 10, player.getZ());
            player.teleport(player.getX(), player.getY() + 10, player.getZ());
        }
    }

    }
