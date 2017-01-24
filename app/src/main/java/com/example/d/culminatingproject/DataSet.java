package com.example.d.culminatingproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Stores all the data from one recording.
 * Comparable implemented to sort recordings by date
 * Parcelable recorded to pass recordings around the program
 * Created by D on 2017-01-20.
 */

public class DataSet implements Comparable, Parcelable {

    private Date initialTime;
    private ArrayList<DataPoint> dataPoints;
    private boolean isFinished;

    /**
     * Creates a new empty data set. Assumes an initial velocity and acceleration of 0
     * pre: none
     * post: new DataSet created
     */
    public DataSet() {
        initialTime = new Date();
        dataPoints = new ArrayList<>();
    }

    /**
     * Creates a DataSet from the information of another. Essentially a constructor for cloning a
     * DataSet.
     * pre: the DataPoints are in the correct order
     * post: new DataSet created
     */
    public DataSet(DataPoint[] points, Date timestamp, boolean recordingFinished) {
        dataPoints = new ArrayList<>();
        Collections.addAll(dataPoints, points);
        initialTime = timestamp;
        isFinished = recordingFinished;
    }

    /**
     * Records a new data point, if the set is still recording. Should be called frequently, on the
     * interval indicated in the settings.
     * pre: none
     * post: new data point added to the set of points
     */
    public void establishNextDataPoint(float xAcceleration, float yAcceleration, float zAcceleration) {

        if (!isFinished) {
            if(dataPoints.isEmpty()) {
                DataPoint newPoint = new DataPoint(System.currentTimeMillis(),System.currentTimeMillis(),
                        xAcceleration, yAcceleration, zAcceleration,
                        0, 0, 0);
                dataPoints.add(newPoint);
            } else {
                DataPoint last = dataPoints.get(dataPoints.size() - 1);
                DataPoint newPoint = new DataPoint(System.currentTimeMillis(),last.getTime(),
                        xAcceleration, yAcceleration, zAcceleration,
                        last.getXVelocity(), last.getYVelocity(), last.getZVelocity());
                dataPoints.add(newPoint);
            }
        }

    }

    /**
     * Finds the current maximum speed out of the set of data and returns it.
     * pre: none
     * post: max speed returned
     * @return the max speed that has been recorded thus far.
     */
    public double getMaxSpeed() {

        double max = 0;

        for (DataPoint dp:
             dataPoints) {
            if (dp.getVelocity() > max) {
                max = dp.getVelocity();
            }
        }

        return max;

    }

    /**
     * Finds the current maximum acceleration out of the set of data and returns it.
     * pre: none
     * post: max acceleration returned
     * @return the max acceleration that has been recorded thus far.
     */
    public double getMaxAcceleration() {

        double max = 0;

        for (DataPoint dp:
                dataPoints) {
            if (dp.getAcceleration() > max) {
                max = dp.getAcceleration();
            }
        }

        return max;

    }

    /**
     * Returns the <tt>Date</tt> at which this DataSet was created, to be used to generate a name for
     * this DataSet or to compare it to other DataSets.
     * pre: none
     * post: timestamp returned
     * @return the timestamp
     */
    public Date getTimestamp() {
        return initialTime;
    }

    /**
     * Returns the amount of time over which data was taken, in milliseconds
     * @return the amount of time over which data was taken, in milliseconds
     */
    public long getTimeElapsedMillis() {
        if (!dataPoints.isEmpty()) {
            return ((dataPoints.get(dataPoints.size()-1)).getTime() - (dataPoints.get(0)).getTime());
        }
        return 0;
    }

    /**
     * Returns the amount of time over which data was taken, in seconds
     * @return the amount of time over which data was taken, in seconds
     */
    public double getTimeElapsedSeconds() {
        return (((double)getTimeElapsedMillis())/1000);
    }

    /**
     * Compares the timestamps of this DataSet with another DataSet.
     * If this DataSet was created first, returns a negative number.
     * It this DataSet and the other were created at the same time, returns 0.
     * If this DataSet was created after the other, return a positive number.
     * @param o The other data set to compare to
     * @return an integer representing whether this DataSet is newer, older, or equally old as the other.
     */
    public int compareTo(Object o) {
        DataSet other = (DataSet)o;
        return (int)(this.getTimestamp().getTime() - other.getTimestamp().getTime());
    }

    /**
     * Returns an array of all the times when data was taken. Note that these times are the times
     * since the recording started, in seconds.
     * @return an array of all the recorded times, relative to when the recording started, in seconds.
     */
    public double[] getTimes() {
        double[] times = new double[dataPoints.size()];
        for (int i = 0; i < times.length; i++) {
            times[i] = ((double)(dataPoints.get(i).getTime()-dataPoints.get(0).getTime()))/1000;
        }
        return times;
    }

    /**
     * Returns an array of all the speeds recorded, in meters per second, since the recoding started.
     * @return an array of all the speeds recorded, in meters per second, since the recoding started
     */
    public double[] getSpeeds() {
        double[] speeds = new double[dataPoints.size()];
        for (int i = 0; i < speeds.length; i++) {
            speeds[i] = dataPoints.get(i).getVelocity();
        }
        return speeds;
    }

    /**
     * Returns an array of all the accelerations recorded, in meters per second squared, since the recoding started.
     * @return an array of all the accelerations recorded, in meters per second squared, since the recoding started
     */
    public double[] getAccelerations() {
        double[] accelerations = new double[dataPoints.size()];
        for (int i = 0; i < accelerations.length; i++) {
            accelerations[i] = dataPoints.get(i).getAcceleration();
        }
        return accelerations;
    }

    /**
     * Makes sure that this DataSet can no longer record new information
     * pre: none
     * post: establishNextDataPoint() will no longer do anything for this DataSet.
     */
    public void finish() {
        isFinished = true;
    }

    /**
     * Don't know what this does, but I need it to pass DataSets between views
     * @return the integer 0.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write all the data of this dataset to a parcel so that it can be sent between views
     * @param dest the parcel
     * @param flags don't know what this is
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        DataPoint[] dataPArray = (DataPoint[]) dataPoints.toArray();
        dest.writeArray(dataPArray);
        dest.writeValue(initialTime);
        dest.writeValue(isFinished);
    }

    /**
     * This CREATOR is used to reconstitute a parcelled DataSet, for reading it in the new View
     */
    public static final Parcelable.Creator<DataSet> CREATOR = new Parcelable.Creator<DataSet>(){
        @Override
        public DataSet createFromParcel(Parcel in) {
            return new DataSet(
                    (DataPoint[]) in.readArray(ClassLoader.getSystemClassLoader()),
                    (Date) in.readValue(ClassLoader.getSystemClassLoader()),
                    (boolean) in.readValue(ClassLoader.getSystemClassLoader())
            );
        }

        @Override
        public DataSet[] newArray(int size) {
            return new DataSet[size];
        }
    };

}
