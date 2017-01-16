package com.example.d.culminatingproject;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

/**
 * Created by D on 2017-01-16.
 */
public class Graph {

    private double[] ys;
    private long[] xs;

    public void Graph(long[] xPoints, double[] yPoints) {
        xs = xPoints;
        ys = yPoints;
    }

    public Drawable getImage() {

        Bitmap bi = Bitmap.createBitmap(1500, 500, Bitmap.Config.ARGB_8888);

        return new BitmapDrawable(bi);

    }

}
