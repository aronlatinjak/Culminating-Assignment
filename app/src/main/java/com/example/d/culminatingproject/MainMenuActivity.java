package com.example.d.culminatingproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Find the buttons and set up objects for them
        Button startRecordingButton = (Button) findViewById(R.id.buttonStart);
        Button historyButton = (Button) findViewById(R.id.buttonHistory);
        Button helpButton = (Button) findViewById(R.id.buttonHelp);
        Button settingsButton = (Button) findViewById(R.id.buttonSettings);

    }

    public void startRecording(View view) {
        Intent i = new Intent(this, StatisticsActivity.class);
        startActivity(i);
    }

    public void startSettings(View view) {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    public void startHistory(View view) {
        Intent i = new Intent(this, HistoryActivity.class);
        startActivity(i);
    }

    public void startHelp(View view) {
        Intent i = new Intent(this, HelpActivity.class);
        startActivity(i);
    }

}
