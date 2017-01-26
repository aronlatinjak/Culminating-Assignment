package com.example.d.culminatingproject;

import android.content.Context;
import android.widget.Toast;

import java.io.*;
import java.util.*;


/**
 * Created by Aron on 2017-01-22.
 */

public class SaveStaticClass implements Serializable {

    /**
     * Dummy method for saving
     * @param ds the DataSet to save
     * @param context the application context
     */
    public static void setSettings(DataSet ds, Context context) {
        File dataFile = new File(context.getFilesDir(), "DataSet.txt");
        try {
            dataFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(dataFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(ds);
            Toast.makeText(context, "Data saved", Toast.LENGTH_LONG).show();
        } catch (IOException ioe) {
            Toast.makeText(context, "Data failed to save", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Dummy method for reading saves
     * @return
     */
    public static DataSet readSaves(Context context) {

        DataSet dataSet;

        try {
            File dataFile = new File(context.getExternalFilesDir(null), "DataSaved");

            if (dataFile.exists()){

                FileInputStream fis = new FileInputStream(dataFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                dataSet = (DataSet) ois.readObject();

                ois.read();
            }
            else {

                dataSet = new DataSet();
                dataFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(dataFile);

                ObjectOutputStream ois = new ObjectOutputStream(fos);
                ois.writeObject(dataSet);
            }
        }
        catch(IOException | ClassNotFoundException ioe){

            System.err.println(ioe.getMessage());
            dataSet = new DataSet();
        }

        return(dataSet);
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
