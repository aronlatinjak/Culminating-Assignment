package com.example.d.culminatingproject;

/**
 * Created by D on 2017-01-24.
 */

/**
 * Used to store the settings
 */
public class Setting {

    private RefreshRate refreshRate;
    boolean kmh;
    boolean h;

    public enum RefreshRate {
        SLOW, MEDIUM, FAST,
    }

    public Setting(RefreshRate rate, boolean kilometers, boolean hours){
        refreshRate = rate;
        kmh = kilometers;
        h = hours;
    }

    public RefreshRate getRefreshRate() {
        return refreshRate;
    }

    public boolean isInKMPerH() {
        return kmh;
    }

    public boolean isinHours() {
        return h;
    }

}
