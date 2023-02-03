package net.fabricmc.smphack.mixin;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

    @Mixin(WorldRenderer.class)
    public class FullBrightMixin {

        private static final KeyBinding FullbrightKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "FullBright toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                "Imperials"
        ));

        @Inject(at = @At("RETURN"), method = "getLightmapCoordinates(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I")
        private static int overrideLightmapCoordinates(BlockRenderView world, BlockState state, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
            //return 15728880;
            return cir.getReturnValueI();
        }

    }


