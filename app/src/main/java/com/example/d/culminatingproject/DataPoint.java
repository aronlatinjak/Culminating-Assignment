package com.example.d.culminatingproject;

import java.sql.Time;

/**
 * Stores a single set of data taken from the sensors
 * Created by D on 2017-01-16.
 */
public class DataPoint {

    private Time time;
    private double v;
    private double a;

    public void DataPoint(Time t, double velocity, double acceleration) {

        time = t;
        v = velocity;
        a = acceleration;

    }

    public Time getTime() {
        return time;
    }

    public double getVelocity() {
        return v;
    }

    public double getAcceleration() {
        return a;
    }

}
