package net.fabricmc.smphack.hud;

import com.tanishisherewith.dynamichud.DynamicHudIntegration;
import com.tanishisherewith.dynamichud.screens.AbstractMoveableScreen;
import com.tanishisherewith.dynamichud.utils.DynamicValueRegistry;
import com.tanishisherewith.dynamichud.widget.Widget;
import com.tanishisherewith.dynamichud.widget.WidgetManager;
import com.tanishisherewith.dynamichud.widget.WidgetRenderer;
import com.tanishisherewith.dynamichud.widgets.TextWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import static net.fabricmc.loader.impl.util.StringUtil.capitalize;
import static net.fabricmc.smphack.Hacks.Fly.Modes.BoatFly.BoatFlyHack.player;

public class DynamicHUDIntegration implements DynamicHudIntegration {
    static MinecraftClient mc  = MinecraftClient.getInstance();
    TextWidget coordsWidget, fpsWidget, biomeWidget,pingWidget,speedWidget;
    MuppetWidget muppetWidget;
    WidgetRenderer renderer;

    public static int getPing() {
        assert mc.player != null;
        PlayerListEntry entry = mc.player.networkHandler.getPlayerListEntry(mc.player.getUuid());
        if (entry != null) {
            return entry.getLatency(); // This method updates slow
        }
        return 0;
    }

    public static String getBiome() {
        String biomes = "";
        if (mc.world != null) {
            Optional<RegistryKey<net.minecraft.world.biome.Biome>> biome = mc.world.getBiome(player.getBlockPos()).getKey();

            if (biome.isPresent()) {
                String biomeName = Text.translatable("biome." + biome.get().getValue().getNamespace() + "." + biome.get().getValue().getPath()).getString();
                biomes = Text.translatable("text.biome", capitalize(biomeName)).getString();
            }
        }
        return biomes.trim();
    }
    private static String getSpeed(){
        String bps = "0.0f";
        ClientPlayerEntity player = mc.player;
        if(player != null) {
            double deltaX, deltaY, deltaZ;
            deltaX = player.getX() - player.prevX;
            deltaY = player.getY() - player.prevY;
            deltaZ = player.getZ() - player.prevZ;
            float floatbps = (float) Math.abs(Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2)) * 20);
            DecimalFormat df = new DecimalFormat("#.#");
            bps = df.format(floatbps);
        }
        return bps;
    }

    @Override
    public void init() {
        DynamicValueRegistry.registerGlobal("Coordinates", () -> {
            int x = 0, y = 0, z = 0;
            if (mc.player != null) {
                x = (int) mc.player.getX();
                y = (int) mc.player.getY();
                z = (int) mc.player.getZ();
            }
            return "XYZ: " + x + " " + y + " " + z;
        });
        DynamicValueRegistry.registerGlobal("Biome", () ->"Biome: "+ getBiome());
        DynamicValueRegistry.registerGlobal("Ping", () ->"Ping: " + getPing());
        DynamicValueRegistry.registerGlobal("BPS", () -> "BPS: " + getSpeed());
        DynamicValueRegistry.registerGlobal("FPS", () -> "FPS: " + mc.getCurrentFps());

        coordsWidget  = new TextWidget.Builder()
                .setX(10)
                .setY(50)
                .setDRKey("Coordinates")
                .rainbow(true)
                .shadow(false)
                .setTextColor(Color.WHITE)
                .setModID("smphack")
                .shouldScale(true)
                .build();
        fpsWidget  = new TextWidget.Builder()
                .setX(10)
                .setY(55)
                .setDRKey("FPS")
                .rainbow(true)
                .shadow(false)
                .setTextColor(Color.WHITE)
                .setModID("smphack")
                .shouldScale(true)
                .build();
        biomeWidget  = new TextWidget.Builder()
                .setX(10)
                .setY(56)
                .setDRKey("Biome")
                .rainbow(true)
                .shadow(false)
                .setTextColor(Color.WHITE)
                .setModID("smphack")
                .shouldScale(true)
                .build();
        speedWidget  = new TextWidget.Builder()
                .setX(10)
                .setY(57)
                .setDRKey("BPS")
                .rainbow(true)
                .shadow(false)
                .setTextColor(Color.WHITE)
                .setModID("smphack")
                .shouldScale(true)
                .build();
        pingWidget  = new TextWidget.Builder()
                .setX(10)
                .setY(58)
                .setDRKey("Ping")
                .rainbow(true)
                .shadow(false)
                .setTextColor(Color.WHITE)
                .setModID("smphack")
                .shouldScale(true)
                .build();

        muppetWidget = new MuppetWidget("smphack");
        muppetWidget.shouldScale = true;
        muppetWidget.setPosition(50,50);
    }
    @Override
    public void addWidgets() {
        WidgetManager.addWidgets(
                coordsWidget,
                muppetWidget);
    }

    @Override
    public void registerCustomWidgets() {
        WidgetManager.registerCustomWidgets(MuppetWidget.DATA);
    }

    @Override
    public void initAfter() {
        List<Widget> widgets = WidgetManager.getWidgetsForMod("smphack");

        renderer = new WidgetRenderer(widgets);
        renderer.shouldRenderInGameHud(true);
        renderer.addScreen(TitleScreen.class);
    }

    @Override
    public AbstractMoveableScreen getMovableScreen() {
        return new AbstractMoveableScreen(Text.of("Editor Screen"),renderer) {
        };
    }

    @Override
    public WidgetRenderer getWidgetRenderer() {
        return renderer;
    }

}
