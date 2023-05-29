package net.fabricmc.smphack.Hacks.Fakeplayer;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

public class FakePlayerCommand {
    private static String fakeplayername="FakePlayer";
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            LiteralArgumentBuilder<FabricClientCommandSource> fakeplayer = ClientCommandManager.literal("fakeplayer")
                    .then(ClientCommandManager.literal("spawn")
                            .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                    .executes(context -> {
                                        assert MinecraftClient.getInstance().player != null;
                                        fakeplayername = StringArgumentType.getString(context, "name");
                                        System.out.println("Called Spawn with name: " + fakeplayername);
                                        FakePlayerEntity.getInstance(fakeplayername).spawn(MinecraftClient.getInstance().player);
                                        return 1;
                                    })))
                    .then(ClientCommandManager.literal("clear").executes(context -> {
                        FakePlayerEntity.getInstance(fakeplayername).clear();
                        return 1;
                    }));

            dispatcher.register(fakeplayer);
        });
    }
}
