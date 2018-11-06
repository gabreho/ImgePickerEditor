package com.skrumble.picketeditor.editor.video;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.skrumble.picketeditor.R;
import com.skrumble.picketeditor.editor.video.compress.VideoCompressor;
import com.skrumble.picketeditor.editor.video.public_interface.VideoCompressListener;
import com.skrumble.picketeditor.editor.video.public_interface.VideoTrimListener;
import com.skrumble.picketeditor.editor.video.widget.VideoTrimmerView;

public class VideoTrimmerActivity extends AppCompatActivity implements VideoTrimListener {

    private static final String VIDEO_PATH_KEY = "video-file-path";

    public static final int VIDEO_TRIM_REQUEST_CODE = 0x001;
    private ProgressDialog mProgressDialog;
    private VideoTrimmerView trimmerView;

//    public static void call(FragmentActivity from, String videoPath) {
//        if (!TextUtils.isEmpty(videoPath)) {
//            Bundle bundle = new Bundle();
//            bundle.putString(VIDEO_PATH_KEY, videoPath);
//            Intent intent = new Intent(from, VideoTrimmerActivity.class);
//            intent.putExtras(bundle);
//            from.startActivityForResult(intent, VIDEO_TRIM_REQUEST_CODE);
//        }
//    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_video_trimmer);
        trimmerView = findViewById(R.id.trimmer_view);

        Bundle bd = getIntent().getExtras();

        String path = "";

        if (bd != null) path = bd.getString(VIDEO_PATH_KEY);
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
        //TODO: please handle your trimmed video url here!!!
        String out = "/storage/emulated/0/Android/data/com.iknow.android/cache/compress.mp4";
        buildDialog(getResources().getString(R.string.compressing)).show();
        VideoCompressor.compress(this, in, out, new VideoCompressListener() {
            @Override
            public void onSuccess(String message) {
            }

            @Override
            public void onFailure(String message) {
            }

            @Override
            public void onFinish() {
                if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
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
