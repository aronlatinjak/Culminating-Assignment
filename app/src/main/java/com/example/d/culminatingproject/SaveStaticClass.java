package com.example.d.culminatingproject;

import android.content.Context;
import java.io.*;
import java.util.*;


/**
 * Created by Aron on 2017-01-22.
 */

public class SaveStaticClass {

    /**
     * Dummy method for saving
     * @param ds
     * @param context
     */
    public static void save (DataSet ds, Context context, ArrayList dataPoints) throws IOException {

            PrintWriter pw = null;
            FileOutputStream fo = null;
            File file = null;
            try {
                file = new File("DataPoints.txt");
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

}
