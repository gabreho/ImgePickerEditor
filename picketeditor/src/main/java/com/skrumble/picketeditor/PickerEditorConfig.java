package com.skrumble.picketeditor;

import android.app.Activity;
import android.util.DisplayMetrics;

public class PickerEditorConfig {

    private static boolean CONFIG_COMPRESS = false;
    private static boolean CONFIG_VIDEO_TRIMMING_FEATURE = false;

    public static int GRID_SPAN_COUNT = 3;

    public static int HEIGHT, WIDTH;

    public static int MAX_VIDEO_RECORDING_LENGTH = 60000;

    public static void updateScreenSize(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        HEIGHT = displayMetrics.heightPixels;
        WIDTH = displayMetrics.widthPixels;
    }

    public static boolean isConfigCompress() {
        return CONFIG_COMPRESS;
    }

    public static void setConfigCompress(boolean configCompress) {
        CONFIG_COMPRESS = configCompress;
    }

    public static boolean isConfigVideoTrimmingFeature() {
        return CONFIG_VIDEO_TRIMMING_FEATURE;
    }

    public static void setConfigVideoTrimmingFeature(boolean configVideoTrimmingFeature) {
        CONFIG_VIDEO_TRIMMING_FEATURE = configVideoTrimmingFeature;
    }
}
