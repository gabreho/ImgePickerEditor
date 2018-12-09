package com.skrumble.picketeditor.data_loaders;

import android.content.Context;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;

import com.skrumble.picketeditor.utility.Constants;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

public class MediaLoader extends CursorLoader {

    private static String[] MEDIA_PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Video.Media.DURATION };

    public MediaLoader(@NonNull Context context) {
        super(context);

        setProjection(MEDIA_PROJECTION);
        setUri(Constants.EXTERNAL_URI);
        setSortOrder(MediaStore.Video.Media.DATE_ADDED + " DESC");

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        setSelection(selection);
    }
}