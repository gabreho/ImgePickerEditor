package com.skrumble.picketeditor.editor.video.public_interface;

public interface VideoTrimListener {
    void onStartTrim();
    void onFinishTrim(String url);
    void onCancel();
}
