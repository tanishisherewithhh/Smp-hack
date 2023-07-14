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
        if (mc.player!=null && !mc.player.getRootVehicle().isSprinting()) {
            Entity e = mc.player.getRootVehicle();
            boolean alreadySprinting=e.isSprinting();
            e.setSprinting(AutoSprint || alreadySprinting);
        }
    }
}
