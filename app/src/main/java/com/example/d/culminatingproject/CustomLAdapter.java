package com.example.d.culminatingproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Extends the array adapter to allow a ListView to be created with a DataSet array.
 * Adds the functionality of the individual items in the list, such as the text and the buttons.
 * Created by D on 2017-01-23.
 */

class CustomLAdapter extends ArrayAdapter<DataSet> {

    private DataSet[] data;
    private boolean[] isCheckedArray;

    CustomLAdapter(Context context, DataSet[] dataSets) {
        super(context, R.layout.listviewrow_custom, dataSets);
        data = dataSets;

        // Keeps track of the state of the check boxes
        isCheckedArray = new boolean[dataSets.length];
        for (boolean b: isCheckedArray) b = false;
    }

    /**
     * Set up the functionality of the list items
     * @param position position of list item in list
     * @param convertView don't know what this does
     * @param parent parent ViewGroup
     * @return the view for the list element
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.listviewrow_custom, parent, false);

        final int accessiblePosition = position;

        TextView nameView = (TextView) customView.findViewById(R.id.itemNameTextView);
        nameView.setText((data[position].getTimestamp()).toString());
        CheckBox checkBox = (CheckBox) customView.findViewById(R.id.itemCheckbox);

        // When list element is clicked
        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                String output = "You clicked element #" + accessiblePosition;

                // Makes the alert box appear
                Toast.makeText(getContext(), output, Toast.LENGTH_LONG).show();
                */
                // Send the reader to a new activity with the data present
                Intent recordingIntent = new Intent(getContext(), HistoryViewActivity.class);
                recordingIntent.putExtra("data_set", data[accessiblePosition]);
                recordingIntent.putExtra("came_from_recording", false);
                getContext().startActivity(recordingIntent);

            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedArray[accessiblePosition] = isChecked;
            }
        });

        return customView;
    }

    public boolean getIsChecked(int position) {
        return isCheckedArray[position];
    }

}
