package net.fabricmc.smphack.Hacks.Reach;

import net.fabricmc.smphack.GeneralConfig;
import net.fabricmc.smphack.MainGui;

public class Reach  extends MainGui {

   public static boolean enabled=false;


    @Override
    public void toggled()
    {
        enabled=!enabled;
    }
    @Override
    public void update()
    {
        onReach();
    }

     public void onReach() {
          EventReach event = new EventReach(GeneralConfig.getConfig().getReach()+0.5F);
            event.setReach((float) (event.getReach() +GeneralConfig.getConfig().getReach()+0.5));
        }
    }

