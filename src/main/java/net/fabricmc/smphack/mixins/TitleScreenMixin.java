package net.fabricmc.smphack.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import com.terraformersmc.modmenu.gui.ModsScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.smphack.GeneralConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(TitleScreen.class)
@Environment(EnvType.CLIENT)
public class TitleScreenMixin extends Screen {

    private static final Identifier SPACE_BACKGROUND = new Identifier("smphack", "textures/gui/space.png");
    private static boolean CustomScreen;
    MinecraftClient mc = MinecraftClient.getInstance();
    @Shadow
    @Final
    private LogoDrawer logoDrawer;
    private long backgroundFadeStart;

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "render")
    private void render(DrawContext drawContext, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        CustomScreen = GeneralConfig.getConfig().isCustomBG();
        if (CustomScreen) {
            // Draw the custom background image
            if (this.backgroundFadeStart == 0L) {
                this.backgroundFadeStart = Util.getMeasuringTimeMs();
            }
            mc.getTextureManager().bindTexture(SPACE_BACKGROUND);
            RenderSystem.setShaderTexture(0, SPACE_BACKGROUND);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float) MathHelper.ceil(MathHelper.clamp((float) (Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0F, 0.0F, 1.0F)));
            drawContext.drawTexture(SPACE_BACKGROUND,0, 0, 0, 0, MinecraftClient.getInstance().getWindow().getScaledWidth(), MinecraftClient.getInstance().getWindow().getScaledHeight(), MinecraftClient.getInstance().getWindow().getScaledWidth(), MinecraftClient.getInstance().getWindow().getScaledHeight());
            this.logoDrawer.draw(drawContext, mc.getWindow().getScaledWidth(), 255);
        }
    }

    @Inject(at = @At("TAIL"), method = "render")
    public void renderButtons(DrawContext drawContext, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (CustomScreen) {
            // Remove the default buttons
            List<Element> toRemove = new ArrayList<>();
            for (Element element : this.children()) {
                if (element instanceof ButtonWidget) {
                    toRemove.add(element);
                }
            }
            for (Element element : toRemove) {
                this.remove(element);
            }

            // Add the custom text buttons
            int y = (this.height / 4) + 48;
            int buttonWidth = 98;
            int buttonHeight = 20;
            int spacing = 4;

            this.addDrawableChild(new PressableTextWidget((this.width / 2) - 33, y, buttonWidth, buttonHeight, Text.translatable("menu.singleplayer"), (button) -> {
                mc.setScreen(new SelectWorldScreen(this));
            }, this.textRenderer));

            y += buttonHeight + spacing;

            this.addDrawableChild(new PressableTextWidget((this.width / 2) - 30, y, buttonWidth, buttonHeight, Text.translatable("menu.multiplayer"), (button) -> {
                mc.setScreen(new MultiplayerScreen(this));
            }, this.textRenderer));

            y += buttonHeight + spacing;

            this.addDrawableChild(new PressableTextWidget((this.width / 2) - 25, y, buttonWidth, buttonHeight, Text.translatable("menu.options"), (button) -> {
                mc.setScreen(new OptionsScreen(this, mc.options));
            }, this.textRenderer));

            y += buttonHeight + spacing;

            this.addDrawableChild(new PressableTextWidget((this.width/2) - 20, y, buttonWidth, buttonHeight, Text.translatable("Mods"), (button) -> {
                mc.setScreen(new ModsScreen(this));
            }, this.textRenderer));

            y += buttonHeight + spacing;

            this.addDrawableChild(new PressableTextWidget((this.width / 2) - 29, y, buttonWidth, buttonHeight, Text.translatable("menu.quit"), (button) -> {
                mc.stop();
            }, this.textRenderer));
        }
    }

}


