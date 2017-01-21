package com.example.d.culminatingproject;

import java.sql.Time;

/**
 * Stores a single set of data taken from the sensors
 * Created by D on 2017-01-16.
 */
public class DataPoint {

    private long t;
    private double v;
    private double a;

    public DataPoint(long time, double velocity, double acceleration) {

        t =time;
        v = velocity;
        a = acceleration;

    }

    public long getTime() {
        return t;
    }

    public double getVelocity() {
        return v;
    }

    public double getAcceleration() {
        return a;
    }

}
