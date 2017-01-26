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
    private boolean kmh;
    private boolean h;

    /**
     * Represent the different device refresh rates
     */
    public enum RefreshRate {
        SLOW, MEDIUM, FAST,
    }

    /**
     * Create a new setting.
     * pre: none
     * post: new setting created
     * @param rate the desired refresh rate
     * @param kilometers should speed be in km/h?
     * @param hours should time be in hours?
     */
    public Setting(RefreshRate rate, boolean kilometers, boolean hours){
        refreshRate = rate;
        kmh = kilometers;
        h = hours;
    }

    /**
     * Create the default setting, with a medium refresh rate, and speed in m/s and time in seconds.
     */
    public Setting() {
        this(RefreshRate.MEDIUM, false, false);
    }

    /**
     * Returns the refresh rate of this setting.
     * @return the refresh rate of this setting
     */
    public RefreshRate getRefreshRate() {
        return refreshRate;
    }

    /**
     * Returns true if speed should be in km/h
     * @return true if speed should be in km/h, otherwise, false
     */
    public boolean isInKMPerH() {
        return kmh;
    }

    /**
     * Returns true if time should be in hours
     * @return true if time should be in hours, otherwise false
     */
    public boolean isInHours() {
        return h;
    }

}
