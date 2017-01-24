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
        timeSwitch = null;

        s = SaveStaticClass.readSettings(getApplicationContext());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

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

                // TODO: Save settings

                SaveStaticClass.setSettings(s, getApplicationContext());

                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
