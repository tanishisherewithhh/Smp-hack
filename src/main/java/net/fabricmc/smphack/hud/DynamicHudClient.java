package net.fabricmc.smphack.hud;

import com.tanishisherewith.dynamichud.DynamicHUD;
import com.tanishisherewith.dynamichud.helpers.ColorHelper;
import com.tanishisherewith.dynamichud.helpers.TextureHelper;
import com.tanishisherewith.dynamichud.util.DynamicUtil;
import com.tanishisherewith.dynamichud.util.TextGenerator;
import com.tanishisherewith.dynamichud.util.WidgetLoading;
import com.tanishisherewith.dynamichud.widget.IWigdets;
import com.tanishisherewith.dynamichud.widget.Widget;
import com.tanishisherewith.dynamichud.widget.item.ItemWidget;
import com.tanishisherewith.dynamichud.widget.text.TextWidget;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.config.ConfigUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.world.biome.Biome;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tanishisherewith.dynamichud.DynamicHUD.WIDGETS_FILE;
import static net.fabricmc.loader.impl.util.StringUtil.capitalize;

public class DynamicHudClient implements ClientModInitializer, IWigdets, WidgetLoading {
    private static final int COLOR_GREEN = 0x00FF00;
    private static final int COLOR_YELLOW = 0xFFFF00;
    private static final int COLOR_RED = 0xFF0000;
    static MinecraftClient mc = MinecraftClient.getInstance();
    static int fps;
    static int ping;
    static String Biome;
    static double deltaX, deltaY, deltaZ, floatbps;
    static String bps;
    protected List<Widget> widgets = new ArrayList<>();
    protected boolean WidgetAdded = false;
    private DynamicUtil dynamicutil;
    private String ArmorDurabilityDisplay;
    private Color color = Color.RED;
    static ClientPlayerEntity player;


    public static int getDurabilityColor(float durability) {
        if (durability > 0.5) {
            return interpolateColors(COLOR_YELLOW, COLOR_GREEN, (durability - 0.5f) * 2);
        } else {
            return interpolateColors(COLOR_RED, COLOR_YELLOW, durability * 2);
        }
    }

    private static int interpolateColors(int color1, int color2, float factor) {
        int red = (int) (((color1 >> 16) & 0xFF) * (1 - factor) + ((color2 >> 16) & 0xFF) * factor);
        int green = (int) (((color1 >> 8) & 0xFF) * (1 - factor) + ((color2 >> 8) & 0xFF) * factor);
        int blue = (int) ((color1 & 0xFF) * (1 - factor) + (color2 & 0xFF) * factor);
        return (red << 16) | (green << 8) | blue;
    }

    private static void getVariableValues() {
        deltaX = player.getX() - player.prevX;
        deltaY = player.getY() - player.prevY;
        deltaZ = player.getZ() - player.prevZ;
        floatbps = (float) Math.abs(Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2)) * 20);
        DecimalFormat df = new DecimalFormat("#.#");
        bps = df.format(floatbps);
        ping = getValue();
        fps = mc.getCurrentFps();
        Biome = getBiome();
    }

    public static int getValue() {
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
            Optional<RegistryKey<Biome>> biome = mc.world.getBiome(player.getBlockPos()).getKey();

            if (biome.isPresent()) {
                String biomeName = Text.translatable("biome." + biome.get().getValue().getNamespace() + "." + biome.get().getValue().getPath()).getString();
                biomes = Text.translatable("text.biome", capitalize(biomeName)).getString();
                assert MinecraftClient.getInstance().world != null;
                assert MinecraftClient.getInstance().player != null;
            }
        }
        return biomes.trim();
    }

    @Override
    public void onInitializeClient() {
        dynamicutil = (DynamicHUD.getDynamicUtil()==null)?new DynamicUtil(mc):DynamicHUD.getDynamicUtil();
        widgets.clear();

        DynamicHUD.setAbstractScreen(new MoveableScreenExtension(Text.of("Editor Screen"), dynamicutil));
        DynamicHUD.setIWigdets(new DynamicHudClient());

        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            dynamicutil.render(matrices, tickDelta);
            if(mc.player!=null) {
                player=mc.player;
                getVariableValues();
            }
        });
    }

    @Override
    public void addWigdets(DynamicUtil dynamicUtil) {
        assert mc.player != null;
        boolean shadow= GeneralConfig.getConfig().getTextshadow();
        widgets.add(new ArmorWidgetExtension(mc, EquipmentSlot.HEAD, 0.1f, 0.9f, true, TextureHelper.Position.ABOVE, () -> getDurabilityForStack(mc.player.getEquippedStack(EquipmentSlot.HEAD)), () -> color));
        widgets.add(new ArmorWidgetExtension(mc, EquipmentSlot.CHEST, 0.15f, 0.9f, true, TextureHelper.Position.ABOVE, () -> getDurabilityForStack(mc.player.getEquippedStack(EquipmentSlot.CHEST)), () -> color));
        widgets.add(new ArmorWidgetExtension(mc, EquipmentSlot.LEGS, 0.2f, 0.9f, true, TextureHelper.Position.ABOVE, () -> getDurabilityForStack(mc.player.getEquippedStack(EquipmentSlot.LEGS)), () -> color));
        widgets.add(new ArmorWidgetExtension(mc, EquipmentSlot.FEET, 0.25f, 0.9f, true, TextureHelper.Position.ABOVE, () -> getDurabilityForStack(mc.player.getEquippedStack(EquipmentSlot.FEET)), () -> color));

        widgets.add(new TextWidget(mc,"FPS: ",()-> String.valueOf(fps),0.05f,0.7f,shadow,true,false,Color.WHITE.getRGB(),Color.WHITE.getRGB(),true));
        widgets.add(new TextWidget(mc,"BPS: ",()-> String.valueOf(bps),0.05f,0.75f,shadow,true,false,Color.WHITE.getRGB(),Color.WHITE.getRGB(),true));
        widgets.add(new TextWidget(mc,"Ping: ",()-> String.valueOf(ping),0.05f,0.8f,shadow,true,false,Color.WHITE.getRGB(),Color.WHITE.getRGB(),true));
        widgets.add(new TextWidget(mc,"",()-> String.valueOf(Biome),0.05f,0.85f,shadow,true,false,Color.WHITE.getRGB(),Color.WHITE.getRGB(),true));

        widgets.add(new ItemWidget(mc, Items.TOTEM_OF_UNDYING::getDefaultStack, 0.15f, 0.15f, true, TextureHelper.Position.ABOVE, () -> String.valueOf(mc.player.getInventory().count(Items.TOTEM_OF_UNDYING)), () -> Color.YELLOW));

        widgets.add(new MuppetWidget(mc, 0.9f, 0.8f, true));

        for (Widget wigdet : widgets) {
            dynamicUtil.getWidgetManager().addWidget(wigdet);
        }
        WidgetAdded = true;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private String getDurabilityForStack(ItemStack stack) {
        String durabilityText = null;
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            ArmorDurabilityDisplay = String.valueOf(ConfigUtil.config.ArmorDurability);
            double durabilityPercentage = ((double) stack.getMaxDamage() - (double) stack.getDamage()) / (double) stack.getMaxDamage();
            durabilityText = (int) (durabilityPercentage * 100.0D) + "%";
            setColor(ColorHelper.getColorFromInt(getDurabilityColor((float) durabilityPercentage)));
            switch (ArmorDurabilityDisplay) {
                case "Percent" -> {
                    //Percent Display
                    return (stack.getDamage() > 0) ? durabilityText : null;
                }
                case "Bar" -> {
                    // Bar display
                    return null;
                }
                case "Number" -> {
                    //Number display
                    return (stack.getDamage() > 0) ? stack.getMaxDamage() - stack.getDamage() + "/" + stack.getMaxDamage() : null;
                }
            }
        }
        return durabilityText;
    }

    @Override
    public void loadWigdets(DynamicUtil dynamicUtil) {
        dynamicUtil.getWidgetManager().setWidgetLoading(new DynamicHudClient());
        List<Widget> widgets = dynamicUtil.getWidgetManager().loadWigdets(WIDGETS_FILE);
        int armorIndex = 0;
        int textIndex = 0;
        assert mc.player != null;
        TextGenerator[] ArmorWidgetText = new TextGenerator[]{
                () -> getDurabilityForStack(mc.player.getEquippedStack(EquipmentSlot.HEAD)),
                () -> getDurabilityForStack(mc.player.getEquippedStack(EquipmentSlot.CHEST)),
                () -> getDurabilityForStack(mc.player.getEquippedStack(EquipmentSlot.LEGS)),
                () -> getDurabilityForStack(mc.player.getEquippedStack(EquipmentSlot.FEET))
        };
        TextGenerator[] TextWidgetText = new TextGenerator[]{
                ()-> String.valueOf(fps),
                ()-> String.valueOf(bps),
                ()-> String.valueOf(ping),
                ()-> String.valueOf(Biome)
        };


        for (Widget widget : widgets) {
            if (widget instanceof TextWidget textWidget)
            {
                if (textIndex < 4) {
                    TextGenerator textGenerator = TextWidgetText[textIndex++];
                    textWidget.setDataTextGenerator(textGenerator);
                }
                dynamicUtil.getWidgetManager().addWidget(textWidget);
            }
            if (widget instanceof ArmorWidgetExtension armorWidget) {
                if (armorIndex < 4) {
                    TextGenerator textGenerator = ArmorWidgetText[armorIndex++];
                    armorWidget.setTextGenerator(textGenerator);
                    armorWidget.setColor(() -> color);
                }
                dynamicUtil.getWidgetManager().addWidget(armorWidget);
            }
            if (widget instanceof ItemWidget itemWidget) {
                TextGenerator textGenerator = () -> String.valueOf(mc.player.getInventory().count(Items.TOTEM_OF_UNDYING));
                itemWidget.setTextGenerator(textGenerator);
                dynamicUtil.getWidgetManager().addWidget(itemWidget);
            }
            if (widget instanceof MuppetWidget muppet) {
                dynamicUtil.getWidgetManager().addWidget(muppet);
            }
        }
    }

    @Override
    public Widget loadWidgetsFromTag(String className, NbtCompound widgetTag) {

        if (className.equals(MuppetWidget.class.getName())) {
            MuppetWidget widget = new MuppetWidget(MinecraftClient.getInstance(), 0, 0, true);
            widget.readFromTag(widgetTag);
            return widget;
        }
        if (className.equals(ArmorWidgetExtension.class.getName())) {
            ArmorWidgetExtension widget = new ArmorWidgetExtension(mc, EquipmentSlot.FEET, 0, 0, true, TextureHelper.Position.ABOVE, () -> "", () -> color);
            widget.readFromTag(widgetTag);
            return widget;
        }
        return WidgetLoading.super.loadWidgetsFromTag(className, widgetTag);
    }

}
