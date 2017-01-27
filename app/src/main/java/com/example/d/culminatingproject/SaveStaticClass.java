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
            oos.writeObject(dataSets);
            oos.close();
            Toast.makeText(context, "Data saved", Toast.LENGTH_LONG).show();
        } catch (IOException ioe) {
            Toast.makeText(context, "Data failed to save", Toast.LENGTH_LONG).show();
            System.err.println("Data failed to save: " + ioe.getLocalizedMessage());
        }
    }

    /**
     * Reads the internally saved data sets.
     * @return
     */
    public static DataSet[] readSaves(Context context) {

        DataSet[] data = null;
        ArrayList<DataSet> dataSets = new ArrayList<>();

        try {

            File dataFile = new File(context.getFilesDir(), SAVE_FILE_NAME);

            if (dataFile.exists()){

                FileInputStream fis = new FileInputStream(dataFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                while(ois.available() > 0) {
                    dataSets.add((DataSet) ois.readObject());
                }
                System.out.println("Number of elements read from file: " + dataSets.size());
            } else {
                Toast.makeText(context, "derp, there is no file", Toast.LENGTH_LONG).show();
            }
        } catch (IOException ioe) {
            System.err.println("File reading failed: " + ioe.getLocalizedMessage());
            Toast.makeText(context, "File reading failed", Toast.LENGTH_LONG)
                    .show();
        } catch (ClassNotFoundException cnfe) {
            System.err.println("File reading failed: " + cnfe.getLocalizedMessage());
        }

        if (!dataSets.isEmpty()) {
            data = new DataSet[dataSets.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = dataSets.get(i);
            }
            System.out.println("Number of items detected in file: " + data.length);
        } else {
            System.out.println("Number of items detected in file: 0");
        }

        return(data);
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
