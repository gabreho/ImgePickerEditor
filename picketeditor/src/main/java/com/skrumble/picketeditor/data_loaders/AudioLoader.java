package com.skrumble.picketeditor.data_loaders;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

public class AudioLoader extends CursorLoader {
    private static final String[] AUDIO_PROJECTION = {
            //Base File
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            //Audio File
            MediaStore.Audio.Media.DURATION
    };

    public AudioLoader(Context context) {
        super(context);

        setProjection(AUDIO_PROJECTION);
        setUri(MediaStore.Files.getContentUri("external"));
        setSortOrder(MediaStore.Audio.Media.DATE_ADDED + " DESC");

        setSelection(MIME_TYPE + "=? or "
                + MIME_TYPE + "=? or "
//                + MIME_TYPE + "=? or "
                + MIME_TYPE + "=?");
        String[] selectionArgs;
        selectionArgs = new String[]{"audio/mpeg", "audio/mp3", "audio/x-ms-wma"};
        setSelectionArgs(selectionArgs);
    }
}
