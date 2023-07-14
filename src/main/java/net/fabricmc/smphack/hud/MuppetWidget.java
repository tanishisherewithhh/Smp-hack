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
    public MuppetWidget(MinecraftClient client,float xPercent, float yPercent,boolean enabled, String label) {
        super(client, label);
        this.xPercent=xPercent;
        this.yPercent=yPercent;
        this.enabled=enabled;
    }

    @Override
    public void setTextGeneratorFromLabel() {

    }

    @Override
    public WidgetBox getWidgetBox() {
        int boxWidth = 25; // Set the desired width of the widget box
        int boxHeight = 55; // Set the desired height of the widget box
        int x1 = this.getX() - boxWidth;
        int y1 = this.getY() - boxHeight;
        int x2 = this.getX() + boxWidth;
        int y2 = this.getY() + boxHeight/3;
        return new WidgetBox(x1, y1, x2, y2);
    }



    @Override
    public void render(DrawContext drawContext) {
        boolean showMuppet= GeneralConfig.getConfig().getShowMuppet();
        //SHOW MUPPET
        if (showMuppet) {
            ClientPlayerEntity player=MinecraftClient.getInstance().player;
            assert player != null;
            float yaw = MathHelper.wrapDegrees(player.prevYaw + (player.getYaw() - player.prevYaw) * mc.getTickDelta());
            float pitch = player.getPitch();
            int x = this.getX();
            int y = this.getY();
            InventoryScreen.drawEntity(drawContext,x,y, 25, -yaw, -pitch, player);
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
