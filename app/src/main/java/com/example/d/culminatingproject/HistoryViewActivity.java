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
 * Created by D on 2017-01-25.
 */

public class HistoryViewActivity extends AppCompatActivity {

    private LineGraphSeries<DataPoint> points;
    private DataSet dataSet;
    private Setting setting;

    // parts of the GUI
    private Switch velAccelSwitch;
    private GraphView graphView;
    private TextView graphTitle;
    private TextView maxVelocityView;
    private TextView maxAccelerationView;
    private TextView timeElapsedView;
    private TextView axisView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historyview);

        // Puts a back button in the top bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar !=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        dataSet = getIntent().getParcelableExtra("data_set");

        // Initialize the textviews
        graphTitle = (TextView) findViewById(R.id.tvGraphTitle);
        maxVelocityView = (TextView) findViewById(R.id.tvMaxVelocity);
        maxAccelerationView = (TextView) findViewById(R.id.tvMaxAccel);
        timeElapsedView = (TextView) findViewById(R.id.tvTime);
        axisView = (TextView) findViewById(R.id.tvGraphY);

        // Initialize the points to draw on the graph
        points = new LineGraphSeries<>(new DataPoint[]{new DataPoint(0,0)});

        // Get the graphView
        graphView = (GraphView) findViewById(R.id.data_graph);
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
        // String for the y-axis of graph
        final int axisLabel;

        // if the switch is to the right
        if(velAccelSwitch.isChecked()) {
            // Get all the accelerations
            double[] accels = dataSet.getAccelerations();
            // Add the relevant accelerations to the "to be drawn" array
            for (int i = 0; i < times.length; i++) {
                freshPoints[i] = new DataPoint(times[i], accels[i]);
            }
            // Set the graph title
            titleString = R.string.at_graph_label;
            axisLabel = R.string.acceleration_axis_label;
        } else {
            // Get all the velocities
            double[] velocities = dataSet.getSpeeds();
            // Add the relevant velocities to the "to be drawn" array
            for (int i = 0; i < times.length; i++) {
                freshPoints[i] = new DataPoint(times[i], velocities[i]);
            }
            // Set the graph title
            titleString = R.string.vt_graph_label;
            axisLabel = R.string.velocity_axis_label;
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
                axisView.setText(axisLabel);
            }
        });

    }

}
