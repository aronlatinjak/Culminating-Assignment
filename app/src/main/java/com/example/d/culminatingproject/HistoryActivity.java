package com.example.d.culminatingproject;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class HistoryActivity extends AppCompatActivity {


    DataSet[] pastRecordings;

    // GUI elements
    private ListView listView;
    private ImageButton searchButton;
    private ImageButton deleteButton;
    private ImageButton downloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Puts a back button in the top bar
        ActionBar actionBar = getSupportActionBar();
        try {
            actionBar.setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            System.err.println("Something went wrong.\n" + e.getMessage());
        }

        // Access the buttons
        searchButton = (ImageButton) findViewById(R.id.ibSearch);
        deleteButton = (ImageButton) findViewById(R.id.ibDelete);
        downloadButton = (ImageButton) findViewById(R.id.ibDownload);

        // Functionality for search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        // Access the ListView
        listView = (ListView) findViewById(R.id.listView);

        // Get all the past recordings
        pastRecordings = SaveStaticClass.readSaves(getApplicationContext());

        String[] stuff = {"Recording 1", "Recording 2", "Recording 3", "Recording 4"};

        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stuff);

        listView.setAdapter(listAdapter);

        // When a list element is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Send them to the statistics view, but pass the existing DataSet and don't continue recording
                String /*Set to DataSet later*/ toPass = String.valueOf(parent.getItemAtPosition(position));
                String output = "You clicked element #" + position + ", "+ toPass;

                // Makes the alert box appear
                Toast.makeText(HistoryActivity.this, output, Toast.LENGTH_LONG).show();
            }
        });


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

}
