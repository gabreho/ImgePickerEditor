package com.skrumble.picketeditor.data_loaders;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;

import com.skrumble.picketeditor.utility.Constants;

public class FileLoader extends CursorLoader {
    private static final String[] FILE_PROJECTION = {
            //Base File
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,

            //Normal File
            MediaStore.Files.FileColumns.MIME_TYPE
    };

    public FileLoader(@NonNull Context context) {
        super(context);
        setProjection(FILE_PROJECTION);
        setUri(Constants.EXTERNAL_URI);
        setSortOrder(MediaStore.Files.FileColumns.DATE_ADDED + " DESC");

        setSelection(MediaStore.Files.FileColumns.MEDIA_TYPE
                + "!="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " AND "
                + MediaStore.Files.FileColumns.MEDIA_TYPE
                + "!="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);

//        setSelection(MIME_TYPE + "=? or "
////                + MIME_TYPE + "=? or "
////                + MIME_TYPE + "=? or "
//                + MIME_TYPE + "=?");
//
//        String[] selectionArgs;
//        selectionArgs = new String[] { "text/txt", "text/plain" };
//        setSelectionArgs(selectionArgs);
    }
}