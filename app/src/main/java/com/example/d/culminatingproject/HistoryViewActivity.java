package com.example.d.culminatingproject;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.*;
import com.jjoe64.graphview.series.DataPoint;

import java.text.DecimalFormat;

/**
 * Used to view a recording that has already been finished.
 * Created by D on 2017-01-25.
 */

public class HistoryViewActivity extends AppCompatActivity {

    private LineGraphSeries<DataPoint> points;
    private DataSet dataSet;
    private Setting setting;

    // Parts of the GUI
    private Switch velAccelSwitch;
    private GraphView graphView;
    private TextView graphTitle;
    private TextView maxVelocityView;
    private TextView maxAccelerationView;
    private TextView timeElapsedView;
    private TextView xAxisView;
    private TextView yAxisView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historyview);

        // Puts a back button in the top bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar !=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Read the DataSet from the intent that created this activity
        dataSet = getIntent().getParcelableExtra("data_set");

        // Read the settings
        setting = SaveStaticClass.readSettings(getApplicationContext());

        // Initialize the text views
        graphTitle = (TextView) findViewById(R.id.tvGraphTitle);
        maxVelocityView = (TextView) findViewById(R.id.tvMaxVelocity);
        maxAccelerationView = (TextView) findViewById(R.id.tvMaxAccel);
        timeElapsedView = (TextView) findViewById(R.id.tvTime);
        yAxisView = (TextView) findViewById(R.id.tvGraphY);
        xAxisView = (TextView) findViewById(R.id.tvGraphX);

        // Initialize the points to draw on the graph (immediately rewritten, so the numbers
        // themselves here don't really matter)
        points = new LineGraphSeries<>(new DataPoint[]{new DataPoint(0,0)});

        // Get the Graph View and set which points it displays
        graphView = (GraphView) findViewById(R.id.data_graph);
        graphView.addSeries(points);
        // Get the velocity/acceleration switch
        velAccelSwitch = (Switch) findViewById(R.id.switchVelocityPosition);
        // When the switch is clicked, refresh the graph
        velAccelSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                refreshGraph();
            }
        });

        refreshGraph();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Draws the graph with the information from the DataSet
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
        // String for the y-axis of graph
        final int axisLabel;

        // if the switch is to the right
        if(velAccelSwitch.isChecked()) {
            // Get all the accelerations
            double[] accels = dataSet.getAccelerations();
            // Add the relevant accelerations to the "to be drawn" array
            for (int i = 0; i < times.length; i++) {
                freshPoints[i] = new DataPoint(
                        // / If the setting tells it to be in hours, divide by 3600
                        (setting.isinHours())?
                                times[i]/3600:
                                times[i], accels[i]);
            }
            // Set the graph title
            titleString = R.string.at_graph_label;
            axisLabel = R.string.acceleration_axis_label;
        } else {
            // Get all the velocities
            double[] velocities = dataSet.getSpeeds();
            // Add the relevant velocities to the "to be drawn" array
            for (int i = 0; i < times.length; i++) {
                freshPoints[i] = new DataPoint(
                        // If the setting tells it to be in hours, divide by 3600
                        (setting.isinHours())?
                                times[i]/3600:
                                times[i],
                        // If the setting tells it to be in km/h, multiply by 3.6
                        (setting.isInKMPerH())?
                                velocities[i]*3.6:
                                velocities[i]);
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
            graphView.getViewport().setMinX(0);
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

}
