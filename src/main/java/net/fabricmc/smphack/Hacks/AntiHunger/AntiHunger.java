package net.fabricmc.smphack.Hacks.AntiHunger;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

import java.util.Objects;

public class AntiHunger extends MainGui {

    private MinecraftClient mc = MinecraftClient.getInstance();
    private int sprintTick = 0;
    private boolean isSprinting = false;
    private int walkTick = 0;
    private boolean isWalking = false;
    boolean AntiHunger;

    @Override
    public void toggled()
    {
        AntiHunger= GeneralConfig.getConfig().getAntiHunger();
        if (AntiHunger)
        {
            update();
        }

    }


    @Override
    public void update() {
        if (mc.player == null) return;

        // check if the player is sprinting
        if (mc.options.forwardKey.isPressed() && !mc.player.isSprinting() && mc.player.getHungerManager().getFoodLevel() > 6) {
            // send player start sprinting packet
            sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
            sprintTick = 0;
            isSprinting = true;
        } else if (mc.player.isSprinting()) {
            sprintTick++;
            if (sprintTick >= 10) {
                // slow down hunger every 10 ticks while sprinting
                mc.player.getHungerManager().addExhaustion(0.06f);
                sprintTick = 0;
            }
            if (!mc.options.forwardKey.isPressed() || mc.player.getHungerManager().getFoodLevel() <= 6) {
                // send player stop sprinting packet
                sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                isSprinting = false;
            }
        }

        // check if the player is walking
        if ((mc.options.forwardKey.isPressed() || mc.options.leftKey.isPressed() || mc.options.rightKey.isPressed() || mc.options.backKey.isPressed())
                && !mc.player.isSprinting() && mc.player.getHungerManager().getFoodLevel() > 6) {
            walkTick = 0;
            isWalking = true;
        } else if (isWalking) {
            walkTick++;
            if (walkTick >= 40) {
                // slow down hunger every 40 ticks while walking
                mc.player.getHungerManager().addExhaustion(0.06f);
                walkTick = 0;
            }
            if (!mc.options.forwardKey.isPressed() && !mc.options.leftKey.isPressed() && !mc.options.rightKey.isPressed() && !mc.options.backKey.isPressed()
                    || mc.player.getHungerManager().getFoodLevel() <= 6) {
                isWalking = false;
            }
        }
    }

    private void sendPacket(ClientCommandC2SPacket packet) {
        Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(packet);
    }
}

