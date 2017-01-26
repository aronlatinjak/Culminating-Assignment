package com.example.d.culminatingproject;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The activity open while recording data.
 */
public class StatisticsActivity extends AppCompatActivity implements SensorEventListener {

    private static final int GRAPH_REFRESH_RATE = 250;
    private static final int GRAPH_TIME_RANGE = 30;
    // Used to set how many points to draw on the graph
    private static final int GRAPH_MAX_POINTS = 300;
    private static final int DATA_POINTS_TO_DISCARD = 20;

    private LineGraphSeries<DataPoint> points;
    private SensorManager sensorManager;
    private Sensor accel;
    private DataSet dataSet;
    private Timer timer;
    private Setting setting;

    // This variable is used to discard the first couple pieces of data. This helps make sure
    // that the gravity value adjusts correctly before the recording starts
    private int accuracyCounter;

    // GUI elements
    private Switch velAccelSwitch;
    private GraphView graphView;
    private TextView graphTitle;
    private TextView maxVelocityView;
    private TextView maxAccelerationView;
    private TextView timeElapsedView;
    private TextView yAxisView;
    private TextView xAxisView;
    private ImageButton stopButton;

    private float[] gravity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Puts a back button in the top bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar !=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Used to store the data recorded
        dataSet = new DataSet();

        // Access the accelerometer. Linear accelerometer automatically removes gravity.
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Access the switch, which alternates between velocity time and position time
        velAccelSwitch = (Switch) findViewById(R.id.switchVelocityPosition);

        // Get the recording settings
        setting = SaveStaticClass.readSettings(getApplicationContext());

        // Set up accuracy counter to discard first 10 results
        accuracyCounter = DATA_POINTS_TO_DISCARD;

        // Set up the gravity for the low-pass filter for removing gravity from acceleration
        gravity = new float[]{0,0,0};

        // Access some of the labels
        graphTitle = (TextView) findViewById(R.id.tvGraphTitle);
        maxVelocityView = (TextView) findViewById(R.id.tvMaxVelocity);
        maxAccelerationView = (TextView) findViewById(R.id.tvMaxAccel);
        timeElapsedView = (TextView) findViewById(R.id.tvTime);
        yAxisView = (TextView) findViewById(R.id.tvGraphY);
        xAxisView = (TextView) findViewById(R.id.tvGraphX);

        // Access the stop button
        stopButton = (ImageButton) findViewById(R.id.ibStop);
        // Add functionality for the stop button
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSet.finish();

                // TODO: add save method here

                Intent i = new Intent(getApplicationContext(), HistoryViewActivity.class);
                i.putExtra("data_set", dataSet);
                i.putExtra("came_from_recording", true);
                startActivity(i);
            }
        });

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
        timer.schedule(timerTask, GRAPH_REFRESH_RATE, GRAPH_REFRESH_RATE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                timer.cancel();
                // Tell the user that their data will be destroyed
                Toast.makeText(getApplicationContext(), "Stopping and deleting recording...", Toast.LENGTH_SHORT)
                        .show();
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Makes sure that the user knows that their data is deleted if teh back button is pressed.
     */
    @Override
    public void onBackPressed() {
        timer.cancel();
        // Tell the user that their data will be destroyed
        Toast.makeText(getApplicationContext(), "Stopping and deleting recording...", Toast.LENGTH_SHORT)
                .show();
        super.onBackPressed();
    }

    /**
     * Updates the graph with the new information from the DataSet
     * pre: none
     * post: graph updated
     */
    public void refreshGraph() {

        // Get all the times
        double[] times = dataSet.getTimes();

        int numPointsToDraw = (times.length > GRAPH_MAX_POINTS)? GRAPH_MAX_POINTS: times.length;

        // Store new points
        DataPoint[] freshPoints = new DataPoint[numPointsToDraw];
        // String for title of graph
        final int titleString;
        // String for the y-axis of graph
        final int axisLabel;

        // Calculate which points to draw
        int firstPointDrawn = (times.length-GRAPH_MAX_POINTS>0)?
                times.length-GRAPH_MAX_POINTS:
                0;

        // if the switch is to the right
        if(velAccelSwitch.isChecked()) {
            // Get all the accelerations
            double[] accels = dataSet.getAccelerations();
            // Add the relevant accelerations to the "to be drawn" array
            for (int i = 0; i < numPointsToDraw; i++) {
                freshPoints[i] = new DataPoint(
                        // / If the setting tells it to be in hours, divide by 3600
                        (setting.isinHours())?
                                (double)times[i+firstPointDrawn]/3600:
                                times[i+firstPointDrawn], accels[i+firstPointDrawn]);
            }
            // Set the graph title
            titleString = R.string.at_graph_label;
            axisLabel = R.string.acceleration_axis_label;
        } else {
            // Get all the velocities
            double[] velocities = dataSet.getSpeeds();
            // Add the relevant velocities to the "to be drawn" array
            for (int i = 0; i < numPointsToDraw; i++) {
                freshPoints[i] = new DataPoint(
                        // If the setting tells it to be in hours, divide by 3600
                        (setting.isinHours())?
                                (double)times[i+firstPointDrawn]/3600:
                                times[i+firstPointDrawn],
                        // If the setting tells it to be in km/h, multiply by 3.6
                        (setting.isInKMPerH())?
                                velocities[i+firstPointDrawn]*3.6:
                                velocities[i+firstPointDrawn]);
            }
            // Set the graph title
            titleString = R.string.vt_graph_label;
            axisLabel = (setting.isInKMPerH())?
                R.string.velocity_axis_label_kmh:
                R.string.velocity_axis_label_ms;
        }

        // swap out the old DataPoints with the new ones
        points.resetData(freshPoints);
        // if there is data, stretch graph to fit it
        if (times.length-1>0) {
            // If the setting is on hours, stretch graph axises accordingly
            graphView.getViewport().setMinX((times[times.length-1] - GRAPH_TIME_RANGE)
                    /((setting.isinHours())?3600:1));
            graphView.getViewport().setMaxX(times[times.length-1]
                    /((setting.isinHours())?3600:1));
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
                        df.format(dataSet.getMaxSpeed()) + " m/s");
                // Set max acceleration
                maxAccelerationView.setText(getString(R.string.max_acceleration) + ":\t\t\t" +
                        df.format(dataSet.getMaxAcceleration()) + " m/s" + '\u00B2');
                // Set time elapsed
                timeElapsedView.setText(getString(R.string.time_elapsed) + ":\t\t\t\t\t\t\t\t" +
                        df.format(dataSet.getTimeElapsedSeconds()) + " s");
                yAxisView.setText(axisLabel);
                xAxisView.setText((setting.isinHours())?
                        R.string.time_axis_label_h:
                        R.string.time_axis_label_s);
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

        int sensorRefresh;

        // Set the refresh rate based on the settings
        switch (setting.getRefreshRate()){
            case SLOW:
                sensorRefresh = SensorManager.SENSOR_DELAY_UI;
                break;
            case MEDIUM:
                sensorRefresh = SensorManager.SENSOR_DELAY_NORMAL;
                break;
            case FAST:
                sensorRefresh = SensorManager.SENSOR_DELAY_FASTEST;
                break;
            default:
                sensorRefresh = SensorManager.SENSOR_DELAY_NORMAL;
        }

        sensorManager.registerListener(this, accel, sensorRefresh);
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

        // This code is directly taken from the Android website.
        // I used it since the linear accelerometer doesn't seem to work correctly.
        // Without borrowing this code, I would have had no idea how to do this.
        // https://developer.android.com/guide/topics/sensors/sensors_motion.html

        final float alpha = 0.8F;

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // don't include first 10 pieces of data to make sure sensor is properly adjusted
        if(accuracyCounter>=0) {
            accuracyCounter--;
        } else {
            // Add the information to the dataset.
            dataSet.establishNextDataPoint(
                    event.values[0] - gravity[0],
                    event.values[1] - gravity[1],
                    event.values[2] - gravity[2]);
        }
    }

    // Don't do anythings when accuracy changes; there isn't anything to do
    // (needed because this implements SensorEventListener)
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
