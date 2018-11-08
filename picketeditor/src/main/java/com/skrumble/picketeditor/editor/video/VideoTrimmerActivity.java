package com.skrumble.picketeditor.editor.video;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.skrumble.picketeditor.PickerEditor;
import com.skrumble.picketeditor.R;
import com.skrumble.picketeditor.editor.video.compress.VideoCompressor;
import com.skrumble.picketeditor.editor.video.public_interface.VideoCompressListener;
import com.skrumble.picketeditor.editor.video.public_interface.VideoTrimListener;
import com.skrumble.picketeditor.editor.video.widget.VideoTrimmerView;
import com.skrumble.picketeditor.gallery.GalleryActivity;
import com.skrumble.picketeditor.picker.utility.Utility;

import java.io.File;

public class VideoTrimmerActivity extends AppCompatActivity implements VideoTrimListener {

    public static final String EXTRA_VIDEO_SRC = "EXTRA_VIDEO_SRC";

    private ProgressDialog mProgressDialog;
    private VideoTrimmerView trimmerView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        VideoTrimmerUtil.init(this);

        setContentView(R.layout.activity_video_trimmer);
        trimmerView = findViewById(R.id.trimmer_view);

        Intent bd = getIntent();

        String path = "";

        if (bd != null) path = bd.getStringExtra(EXTRA_VIDEO_SRC);
        if (trimmerView != null) {
            trimmerView.setOnTrimVideoListener(this);
            trimmerView.initVideoByURI(Uri.parse(path));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        trimmerView.onVideoPause();
        trimmerView.setRestoreState(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        trimmerView.onDestroy();
    }

    @Override
    public void onStartTrim() {
        buildDialog(getResources().getString(R.string.trimming)).show();
    }

    @Override
    public void onFinishTrim(String in) {
        final File videoFile = Utility.getVideoFile();

        buildDialog(getResources().getString(R.string.compressing)).show();
        VideoCompressor.compress(this, in, videoFile.getAbsolutePath(), new VideoCompressListener() {
            @Override
            public void onSuccess(String message) {
            }

            @Override
            public void onFailure(String message) {
            }

            @Override
            public void onFinish() {
                if (GalleryActivity.activity != null) {
                    GalleryActivity.activity.finish();
                }

                if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra(PickerEditor.RESULT_FILE, videoFile.getAbsolutePath());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onCancel() {
        trimmerView.onDestroy();
        finish();
    }

    private ProgressDialog buildDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, "", msg);
        }
        mProgressDialog.setMessage(msg);
        return mProgressDialog;
    }
}
