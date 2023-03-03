package net.fabricmc.smphack.config;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;


@Config(name = "Config Settings")
public class ControllersConfig implements ConfigData {

    public float fireOpacity = 0.9F;
    public float JETPACK_MAX_SPEED = 1F;
    public float speed_multiplier = 1.2F;


    public enum SpeedMinemodes {
        HasteMode,
        NoBreakDelay
    }

    public enum TextSide {
        Left,
        TopRight,
        BottomRight
    }

    public enum CoordsSide {
        Top,
        TopRight,
        BottomRight,
        BottomLeft
    }

    public enum SpeedMode
    {
        onGround,
        Strafe,
        Bhop
    }

    public SpeedMinemodes SpeedmMineModes = SpeedMinemodes.HasteMode;
    public TextSide Textside = TextSide.Left;
    public CoordsSide Coordside = CoordsSide.BottomLeft;
    public SpeedMode Speedmode = SpeedMode.onGround;


    public int NobreakHaste = 1;
    private int flySpeed = 10;
    private int CrystalBreakDelay_in_seconds = 1;
    private int KillAuraDelay = 8;
    private int boatflySpeed = 1;
    private int range = 6;//for auto crystal and killaura
    private int reach = 4;// for player
    private int speedforjesus = 1;


    public boolean Antikick = true;
    public boolean Fullbright = true;
    public boolean NoWeather = false;
    private boolean enableSpeedmine = false;
    private boolean NoHurtCam = false;
    private boolean actionBar = true;



    public float getJETPACK_MAX_SPEED() {
        return JETPACK_MAX_SPEED;
    }

    public void setJETPACK_MAX_SPEED(float jetpackspeed) {
        this.JETPACK_MAX_SPEED = jetpackspeed;
    }

    public float getSpeed_multiplier() {
        return speed_multiplier;
    }

    public void setSpeed_multiplier(float speed_multiplier) {
        this.speed_multiplier = speed_multiplier;
    }

    public void setFireOpacity(float Fireopacity) {
        this.fireOpacity = Fireopacity;
    }

    public float getFireOpacity() {
        return fireOpacity;
    }

    public boolean isEnableAntikick() {
        return Antikick;
    }

    public void setAntikick(boolean Anitkick) {
        this.Antikick = Anitkick;
    }

    public boolean getFullbright() {
        return Fullbright;
    }

    public void setFullbright(boolean fullbright) {
        this.Fullbright = fullbright;
    }

    public boolean getNoHurtCam() {
        return NoHurtCam;
    }

    public void setNoHurtCam(boolean noHurtCam) {
        this.NoHurtCam = noHurtCam;
    }

    public boolean getNoWeather() {
        return NoWeather;
    }

    public void setNoWeather(boolean NoWeather) {
        this.NoWeather = NoWeather;
    }

    public void setNobreakHaste(int haste) {
        this.NobreakHaste = haste;
    }

    public int getNobreakHaste() {
        return NobreakHaste;
    }

    public void setSpeedforjesus(int Speed) {
        this.speedforjesus = Speed;
    }

    public int getSpeedforjesus() {
        return speedforjesus;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getRange() {
        return range;
    }

    public void setReach(int reach) {
        this.reach = reach;
    }

    public int getReach() {
        return reach;
    }

    public int getBoatflySpeed() {
        return boatflySpeed;
    }
    public void setBoatflySpeed(int BoatflySpeed) {
        this.boatflySpeed = BoatflySpeed;
    }

    public int getCrystalBreakDelay_in_seconds() {
        return CrystalBreakDelay_in_seconds;
    }
    public void setCrystalBreakDelay_in_seconds(int CrystalBreakDelay) {
        this.CrystalBreakDelay_in_seconds = CrystalBreakDelay;
    }

    public int getKillAuraDelay() {
        return KillAuraDelay;
    }
    public void setKillAuraDelay(int KillauraDelay) {
        this.KillAuraDelay = KillauraDelay;
    }

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

