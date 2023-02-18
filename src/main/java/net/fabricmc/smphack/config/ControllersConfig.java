package net.fabricmc.smphack.config;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "Config Settings")
public class ControllersConfig implements ConfigData {
    public float fireOpacity = 0.9F;
    public float JETPACK_MAX_SPEED=1F;
    public float speed_multiplier= 1.2F;
    public boolean Antikick=true;
    public boolean Fullbright=true;
    public int NobreakHaste=1;
    public int NobreakCooldown=1;
    public boolean NoWeather=false;
    public static String SpeedMineMode = "";
    public static String SpeedMode = "";
    public static String TextSide = "Left";
    public static String CoordsSide = "BottomLeft";
    private boolean enableSpeedmine = false;
    private int flySpeed = 10;
    private boolean NoHurtCam=false;
    private boolean actionBar = true;

    public float getJETPACK_MAX_SPEED() {
        return JETPACK_MAX_SPEED;
    }

    public void setJETPACK_MAX_SPEED(float jetpackspeed) {
        this.JETPACK_MAX_SPEED=jetpackspeed;
    }

    public float getSpeed_multiplier() {
        return speed_multiplier;
    }

    public void setSpeed_multiplier(float speed_multiplier) {
        this.speed_multiplier=speed_multiplier;
    }

    public void setFireOpacity(float Fireopacity) {
        this.fireOpacity=Fireopacity;
    }
    public float getFireOpacity() {
        return fireOpacity;
    }

    public boolean isEnableAntikick() {return Antikick;}
    public void setAntikick(boolean Anitkick) {
        this.Antikick=Anitkick;
    }

    public boolean getFullbright() {
        return Fullbright;
    }
    public void setFullbright(boolean fullbright) {
        this.Fullbright=fullbright;
    }
    public boolean getNoHurtCam() {
        return NoHurtCam;
    }
    public void setNoHurtCam(boolean noHurtCam) {
        this.NoHurtCam=noHurtCam;
    }

    public boolean getNoWeather() {
        return NoWeather;
    }
    public void setNoWeather(boolean NoWeather) {
        this.NoWeather=NoWeather;
    }

    public void setNobreakHaste(int haste) {this.NobreakHaste=haste;}
    public int getNobreakHaste() {return NobreakHaste;}

    public void setNobreakCooldown(int Cooldown) {this.NobreakCooldown=Cooldown;}
    public int getNobreakCooldown()
    {return NobreakCooldown;}

    public String getSpeedMineMode() {return SpeedMineMode;}
    public void setSpeedMineMode(String minemode) {SpeedMineMode = minemode;}
    public String getSpeedMode() {return SpeedMode;}
    public void setSpeedMode(String Speedmode) {SpeedMode = Speedmode;}

    public String getTextSide() {return TextSide;}
    public void setTextSide(String Textside) {TextSide = Textside;}

    public String getCoordsSide() {return CoordsSide;}
    public void setCoordsSide(String CoordSide) {CoordsSide = CoordSide;}

    public boolean isEnableSpeedmine() {
        return enableSpeedmine;
    }
    public void setEnableSpeedmine(boolean SpeedMine) {this.enableSpeedmine = SpeedMine;}


    public int getFlySpeed() {
        return flySpeed;
    }

    public void setFlySpeed(int flySpeed) {
        this.flySpeed = flySpeed;
    }

    public boolean isActionBar() {
        return actionBar;
    }

    public void setActionBar(boolean actionBar) {
        this.actionBar = actionBar;
    }


}

