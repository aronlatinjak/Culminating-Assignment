package com.example.d.culminatingproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * The main screen for the app, that is shown when the app starts up.
 */
public class MainMenuActivity extends AppCompatActivity {

    ImageView imageView;
    // For the easter egg
    private short counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Find the buttons and set up objects for them
        Button startRecordingButton = (Button) findViewById(R.id.buttonStart);
        Button historyButton = (Button) findViewById(R.id.buttonHistory);
        Button helpButton = (Button) findViewById(R.id.buttonHelp);
        Button settingsButton = (Button) findViewById(R.id.buttonSettings);

        counter = 0;

        // Find the image view for the easter egg
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter == 41) {
                    // Set the image to the easter egg image after it has been clicked 42 times
                    imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.meaning));
                } else if (counter < 41) {
                    counter++;
                }
            }
        });

    }

    /**
     * Send the user to their application launcher if they press back
     */
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
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
