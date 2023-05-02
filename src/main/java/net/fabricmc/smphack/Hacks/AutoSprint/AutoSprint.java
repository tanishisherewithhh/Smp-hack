package net.fabricmc.smphack.Hacks.AutoSprint;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.MainGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public class AutoSprint extends MainGui {

    MinecraftClient mc = MinecraftClient.getInstance();
    boolean AutoSprint;


    @Override
    public void update() {
        AutoSprint= GeneralConfig.getConfig().getAutoSprint();
        assert mc.player != null;
        Entity e = mc.player.getRootVehicle();
        e.setSprinting(AutoSprint);
    }
}
