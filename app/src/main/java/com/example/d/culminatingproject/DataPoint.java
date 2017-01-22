package com.example.d.culminatingproject;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Stores a single set of data taken from the sensors
 * Created by D on 2017-01-16.
 */
public class DataPoint implements Parcelable {

    private long t;
    private double vX;
    private double vY;
    private double vZ;
    private double aX;
    private double aY;
    private double aZ;

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
                     double xAcceleration, double yAcceleration, double zAcceleration,
                     double lastXVelocity, double lastYVelocity, double lastZVelocity) {

        t = time;
        aX = xAcceleration;
        aY = yAcceleration;
        aZ = zAcceleration;
        vX = lastXVelocity + ((double)(time-lastTime)/1000)*xAcceleration;
        vY = lastYVelocity + ((double)(time-lastTime)/1000)*yAcceleration;
        vZ = lastZVelocity + ((double)(time-lastTime)/1000)*zAcceleration;
        System.out.println("" + vX + " " + vY + " " + vZ);

    }

    protected DataPoint(Parcel in) {
        t = in.readLong();
        vX = in.readDouble();
        vY = in.readDouble();
        vZ = in.readDouble();
        aX = in.readDouble();
        aY = in.readDouble();
        aZ = in.readDouble();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(t);
        dest.writeDouble(vX);
        dest.writeDouble(vY);
        dest.writeDouble(vZ);
        dest.writeDouble(aX);
        dest.writeDouble(aY);
        dest.writeDouble(aZ);
    }
}
