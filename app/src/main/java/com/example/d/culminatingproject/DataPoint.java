package com.example.d.culminatingproject;

import java.sql.Time;

/**
 * Stores a single set of data taken from the sensors
 * Created by D on 2017-01-16.
 */
public class DataPoint {

    private long t;
    private double vX;
    private double vY;
    private double vZ;
    private double aX;
    private double aY;
    private double aZ;

    /**
     * Used to store a set of data.
     * @param time
     * @param xAcceleration
     * @param yAcceleration
     * @param zAcceleration
     * @param lastXVelocity
     * @param lastYVelocity
     * @param lastZVelocity
     */
    public DataPoint(long time,
                     double xAcceleration, double yAcceleration, double zAcceleration,
                     double lastXVelocity, double lastYVelocity, double lastZVelocity) {

        t =time;
        aX = xAcceleration;
        aY = yAcceleration;
        aZ = zAcceleration;
        vX = lastXVelocity + xAcceleration;
        vY = lastYVelocity + yAcceleration;
        vZ = lastZVelocity + zAcceleration;

    }

    public long getTime() {
        return t;
    }

    public double getXVelocity() {
        return vX;
    }

    public double getYVelocity() {
        return vY;
    }

    public double getZVelocity() {
        return vZ;
    }

    /**
     * Returns the vector sum of all three velocities, in m/s
     * pre: none
     * post: vector sum of velocities returned
     * @return vector sum of velocities
     */
    public double getVelocity() {
        return Math.sqrt(vX*vX + vY*vY + vZ*vZ);
    }

    public double getXAcceleration() {
        return aX;
    }

    public double getYAcceleration() {
        return aY;
    }

    public double getZAcceleration() {
        return aZ;
    }

    /**
     * Returns the vector sum of all three accelerations, in m/(s*s)
     * pre: none
     * post: vector sum of accelerations returned
     * @return vector sum of accelerations
     */
    public double getAcceleration() {
        return Math.sqrt(aX*aX + aY*aY + aZ*aZ);
    }

}
