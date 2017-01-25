package com.example.d.culminatingproject;

import android.content.Context;
import android.widget.Toast;

import java.io.*;
import java.util.*;


/**
 * Created by Aron on 2017-01-22.
 */

public class SaveStaticClass {

    /**
     * Dummy method for saving
     * @param ds the DataSet to save
     * @param context the application context
     */
    public static void save (DataSet ds, Context context, ArrayList dataPoints) throws IOException {

            PrintWriter pw = null;
            FileOutputStream fo = null;
            File file = null;
            try {
                file = new File(context.getFilesDir(), "DataPoints.txt");
                pw = new PrintWriter(new FileOutputStream(file));
                fo = new FileOutputStream(file);
                int datList = dataPoints.size();
                for (int i = 0; i < datList; i++) {
                    pw.write(dataPoints.get(i).toString() + "\n");
                }
            }

            finally {
                pw.flush();
                pw.close();
                fo.close();
            }
    }

    /**
     * Dummy method for reading saves
     * @return
     */
    public static DataSet[] readSaves(Context context) {
        return null;
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
                // The the Setting object from the file
                ObjectInputStream ois = new ObjectInputStream(fileInputStream);
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
            // Beats me...
            System.err.println(ioe.getMessage());
            setting = new Setting();
        }

        return setting;

    }

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

}
