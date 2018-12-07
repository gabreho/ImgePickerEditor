package com.skrumble.picketeditor.data_loaders;

import android.content.Context;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;

public class MediaLoader extends CursorLoader {

    private static String[] MEDIA_PROJECTION = {
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.PARENT};

    public MediaLoader(@NonNull Context context) {
        super(context);

        setProjection(MEDIA_PROJECTION);
        setUri(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        setSortOrder(MediaStore.Video.Media.DATE_ADDED + " DESC");

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        setSelection(selection);

        String[] selectionArgs;
        selectionArgs = new String[] {"image/jpeg", "image/png", "image/jpg","image/gif","video/mpeg","video/mp4" };
        setSelectionArgs(selectionArgs);
    }
}