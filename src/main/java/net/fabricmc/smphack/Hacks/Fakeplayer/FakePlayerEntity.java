package net.fabricmc.smphack.Hacks.Fakeplayer;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Objects;
import java.util.UUID;

public class FakePlayerEntity extends OtherClientPlayerEntity {
    private static FakePlayerEntity instance;
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static FakePlayerEntity getInstance(String name) {
        if (instance != null) {
            instance.clear();
        }
        instance = new FakePlayerEntity(name);
        return instance;
    }
    private FakePlayerEntity(String name) {
        super(Objects.requireNonNull(mc.world), new GameProfile(UUID.randomUUID(), name));
        this.setHealth(20f);
        this.setInvisible(false);
        this.setInvulnerable(true);
    }

    public void spawn(PlayerEntity player) {
        this.copyPositionAndRotation(player);
        Byte playerModel = player.getDataTracker().get(PlayerEntity.PLAYER_MODEL_PARTS);
        dataTracker.set(PlayerEntity.PLAYER_MODEL_PARTS, playerModel);
        this.getAttributes().setFrom(player.getAttributes());
        this.setPose(player.getPose());
        this.getInventory().clone(player.getInventory());
        assert mc.world != null;
        mc.world.addEntity(getId(), this);
        System.out.println("FakePlayer Spawned Instance: " + this);
    }
    public void clear() {
        this.kill();
        assert mc.world != null;
        mc.world.removeEntity(getId(), RemovalReason.DISCARDED);
        setRemoved(RemovalReason.DISCARDED);
        instance = null;
    }
}



