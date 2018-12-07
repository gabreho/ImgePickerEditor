package com.skrumble.picketeditor.data_loaders;

import android.content.Context;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

public class ImageLoader extends CursorLoader {
    private static final String[] IMAGE_PROJECTION = {
            //Base File
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.MIME_TYPE
    };

    public ImageLoader(@NonNull Context context) {
        super(context);
        setProjection(IMAGE_PROJECTION);
        setUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        setSortOrder(MediaStore.Images.Media.DATE_ADDED + " DESC");

        setSelection(MIME_TYPE + "=? or " + MIME_TYPE + "=? or "+ MIME_TYPE + "=? or " + MIME_TYPE + "=?");

        String[] selectionArgs;
        selectionArgs = new String[] { "image/jpeg", "image/png", "image/jpg","image/gif" };
        setSelectionArgs(selectionArgs);
    }
}