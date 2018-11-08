package com.skrumble.picketeditor;

import android.app.Application;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class PickerEditorApplication extends Application {

    public static PickerEditorApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;


    }
}