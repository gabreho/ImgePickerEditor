package com.skrumble.imagepicketeditor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.skrumble.picketeditor.PickerEditor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    public void OnTextViewClick(View view) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void OnVideoPickerClick(View view) {
        PickerEditor.openVideoGallery(this, 1);
    }

    public void OnImagePickClick(View view) {
        PickerEditor.openPictureGallery(this, 1);
    }

    public void onCameraClick(View view) {
        PickerEditor.startCamera(this, 1);
    }

    public void onImageAndVideoClick(View view) {
        PickerEditor.openPictureAndVideoGallery(this, 1);
    }
}
