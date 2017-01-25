package com.example.d.culminatingproject;

/**
 * Created by D on 2017-01-24.
 */

import java.io.Serializable;

/**
 * Used to store the settings
 */
public class Setting implements Serializable {

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

    public Setting() {
        this(RefreshRate.MEDIUM, false, false);
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
