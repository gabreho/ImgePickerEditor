package com.skrumble.picketeditor.data_loaders;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;

import com.skrumble.picketeditor.utility.Constants;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

public class VideoLoader extends CursorLoader {
    private static final String[] VIDEO_PROJECTION = {
            //Base File
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            //Video File
            MediaStore.Video.Media.DURATION
    };

    public VideoLoader(@NonNull Context context) {
        super(context);

        setProjection(VIDEO_PROJECTION);
        setUri(Constants.EXTERNAL_URI);
        setSortOrder(MediaStore.Video.Media.DATE_ADDED + " DESC");

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        setSelection(selection);

//        setSelection(selection + " and " + MIME_TYPE + "=? or " + MIME_TYPE + " =? ");
//        String[] selectionArgs;
//        selectionArgs = new String[] { "video/mpeg", "video/mp4" };
//        setSelectionArgs(selectionArgs);
    }
}