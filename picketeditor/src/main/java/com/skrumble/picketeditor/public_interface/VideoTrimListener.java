package com.skrumble.picketeditor.public_interface;

public interface VideoTrimListener {
    void onStartTrim();
    void onFinishTrim(String url);
    void onCancel();
}
