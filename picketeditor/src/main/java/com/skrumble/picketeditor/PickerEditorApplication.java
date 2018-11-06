package com.skrumble.picketeditor;

import android.app.Application;
import android.content.Context;
import android.util.TypedValue;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.text.DecimalFormat;

public class PickerEditorApplication extends Application {

    public static PickerEditorApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        try {
            FFmpeg.getInstance(this).loadBinary(new LoadBinaryResponseHandler() {
                @Override public void onFailure() {
                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public static int convertDpToPixels(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, PickerEditorApplication.sInstance.getResources().getDisplayMetrics());
    }

    public static String convertSecondsToTime(long seconds) {
        DecimalFormat formatter = new DecimalFormat("00");
        String timeStr;
        int hour;
        int minute;
        int second;
        if (seconds <= 0)
            return "00:00";
        else {
            minute = (int)seconds / 60;
            if (minute < 60) {
                second = (int)seconds % 60;
                timeStr = formatter.format(minute) + ":" + formatter.format(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = (int)(seconds - hour * 3600 - minute * 60);
                timeStr = formatter.format(hour) + ":" + formatter.format(minute) + ":" + formatter.format(second);
            }
        }
        return timeStr;
    }
}
