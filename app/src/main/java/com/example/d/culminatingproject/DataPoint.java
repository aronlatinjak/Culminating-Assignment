package com.example.d.culminatingproject;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Stores a single set of data taken from the sensors
 * Created by D on 2017-01-16.
 */
public class DataPoint implements Parcelable, Serializable {

    private long t;
    private float vX;
    private float vY;
    private float vZ;
    private float aX;
    private float aY;
    private float aZ;

    /**
     * Used to store a set of data taken from the accelerometer.
     * pre: values don't overflow double or long
     * post: new DataPoint created
     * @param time system time
     * @param xAcceleration device's x acceleration, in m/(s*s)
     * @param yAcceleration device's y acceleration, in m/(s*s)
     * @param zAcceleration device's z acceleration, in m/(s*s)
     * @param lastXVelocity last calculated x velocity in m/s, used to calculate current x velocity
     * @param lastYVelocity last calculated y velocity in m/s, used to calculate current y velocity
     * @param lastZVelocity last calculated z velocity in m/s, used to calculate current z velocity
     */
    public DataPoint(long time, long lastTime,
                     float xAcceleration, float yAcceleration, float zAcceleration,
                     float lastXVelocity, float lastYVelocity, float lastZVelocity) {

        t = time;
        aX = xAcceleration;
        aY = yAcceleration;
        aZ = zAcceleration;
        vX = lastXVelocity + ((float)(time-lastTime)/1000)*xAcceleration;
        vY = lastYVelocity + ((float)(time-lastTime)/1000)*yAcceleration;
        vZ = lastZVelocity + ((float)(time-lastTime)/1000)*zAcceleration;

    }

    /**
     * Create a DataPoint from the given Parcel.
     * @param in The parcel to read from.
     */
    protected DataPoint(Parcel in) {
        t = in.readLong();
        vX = in.readFloat();
        vY = in.readFloat();
        vZ = in.readFloat();
        aX = in.readFloat();
        aY = in.readFloat();
        aZ = in.readFloat();
    }

    /**
     * For reading the DataPoint after it has been passed between views.
     */
    public static final Creator<DataPoint> CREATOR = new Creator<DataPoint>() {
        @Override
        public DataPoint createFromParcel(Parcel in) {
            return new DataPoint(in);
        }

        @Override
        public DataPoint[] newArray(int size) {
            return new DataPoint[size];
        }
    };

    /**
     * Returns the system time, in millis, of this DataPoint
     * pre: none
     * post: time, in millis, returned
     * @return time in millis
     */
    public long getTime() {
        return t;
    }

    /**
     * Returns the data point's x velocity
     * @return The data point's x velocity
     */
    public float getXVelocity() {
        return vX;
    }

    /**
     * Returns the data point's y velocity
     * @return The data point's y velocity
     */
    public float getYVelocity() {
        return vY;
    }

    /**
     * Returns the data point's z velocity
     * @return The data point's z velocity
     */
    public float getZVelocity() {
        return vZ;
    }

    /**
     * Returns the vector sum of all three velocities, in m/s
     * pre: none
     * post: vector sum of velocities returned
     * @return vector sum of velocities
     */
    public float getVelocity() {
        return (float)Math.sqrt(vX*vX + vY*vY + vZ*vZ);
    }

    // Apparently these next three methods are not yet used, but they
    // may be useful for the export feature

    public float getXAcceleration() {
        return aX;
    }

    public float getYAcceleration() {
        return aY;
    }

    public float getZAcceleration() {
        return aZ;
    }

    /**
     * Returns the vector sum of all three accelerations, in m/(s*s)
     * pre: none
     * post: vector sum of accelerations returned
     * @return vector sum of accelerations
     */
    public float getAcceleration() {
        return (float)Math.sqrt(aX*aX + aY*aY + aZ*aZ);
    }

    /**
     * Returns zero
     * @return zero
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write this DataPoint to a parcel.
     * @param dest The Parcel to write to.
     * @param flags Don't known what this does, and it is not used.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(t);
        dest.writeFloat(vX);
        dest.writeFloat(vY);
        dest.writeFloat(vZ);
        dest.writeFloat(aX);
        dest.writeFloat(aY);
        dest.writeFloat(aZ);
    }
}
