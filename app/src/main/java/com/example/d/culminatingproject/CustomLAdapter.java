package com.example.d.culminatingproject;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
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

    /**
     * Create a new adapter, given a set of data sets
     * @param context The application context
     * @param dataSets The data sets to use in the list
     */
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
        // Make a view for the list element
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.listviewrow_custom, parent, false);

        final int accessiblePosition = position;

        // Get the GUI elements of this view
        TextView nameView = (TextView) customView.findViewById(R.id.itemNameTextView);
        nameView.setText((data[position].getTimestamp()).toString());
        CheckBox checkBox = (CheckBox) customView.findViewById(R.id.itemCheckbox);

        // When list element is clicked
        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Send the reader to a new activity with the data present
                Intent recordingIntent = new Intent(getContext(), HistoryViewActivity.class);
                recordingIntent.putExtra("data_set", (Parcelable) data[accessiblePosition]);
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

    /**
     * Find if a certain checkbox is currently checked.
     * @param position Position of the checkbox in the list.
     * @return True if the box is check, false otherwise.
     */
    public boolean getIsChecked(int position) {
        return isCheckedArray[position];
    }

}
