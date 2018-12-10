package com.skrumble.picketeditor;

import android.app.Activity;
import android.util.DisplayMetrics;

public class Config {

    public static int GRID_SPAN_COUNT = 3;

    public static int HEIGHT, WIDTH;

    public static int MAX_VIDEO_RECORDING_LENGTH = 60000;

    public static void updateScreenSize(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        HEIGHT = displayMetrics.heightPixels;
        WIDTH = displayMetrics.widthPixels;
    }
}
