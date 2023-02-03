package net.fabricmc.smphack.mixin;

import io.netty.buffer.Unpooled;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class) // IMA Remove this because it lets the server know you are using the autototem mod
public class ServerOptOut {
    @Inject(at = @At("TAIL"), method = "onGameJoin")
    private void sendInfoPackage(GameJoinS2CPacket packet, CallbackInfo ci) {
        ClientPlayNetworkHandler networkHandler = (ClientPlayNetworkHandler) ((Object) this);
        networkHandler.sendPacket(new CustomPayloadC2SPacket(Identifier.of("autototem", ""),
                new PacketByteBuf(Unpooled.buffer())
                        .writeString("Client uses the Autototem mod.")
        ));
    }
}
