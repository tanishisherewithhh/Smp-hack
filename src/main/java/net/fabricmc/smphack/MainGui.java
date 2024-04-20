package net.fabricmc.smphack;

public abstract class MainGui {

    // i dont know why am i using this. Yes i am bad at OOP
    public boolean enabled=false;
    public void toggled(){
        enabled=!enabled;
    }
    public void update() {}
}
