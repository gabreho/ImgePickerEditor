package com.skrumble.imagepicketeditor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.skrumble.picketeditor.PickerEditor;
import com.skrumble.picketeditor.picker.interfaces.WorkFinish;
import com.skrumble.picketeditor.picker.utility.PermUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void OnVideoPickerClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermUtil.checkForCamaraWritePermissions(this, new WorkFinish() {
                @Override
                public void onWorkFinish(Boolean check) {
                    PickerEditor.openVideoGallery(MainActivity.this, 1);
                }
            });
            return;
        }

        PickerEditor.openVideoGallery(MainActivity.this, 1);
    }

    public void OnImagePickClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermUtil.checkForCamaraWritePermissions(this, new WorkFinish() {

                @Override
                public void onWorkFinish(Boolean check) {
                    PickerEditor.openPictureGallery(MainActivity.this, 1);
                }
            });
            return;
        }
        PickerEditor.openPictureGallery(MainActivity.this, 1);
    }

    public void onCameraClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermUtil.checkForCamaraWritePermissions(this, new WorkFinish() {

                @Override
                public void onWorkFinish(Boolean check) {
                    PickerEditor.startCamera(MainActivity.this, 1);
                }
            });
            return;
        }
        PickerEditor.startCamera(MainActivity.this, 1);
    }

    public void onImageAndVideoClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermUtil.checkForCamaraWritePermissions(this, new WorkFinish() {
                @Override
                public void onWorkFinish(Boolean check) {
                    PickerEditor.openPictureAndVideoGallery(MainActivity.this, 1);
                }
            });
            return;
        }

        PickerEditor.openPictureAndVideoGallery(MainActivity.this, 1);
    }

    public void onRecordVideoClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermUtil.checkForCamaraWritePermissions(this, new WorkFinish() {
                @Override
                public void onWorkFinish(Boolean check) {
                    PickerEditor.startCameraForVideo(MainActivity.this, 1);
                }
            });

            return;
        }

        PickerEditor.startCameraForVideo(MainActivity.this, 1);
    }
}
