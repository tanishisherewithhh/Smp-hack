package net.fabricmc.smphack.hud;

import com.tanishisherewith.dynamichud.widget.Widget;
import com.tanishisherewith.dynamichud.widget.WidgetBox;
import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

public class MuppetWidget extends Widget {
    MinecraftClient mc=MinecraftClient.getInstance();
    /**
     * Constructs a Widget object.
     *
     * @param client The Minecraft client instance
     */
    public MuppetWidget(MinecraftClient client,float xPercent, float yPercent,boolean enabled) {
        super(client);
        this.xPercent=xPercent;
        this.yPercent=yPercent;
        this.enabled=enabled;
    }

    @Override
    public WidgetBox getWidgetBox() {
        return new WidgetBox(this.getX() - 18, this.getY() - 55, this.getX() + 18, this.getY() + 12);
    }


    @Override
    public void render(DrawContext drawContext) {
        boolean showMuppet= GeneralConfig.getConfig().getShowMuppet();
        //SHOW MUPPET
        if (showMuppet) {
            ClientPlayerEntity player=MinecraftClient.getInstance().player;
            float yaw = MathHelper.wrapDegrees(player.prevYaw + (player.getYaw() - player.prevYaw) * mc.getTickDelta());
            float pitch = player.getPitch();
            InventoryScreen.drawEntity(drawContext,getX(),getY(), (int)25.0, -yaw, -pitch, player);
        }
    }
    @Override
    public void writeToTag(NbtCompound tag) {
        super.writeToTag(tag);
        tag.putString("class", getClass().getName());
        tag.putFloat("xPercent", xPercent);
        tag.putFloat("yPercent", yPercent);
        tag.putBoolean("Enabled", this.enabled);
    }

    @Override
    public void readFromTag(NbtCompound tag) {
        super.readFromTag(tag);
        xPercent = tag.getFloat("xPercent");
        yPercent = tag.getFloat("yPercent");
        enabled = tag.getBoolean("Enabled");
    }
}
