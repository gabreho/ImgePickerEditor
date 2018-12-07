package com.skrumble.picketeditor.data_loaders;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

public class VideoLoader extends CursorLoader {
    private static final String[] VIDEO_PROJECTION = {
            //Base File
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DATE_ADDED,
            //Video File
            MediaStore.Video.Media.DURATION
    };

    public VideoLoader(@NonNull Context context) {
        super(context);

        setProjection(VIDEO_PROJECTION);
        setUri(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        setSortOrder(MediaStore.Video.Media.DATE_ADDED + " DESC");

        setSelection(MIME_TYPE + "=? or " + MIME_TYPE + "=?");
        String[] selectionArgs;
        selectionArgs = new String[] { "video/mpeg", "video/mp4" };
        setSelectionArgs(selectionArgs);
    }
}
