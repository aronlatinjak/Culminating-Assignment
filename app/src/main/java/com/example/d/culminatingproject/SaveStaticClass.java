package com.example.d.culminatingproject;

import android.content.Context;
import android.widget.Toast;

import java.io.*;
import java.util.*;


/**
 * Created by Aron on 2017-01-22.
 */

public class SaveStaticClass implements Serializable {

    public static final String SAVE_FILE_NAME = "DataSaved";

    /**
     * Saves the provided set of data.
     * @param dataSets the DataSet to save
     * @param context the application context
     */
    public static void writeSaves(DataSet[] dataSets, Context context) {
        File dataFile = new File(context.getFilesDir(), SAVE_FILE_NAME);
        try {
            if (dataFile.exists()) {
                dataFile.delete();
            }
            System.out.println("File creation successful: " + dataFile.createNewFile());
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile));
            for (DataSet dataSet:
                 dataSets) {
                oos.writeObject(dataSet);
            }
            oos.close();
            Toast.makeText(context, "Data saved", Toast.LENGTH_LONG).show();
        } catch (IOException ioe) {
            Toast.makeText(context, "Data failed to save", Toast.LENGTH_LONG).show();
            System.err.println("Data failed to save: " + ioe.getLocalizedMessage());
        }
    }

    /**
     * Reads the saves from the file and returns them as an array of DataSets
     * @param context
     * @return
     */
    public static DataSet[] readSaves(Context context) {
        File file = new File(context.getFilesDir(), SAVE_FILE_NAME);
        ArrayList<Object> readObjects = new ArrayList<>();
        DataSet[] dataSets = null;

        try {
            if (file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(
                        new FileInputStream(file));
                Object current;
                try {
                    while ((current = ois.readObject()) != null) {
                        readObjects.add(current);
                    }
                } catch (EOFException eofe) {
                    ois.close();
                }
                dataSets = new DataSet[readObjects.size()];
                for (int i = 0; i < readObjects.size(); i++) {
                    try {
                        dataSets[i] = (DataSet) readObjects.get(i);
                    } catch (ClassCastException cce) {
                        // If something is in the file that shouldn't be, delete it
                        file.delete();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException iocnfe) {
            System.err.println("Something went wrong: " + iocnfe.toString());
            Toast.makeText(context, "Failed to load saved recordings", Toast.LENGTH_SHORT).show();
        }
        return dataSets;
    }

    /**
     * Save a single data set. used after a recording has been taken.
     * @param dataSet
     * @param context
     */
    public static void saveDataSet(DataSet dataSet, Context context) {
        DataSet[] oldData = readSaves(context);
        DataSet[] newData;
        if (oldData != null && oldData.length > 0) {
            newData = new DataSet[oldData.length + 1];
            for (int i = 0; i < oldData.length; i++) {
                newData[i] = oldData[i];
            }

            newData[newData.length - 1] = dataSet;
        } else {
            newData = new DataSet[]{dataSet};
        }
        writeSaves(newData, context);

    }

    /**
     * Dummy method for exporting a recording to downloads folder as a .csv file
     * @param context
     */
    public static void exportAsCsv(Context context, DataSet dataSet) {};

    /**
     * Save a new set of settings
     */
    public static void setSettings(Setting s, Context context) {
        File settingsFile = new File(context.getFilesDir(), "Settings.txt");
        try {
            settingsFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(settingsFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(s);
            Toast.makeText(context, "Settings saved", Toast.LENGTH_LONG).show();
        } catch (IOException ioe) {
            Toast.makeText(context, "Settings failed to save", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Returns the settings from the settings file
     * @param context
     * @return
     */
    public static Setting readSettings(Context context) {

        Setting setting;

        try {
            File settingsFile = new File(context.getFilesDir(), "Settings.txt");

            if(settingsFile.exists()) {

                // Read the file
                FileInputStream fileInputStream = new FileInputStream(settingsFile);
                // The Setting object from the file
                ObjectInputStream ois = new ObjectInputStream(fileInputStream);
                setting = (Setting) ois.readObject();

                ois.read();
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
            // Beats me...
            System.err.println(ioe.getMessage());
            setting = new Setting();
        }

        return setting;

    }
}
