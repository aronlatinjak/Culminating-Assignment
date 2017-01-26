package com.example.d.culminatingproject;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.Switch;

import static com.example.d.culminatingproject.Setting.RefreshRate.FAST;
import static com.example.d.culminatingproject.Setting.RefreshRate.MEDIUM;
import static com.example.d.culminatingproject.Setting.RefreshRate.SLOW;

/**
 * Created by Madison on 1/15/2017.
 * Used to change the recording/display settings for the application.
 */
public class SettingsActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    Switch velocitySwitch;
    Switch timeSwitch;
    Setting s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Puts a back button in the top bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar !=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        radioGroup = (RadioGroup) findViewById(R.id.refreshRadioGroup);
        velocitySwitch = (Switch) findViewById(R.id.switchVelocity);
        timeSwitch = (Switch) findViewById(R.id.switchTime);

        s = SaveStaticClass.readSettings(getApplicationContext());

        // Update the sliders to what is specified in the current settings
        velocitySwitch.setChecked(s.isInKMPerH());
        timeSwitch.setChecked(s.isInHours());

        // Update the radio buttons to what is specified in the current settings
        switch(s.getRefreshRate()) {
            case SLOW:
                radioGroup.check(R.id.radioSlow);
                break;
            case MEDIUM:
                radioGroup.check(R.id.radioMedium);
                break;
            case FAST:
                radioGroup.check(R.id.radioFast);
        }

    }

    /**
     * Adds functionality to the back button on the nav bar, as well as saving data when back is
     * pressed.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                saveSettings();
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Changes the functionality of the back button. Saves the settings first, then navigates up.
     * pre: switches and radio group initialized
     * post: settings saved, activity quit
     */
    @Override
    public void onBackPressed() {
        saveSettings();
        super.onBackPressed();
    }

    /**
     * Saves the settings according to the state of the buttons and switches of this view.
     * pre: switches and radio group initialized
     * post: settings saved
     */
    public void saveSettings() {
        Setting.RefreshRate newRate;

        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioSlow:
                newRate = SLOW;
                break;
            case R.id.radioMedium:
                newRate = MEDIUM;
                break;
            case R.id.radioFast:
                newRate = FAST;
                break;
            default:
                newRate = MEDIUM;
                break;
        }

        s = new Setting(newRate, velocitySwitch.isChecked(),
                timeSwitch.isChecked());

        SaveStaticClass.setSettings(s, getApplicationContext());
    }

}
