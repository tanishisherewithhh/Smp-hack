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

    public enum nametagcolour {
        BLACK,
        DARK_BLUE,
        DARK_GREEN,
        DARK_AQUA,
        DARK_RED,
        DARK_PURPLE,
        GOLD,
        GRAY,
        DARK_GRAY,
        BLUE,
        GREEN,
        AQUA,
        RED,
        LIGHT_PURPLE,
        YELLOW,
        WHITE
    }

    public enum ArmorDurabilityDisplay {
        Percent,
        Bar,
        Number
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
    public enum Buttons
    {
        RightMButton,
        LeftMButton
    }
    public enum MouseKeybind
    {
        RightButton,
        LeftButton
    }


    public SpeedMinemodes SpeedmMineModes = SpeedMinemodes.HasteMode;
    public TextSide Textside = TextSide.Left;
    public CoordsSide Coordside = CoordsSide.BottomLeft;
    public SpeedMode Speedmode = SpeedMode.onGround;
    public Buttons ButtonAuto = Buttons.RightMButton;
    public MouseKeybind MouseKeybindButton = MouseKeybind.RightButton;
    public nametagcolour EntityNametagcolour= nametagcolour.WHITE;
    public nametagcolour PlayerNametagcolour= nametagcolour.WHITE;
    public ArmorDurabilityDisplay ArmorDurability= ArmorDurabilityDisplay.Percent;


    public int NobreakHaste = 1;
    private int flySpeed = 10;
    private int CrystalBreakDelay_in_seconds = 1;
    private int KillAuraDelay = 8;
    private int boatflySpeed = 1;
    private int range = 4;//for auto crystal and killaura
    private int reach = 4;// for player
    private int speedforjesus = 1;
    private int DelayAC = 0;
    private int AntiSuicideDamage=10;

    public boolean Antikick = true;
    public boolean Fullbright = true;
    public boolean NoWeather = false;
    private boolean enableSpeedmine = false;
    private boolean NoHurtCam = false;
    private boolean actionBar = true;
    private boolean AutoSprint = false;
    private boolean AntiHunger = false;
    private boolean MouseButton = true;
    private boolean AutoClicker = false;
    private boolean AutoClickerToggle = false;
    private boolean nametagshadow = false;
    private boolean TextShadow = false;
    private boolean SwordAxeOnly = false;
    private boolean MultiTarget = false;
    private boolean AutoDelayKA = true;
    private boolean NoAccidentalFireworks = false;
    private boolean showMuppet=true;
    private boolean AutoReplace=false;
    private boolean AntiSuicide=false;
    private boolean OnlyOwn=false;
    private boolean AutoTool=false;

    private int playernametagcolour = 0xFF;


    public int getPlayernametagcolour() {
        return playernametagcolour;
    }



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
    public boolean getAutoHotbar() {
        return AutoReplace;
    }

    public void setAutoHotbar(boolean Autoreplace) {
        this.AutoReplace = Autoreplace;
    }
    public boolean getMouseButton() {
        return MouseButton;
    }

    public void setMouseButton(boolean Mousebutton) {
        this.MouseButton = Mousebutton;
    }

    public boolean getAutoTool() {
        return AutoTool;
    }

    public void setAutoTool(boolean Autotool) {
        this.AutoTool = Autotool;
    }

    public boolean getShowMuppet() {
        return showMuppet;
    }

    public void setShowMuppet(boolean ShowMuppet) {
        this.showMuppet = ShowMuppet;
    }
    public boolean getNoAccidentalFireworks() {
        return NoAccidentalFireworks;
    }

    public void setNoAccidentalFireworks(boolean fireworks) {
        this.NoAccidentalFireworks = fireworks;
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

    public boolean getAutoSprint() {
        return AutoSprint;
    }

    public void setAutoSprint(boolean AutoSprint) {
        this.AutoSprint = AutoSprint;
    }

    public boolean getSwordAxeOnly() {
        return SwordAxeOnly;
    }

    public void setSwordAxeOnly(boolean Swordaxeonly) {
        this.SwordAxeOnly = Swordaxeonly;
    }

    public boolean getMultiTarget() {
        return MultiTarget;
    }

    public void setMultiTarget(boolean Multitarget) {
        this.MultiTarget = Multitarget;
    }

    public boolean getAutoDelayKA() {
        return AutoDelayKA;
    }

    public void setAutoDelayKA(boolean Autodelay) {
        this.AutoDelayKA = Autodelay;
    }


    public boolean getAntiHunger() {
        return AntiHunger;
    }

    public void setAntiHunger(boolean AntiHunger) {
        this.AntiHunger = AntiHunger;
    }


    public boolean getNoWeather() {
        return NoWeather;
    }

    public void setNoWeather(boolean NoWeather) {
        this.NoWeather = NoWeather;
    }
    public boolean getTextshadow() {
        return TextShadow;
    }

    public void setTextShadow(boolean textShadow) {
        this.TextShadow = textShadow;
    }
    public boolean getnametagshadow() {
        return nametagshadow;
    }

    public void setAntiSuicide(boolean AntiSuicide) {
        this.AntiSuicide = AntiSuicide;
    }
    public boolean getAntiSuicide() {
        return AntiSuicide;
    }
    public void setOnlyOwn(boolean OnlyOwn) {
        this.OnlyOwn = OnlyOwn;
    }
    public boolean getOnlyOwn() {
        return OnlyOwn;
    }
    public void setNametagshadow(boolean textShadow) {
        this.nametagshadow = textShadow;
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


    public void setDelayAC(int delay) {
        this.DelayAC = delay;
    }

    public int getDelayAC() {
        return DelayAC;
    }
    public void setASdamage(int aSdamage) {
        this.AntiSuicideDamage = aSdamage;
    }

    public int getASdamage() {
        return AntiSuicideDamage;
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

    public boolean getAutoClicker() {
        return AutoClicker;
    }
    public void setAutoClicker(boolean Autoclicker) {this.AutoClicker = Autoclicker;}

    public boolean getAutoClickerToggle() {
        return AutoClickerToggle;
    }
    public void setAutoClickerToggle(boolean AutoclickerT) {this.AutoClickerToggle = AutoclickerT;}

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
