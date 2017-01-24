package com.example.d.culminatingproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Date;

/**
 * The activity for viewing, deleting, and downloading past data.
 */
public class HistoryActivity extends AppCompatActivity {


    DataSet[] pastRecordings;

    // The all-essential ListAdapter
    CustomLAdapter listAdapter;

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
        if (actionBar !=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
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
                if(getCurrentFocus()!=null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        });



        // Access the ListView
        listView = (ListView) findViewById(R.id.listView);

        // Load the ♪ Lizst ♪ (note: actually loads the list, not the composer Lizst) //
        reloadListItems();

        // Set up the delete button; It should delete all the recordings with check boxes beside them
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // For now, just count the number of items to delete
                int numToDeleteCounter = 0;
                for (int i = 0; i < pastRecordings.length; i++) {
                    if (listAdapter.getIsChecked(i)) numToDeleteCounter++;
                }

                final int numToDelete = numToDeleteCounter;

                AlertDialog.Builder aDB = new AlertDialog.Builder(HistoryActivity.this);

                // Set the message for the alert box (all the fancy question marks are just to
                // adjust grammar to if there is one ore multiple items to delete)
                aDB.setMessage("Are you sure that you would like to delete " + (numToDelete>1?"these " +
                        numToDelete + " recordings?":"this recording?"));

                // If yes is selected in the alert box
                aDB.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Alert the user that the recordings are being deleted
                        Toast.makeText(getApplicationContext(), "Deleting " + numToDelete +
                                " recordings...", Toast.LENGTH_LONG).show();

                        // Reload the list of items
                        reloadListItems();
                    }
                });

                // If no is selected in the alert box
                aDB.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });

                // Send the alert box to the screen
                AlertDialog alertDialog = aDB.create();
                alertDialog.show();

            }
        });

        // Set up the download button; it will download all the recordings selected as a .csv file
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // For now, just count the number of recordings to download
                int numToDownloadCounter = 0;
                for (int i = 0; i < pastRecordings.length; i++) {
                    if (listAdapter.getIsChecked(i)) numToDownloadCounter++;
                }

                final int numToDownload = numToDownloadCounter;

                AlertDialog.Builder aDB = new AlertDialog.Builder(HistoryActivity.this);

                // Set the message for the alert box
                if (numToDownload>1){
                    aDB.setMessage("Are you sure that you would like to download these " +
                            numToDownload + " recordings as .csv files?");
                } else {
                    aDB.setMessage("Are you sure that you would like to download this recording " +
                            "as a .csv file?");
                }

                // If yes is selected in the alert box
                aDB.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Alert the user that the recordings are being downloaded
                        Toast.makeText(getApplicationContext(), "Downloading " + numToDownload +
                                " recordings...", Toast.LENGTH_LONG).show();
                    }
                });

                // If no is selected in the alert box
                aDB.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });

                // Send the alert box to the screen
                AlertDialog alertDialog = aDB.create();
                alertDialog.show();
            }
        });

    }

    /**
     * For the back button.
     * @param item
     * @return
     */
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
     * Reloads the list and the items contained within. Called when the list is initially set up
     * and when items in the list are deleted.
     * pre: none
     * post: ListView refreshed
     */
    public void reloadListItems() {
        // Get all the past recordings
        pastRecordings = SaveStaticClass.readSaves(getApplicationContext());

        // Adding dummy data since SaveStaticClass isn't set up yet
        pastRecordings = new DataSet[]{
                new DataSet(new DataPoint[]{new DataPoint(0,0,0,0,0,0,0,0)}, new Date(), true),
                new DataSet(new DataPoint[]{new DataPoint(1,0,0,0,0,0,0,0)}, new Date(), true),
                new DataSet(new DataPoint[]{new DataPoint(2,0,0,0,0,0,0,0)}, new Date(), true)
        };

        // Set up the adapter that puts things in the list correctly
        listAdapter = new CustomLAdapter(this, pastRecordings);

        // Set the adapter of the list to the newly created custom adapter
        listView.setAdapter(listAdapter);
    }

}
