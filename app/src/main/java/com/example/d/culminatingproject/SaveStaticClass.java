package com.example.d.culminatingproject;

import android.content.Context;
import android.widget.Toast;

import java.io.*;
import java.util.*;


/**
 * Created by Aron on 2017-01-22.
 * hi
 */

public class SaveStaticClass {

    // This is the name of the save file for the DataSets
    public static final String SAVE_FILE_NAME = "DataSaved";
    // This is the name of the save file for the settings
    public static final String SETTINGS_FILE_NAME = "Settings.txt";

     /**
     * Saves the provided sets of data.
     * pre: none
     * post: sets of data updated in internal storage.
     * @param dataSets the DataSet to save.
     * @param context the application context.
     */
    public static void writeSaves(DataSet[] dataSets, Context context) {
        // Get the saves file
        File dataFile = new File(context.getFilesDir(), SAVE_FILE_NAME);
        try {
            // Delete the current one if it already exists
            if (dataFile.exists()) {
                dataFile.delete();
            }

            // Open an object output stream to write the DataSet objects
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile));
            // Write all the objects
            for (DataSet dataSet:
                 dataSets) {
                oos.writeObject(dataSet);
            }
            oos.close();
            // Indicate to the user that the data has successfully updated
            Toast.makeText(context, "Data updated", Toast.LENGTH_SHORT).show();
        } catch (IOException ioe) {
            // Alert the user if the data fails to save
            Toast.makeText(context, "Data failed to save", Toast.LENGTH_LONG).show();
            System.err.println("Data failed to save: " + ioe.getLocalizedMessage());
        }
    }

    /**
     * Reads the saves from the file and returns them as an array of DataSets.
     * @param context The application context.
     * @return An array of all the past <tt>DataSet</tt>s.
     */
    public static DataSet[] readSaves(Context context) {
        // Get the saves file
        File file = new File(context.getFilesDir(), SAVE_FILE_NAME);
        // Holds the read files
        ArrayList<Object> readObjects = new ArrayList<>();
        // If the data can't be read, return this null array
        DataSet[] dataSets = null;

        try {
            // If the file exists
            if (file.exists()) {
                // Set up an object reading stream
                ObjectInputStream ois = new ObjectInputStream(
                        new FileInputStream(file));
                Object current;

                // Read the objects from file until the end of file is reached
                try {
                    while ((current = ois.readObject()) != null) {
                        readObjects.add(current);
                    }
                } catch (EOFException eofe) {
                    ois.close();
                }

                // Make an array version of the array list of objects
                dataSets = new DataSet[readObjects.size()];
                for (int i = 0; i < readObjects.size(); i++) {
                    try {
                        dataSets[i] = (DataSet) readObjects.get(i);
                    } catch (ClassCastException cce) {
                        // If something is in the file that shouldn't be, delete everything in the
                        // file!!!
                        file.delete();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException iocnfe) {
            // If the file can't be read, alert the user
            System.err.println("Something went wrong: " + iocnfe.toString());
            Toast.makeText(context, "Failed to load saved recordings", Toast.LENGTH_SHORT).show();
        }
        return dataSets;
    }

    /**
     * Save a single data set. Used after a recording has been taken.
     * @param dataSet The data set to save.
     * @param context The application context.
     */
    public static void saveDataSet(DataSet dataSet, Context context) {
        // Read the current saves
        DataSet[] oldData = readSaves(context);
        // "to be written" array
        DataSet[] newData;

        // If there actually is previous data, add it to the "to be written" array
        if (oldData != null && oldData.length > 0) {
            newData = new DataSet[oldData.length + 1];
            for (int i = 0; i < oldData.length; i++) {
                newData[i] = oldData[i];
            }
            // Add the new data at the end
            newData[newData.length - 1] = dataSet;
        } else {
            // If there isn't previous data, only write the new data
            newData = new DataSet[]{dataSet};
        }

        // Use write saves to update the data in the internal file
        writeSaves(newData, context);

    }

    /**
     * Dummy method for exporting a recording to downloads folder as a .csv file.
     * @param context The application context.
     * @param dataSet The data set to export.
     */
    public static void exportAsCsv(Context context, DataSet dataSet) {
        // TODO: implement me!
    }

    /**
     * Save a new set of settings.
     * @param context The application context.
     * @param s The setting to save.
     */
    public static void setSettings(Setting s, Context context) {
        // Get the settings file
        File settingsFile = new File(context.getFilesDir(), SETTINGS_FILE_NAME);
        try {
            // Overwrite the file with the new data
            if (settingsFile.exists()) {
                settingsFile.delete();
            }
            settingsFile.createNewFile();

            // Open an object output stream to write the settings object
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(settingsFile));
            oos.writeObject(s);
            // Alert the user that the setting successfully saved
            Toast.makeText(context, "Settings saved", Toast.LENGTH_LONG).show();
        } catch (IOException ioe) {
            // Alter the user that something went wrong, and that the settings didn't save
            Toast.makeText(context, "Settings failed to save", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Returns the settings from the settings file.
     * @param context The application context.
     * @return The <tt>Setting</tt> stored in file, or a blank setting if the setting couldn't
     * be read.
     */
    public static Setting readSettings(Context context) {

        Setting setting;

        try {
            // Get the settings file
            File settingsFile = new File(context.getFilesDir(), SETTINGS_FILE_NAME);

            if(settingsFile.exists()) {
                // Open an object input stream to read the current setting
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(settingsFile));
                setting = (Setting) ois.readObject();
            } else {
                // Create the file
                setting = new Setting();
                settingsFile.createNewFile();
                // Add a blank setting to the file
                FileOutputStream fos = new FileOutputStream(settingsFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(setting);
            }
        } catch (IOException | ClassNotFoundException ioe){
            // The file couldn't be created. Use a default setting
            System.err.println(ioe.getMessage());
            setting = new Setting();
        }

        return setting;

    }
}
