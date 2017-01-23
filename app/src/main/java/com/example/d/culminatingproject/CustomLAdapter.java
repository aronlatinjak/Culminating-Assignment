package com.example.d.culminatingproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by D on 2017-01-23.
 */

public class CustomLAdapter extends ArrayAdapter<DataSet> {

    DataSet[] data;

    public CustomLAdapter(Context context, DataSet[] dataSets) {
        super(context, R.layout.listviewrow_custom, dataSets);
        data = dataSets;
    }

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
                String output = "You clicked element #" + accessiblePosition;

                // Makes the alert box appear
                Toast.makeText(getContext(), output, Toast.LENGTH_LONG).show();
            }
        });

        return customView;
    }

}
