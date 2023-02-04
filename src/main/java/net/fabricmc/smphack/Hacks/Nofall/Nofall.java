package net.fabricmc.smphack.Hacks.Nofall;

import net.fabricmc.smphack.MainGui;
import net.minecraft.client.network.ClientPlayerEntity;

import static net.fabricmc.smphack.Hacks.BoatFly.BoatFly.player;

public class Nofall extends MainGui {

    @Override
    public void toggled()
    {
        enabled=!enabled;
    }

    @Override
    public void update()
    {
        NoFallHack nofall = new NoFallHack();
        nofall.cancelFallDamage((ClientPlayerEntity) player);
    }


}
