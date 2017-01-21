package com.example.d.culminatingproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.*;
import com.jjoe64.graphview.series.DataPoint;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class StatisticsActivity extends AppCompatActivity implements SensorEventListener {

    private LineGraphSeries<DataPoint> points;
    private GraphView graphView;
    private SensorManager sensorManager;
    private Sensor accel;
    private DataSet dataSet;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Puts a back button in the top bar
        ActionBar actionBar = getSupportActionBar();
        try {
            actionBar.setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            System.err.println("Something went wrong.\n" + e.getMessage());
        }

        dataSet = new DataSet();

        // Access the accelerometer
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // using linear accelerometer to ignore acceleration due
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Access the graph
        graphView = (GraphView) findViewById(R.id.data_graph);

        // Makes sure that the user always sees the data from beginning to end
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(0);

        // Used to store the data for the graph
        points = new LineGraphSeries<>(new DataPoint[] {new DataPoint(0,0)});

        // Attach the data to the graph
        graphView.addSeries(points);

        // Used to redraw the graph
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                refreshGraph();
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 250, 250);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                timer.cancel();
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Updates the graph with the new information from the DataSet
     * pre: none
     * post: graph updated
     */
    public void refreshGraph() {
        // TODO:
        /*
        switch() {
            case SPEED:
                break;
            case ACCELERATION:
                break;
            default:
                break;
        }*/

        // Get all the current speeds and times
        double[] accels = dataSet.getAccelerations();
        double[] times = dataSet.getTimes();

        DataPoint[] freshPoints = new DataPoint[accels.length];
        for (int i = 0; i < freshPoints.length; i++) {
            freshPoints[i] = new DataPoint(times[i], accels[i]);
        }

        // swap out the old DataPoints with the new ones
        points.resetData(freshPoints);
        graphView.getViewport().setMaxX(times[times.length-1]);

    }

    /**
     * Starts listening to the accelerometer when the view starts.
     * pre: none
     * post: sensor listening started
     */
    @Override
    protected void onResume() {
        super.onResume();

        // TODO: implement a switch structure to change the frequency of the requests for the sensor

        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Pause listening to the accelerometer when the app is not open or the screen is locked.
     * pre: none
     * post: sensor listening paused
     */
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    /**
     * When the device gets new information from the accelerometer, send it to the DataSet
     * @param event the event that the accelerometer generates
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] tokens = event.values;
        //float x = tokens[0];
        dataSet.establishNextDataPoint(tokens[0],tokens[1],tokens[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
