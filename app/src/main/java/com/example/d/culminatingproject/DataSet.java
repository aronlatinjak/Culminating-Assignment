package com.example.d.culminatingproject;

import android.content.Context;
import android.hardware.Sensor;

import java.util.ArrayList;
import java.util.Date;

/**
 * Stores all the data from one recording.
 * Created by D on 2017-01-20.
 */

public class DataSet implements Comparable {

    private Date initialTime;
    private ArrayList<DataPoint> dataPoints;
    private boolean isFinished;
    private Sensor accel;

    public DataSet() {
        //getSystemService(Context.SENSOR_SERVICE);
        initialTime = new Date();
        dataPoints = new ArrayList<>();
        dataPoints.add(new DataPoint(System.currentTimeMillis(), 0, 0, 0, 0, 0, 0));
    }

    /**
     * Records a new data point, if the set is still recording. Should be called frequently, on the
     * interval indicated in the settings.
     * pre: none
     * post: new data point added to the set of points
     */
    public void establishNextDataPoint(double xAcceleration, double yAcceleration, double zAcceleration) {

        if (!isFinished) {
            DataPoint last = dataPoints.get(dataPoints.size()-1);
            DataPoint newPoint = new DataPoint(System.currentTimeMillis(),
                    xAcceleration, yAcceleration, zAcceleration,
                    last.getXVelocity(), last.getYVelocity(), last.getZVelocity());
            dataPoints.add(newPoint);
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
            if (dp.getVelocity() > max) {
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
        return ((dataPoints.get(dataPoints.size()-1)).getTime() - (dataPoints.get(0)).getTime());
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
        for (int i = 0; i < dataPoints.size(); i++) {
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
        for (int i = 0; i < dataPoints.size(); i++) {
            speeds[i] = dataPoints.get(i).getVelocity();
        }
        return speeds;
    }

    public double[] getAccelerations() {
        double[] accelerations = new double[dataPoints.size()];
        for (int i = 0; i < dataPoints.size(); i++) {
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

}
