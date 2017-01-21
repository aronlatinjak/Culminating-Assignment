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
import android.widget.Switch;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.*;
import com.jjoe64.graphview.series.DataPoint;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class StatisticsActivity extends AppCompatActivity implements SensorEventListener {

    private LineGraphSeries<DataPoint> points;
    private SensorManager sensorManager;
    private Sensor accel;
    private DataSet dataSet;
    private Timer timer;

    // GUI elements
    private Switch velAccelSwitch;
    private GraphView graphView;
    private TextView graphTitle;
    private TextView maxVelocityView;
    private TextView maxAccelerationView;
    private TextView timeElapsedView;

    // Used for the low-pass filter
    private float[] gravity;

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

        // Used to store the data recorded
        dataSet = new DataSet();

        // Initialize the gravity variables for the low-pass filter
        gravity = new float[]{0,0,0};

        // Access the accelerometer
        // I tried the linear acceleration to remove the effects of gravity, but it didn't work
        // so I stuck to using an equation provided by the android reference site
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        // Access the switch, which alternates between velocity time and position time
        velAccelSwitch = (Switch) findViewById(R.id.switchVelocityPosition);

        // Access some of the labels
        graphTitle = (TextView) findViewById(R.id.tvGraphTitle);
        maxVelocityView = (TextView) findViewById(R.id.tvMaxVelocity);
        maxAccelerationView = (TextView) findViewById(R.id.tvMaxAccel);
        timeElapsedView = (TextView) findViewById(R.id.tvTime);

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

    // TODO:
    // Hey aron. could you make the method for saving the data here? thanks.





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

        // Get all the times
        double[] times = dataSet.getTimes();
        // Store new points
        DataPoint[] freshPoints = new DataPoint[times.length];
        // String for title of graph
        final int titleString;

        // if the switch is to the right
        if(velAccelSwitch.isChecked()) {
            // Get all the accelerations
            double[] accels = dataSet.getAccelerations();
            for (int i = 0; i < times.length; i++) {
                freshPoints[i] = new DataPoint(times[i], accels[i]);
            }
            // Set the graph title
            titleString = R.string.at_graph_label;
        } else {
            // Get all the velocities
            double[] velocities = dataSet.getSpeeds();
            for (int i = 0; i < times.length; i++) {
                freshPoints[i] = new DataPoint(times[i], velocities[i]);
            }
            // Set the graph title
            titleString = R.string.vt_graph_label;
        }

        // swap out the old DataPoints with the new ones
        points.resetData(freshPoints);
        // if there is data, stretch graph to fit it
        if (times.length-1>0) {
            graphView.getViewport().setMaxX(times[times.length-1]);
        }

        // Update elements of the UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DecimalFormat df = new DecimalFormat("#.00");

                // Set graph title
                graphTitle.setText(titleString);
                // Set max velocity
                maxVelocityView.setText(getString(R.string.max_velocity) + ":\t\t\t\t\t\t\t\t" +
                        df.format(dataSet.getMaxSpeed()) + "m/s");
                // Set max acceleration
                maxAccelerationView.setText(getString(R.string.max_acceleration) + ":\t\t\t" +
                        df.format(dataSet.getMaxAcceleration()) + "m/s" + '\u00B2');
                // Set time elapsed
                timeElapsedView.setText(getString(R.string.time_elapsed) + ":\t\t\t\t\t\t\t\t" +
                        df.format(dataSet.getTimeElapsedSeconds()) + 's');
            }
        });

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

        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
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
     * When the device gets new information from the accelerometer, send it to the DataSet.
     * @param event the event that the accelerometer generates
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        // Uses a low-pass filter to remove acceleration due to gravity
        // This code was taken directly from the android reference website, as I would
        // have no idea how to do this otherwise.
        // https://developer.android.com/guide/topics/sensors/sensors_motion.html#sensors-motion-accel

        final float alpha = 0.8F;

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        dataSet.establishNextDataPoint(
                event.values[0] - gravity[0],
                event.values[1] - gravity[1],
                event.values[2] - gravity[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
