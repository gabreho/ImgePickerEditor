package com.skrumble.picketeditor.data_loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.skrumble.picketeditor.enumeration.GalleryType;
import com.skrumble.picketeditor.model.Media;
import com.skrumble.picketeditor.public_interface.OnCompletion;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.provider.MediaStore.Files.FileColumns._ID;
import static android.provider.MediaStore.Files.FileColumns.TITLE;
import static android.provider.MediaStore.Files.FileColumns.DATA;
import static android.provider.MediaStore.Files.FileColumns.SIZE;
import static android.provider.MediaStore.Files.FileColumns.DATE_ADDED;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE;

import static android.provider.MediaStore.Video.VideoColumns.DURATION;
import static android.provider.MediaStore.Files.FileColumns.MIME_TYPE;

public class MediaLoaderCallBack implements LoaderManager.LoaderCallbacks<Cursor> {

    GalleryType galleryType;
    CursorLoader cursorLoader;
    WeakReference<Context> context;

    OnCompletion<GalleryType, ArrayList<Media>> completion;

    MediaLoaderCallBack(Context context, GalleryType galleryType, OnCompletion<GalleryType, ArrayList<Media>> onCompletion) {
        this.galleryType = galleryType;
        this.context = new WeakReference<>(context);
        this.completion = onCompletion;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        switch (galleryType) {
            case PICTURE:
                cursorLoader = new ImageLoader(context.get());
                break;
            case VIDEO:
                cursorLoader = new VideoLoader(context.get());
                break;
            case FILE:
                cursorLoader = new FileLoader(context.get());
                break;
            case AUDIO:
                cursorLoader = new AudioLoader(context.get());
                break;
            case PICTURE_VIDEO:
                cursorLoader = new MediaLoader(context.get());
                break;
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.isClosed()){
            return;
        }

        if (cursor.getPosition() != -1) {
            cursor.moveToPosition(-1);
        }

        switch (galleryType) {
            case PICTURE:
                onImageResult(cursor);
                break;
            case VIDEO:
                onVideoResult(cursor);
                break;
            case FILE:
                onFileResult(cursor);
                break;
            case PICTURE_VIDEO:
                onMediaResult(cursor);
                break;
            case AUDIO:
                onAudioResult(cursor);
                break;
        }
    }

    private void onAudioResult(Cursor cursor) {
        ArrayList<Media> mediaArrayList = new ArrayList<>();

        while (cursor.moveToNext()) {
            Media media = new Media();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(DATA));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(SIZE));
            long dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_ADDED));
            int mediaType = cursor.getInt(cursor.getColumnIndexOrThrow(MEDIA_TYPE));

            long duration = cursor.getLong(cursor.getColumnIndexOrThrow(DURATION));

            if (size < 1) continue;
            if (path == null || path.equals("")) continue;

            media.setId(id);
            media.setName(name);
            media.setTime(dateTime);
            media.setMediaType(mediaType);
            media.setSize(size);
            media.setPath(path);
            media.setDuration(duration);

            if (new File(path).exists()){
                mediaArrayList.add(media);
            }
        }

        completion.onCompleted(galleryType, mediaArrayList);
        cursor.close();
    }

    private void onMediaResult(Cursor cursor) {
        ArrayList<Media> mediaArrayList = new ArrayList<>();

        while (cursor.moveToNext()) {
            Media media = new Media();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(DATA));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(SIZE));
            long dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_ADDED));
            String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MIME_TYPE));
            long duration = cursor.getLong(cursor.getColumnIndexOrThrow(DURATION));

            if (size < 1) continue;
            if (path == null || path.equals("")) continue;

            media.setId(id);
            media.setName(name);
            media.setTime(dateTime);
            media.setMimeType(mimeType);
            media.setSize(size);
            media.setPath(path);
            media.setDuration(duration);

            if (new File(path).exists()){
                mediaArrayList.add(media);
            }
        }

        completion.onCompleted(galleryType, mediaArrayList);
        cursor.close();
    }

    private void onFileResult(Cursor cursor) {
        ArrayList<Media> mediaArrayList = new ArrayList<>();
        List<String> ignoreList = Arrays.asList("png", "jpg", "jpeg", "gif","wmv", "mp4", "mpg", "m4v", "mov", "avi");

        while (cursor.moveToNext()) {
            Media media = new Media();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(DATA));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(SIZE));
            long dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_ADDED));
            int mediaType = cursor.getInt(cursor.getColumnIndexOrThrow(MEDIA_TYPE));

            if (size < 1) continue;
            if (path == null || path.equals("")) continue;

            media.setId(id);
            media.setName(name);
            media.setTime(dateTime);
            media.setMediaType(mediaType);
            media.setSize(size);
            media.setPath(path);

            String ex = media.getExtension().ext;

            if (ex.isEmpty() || ex.length() != 3){
                continue;
            }

            if (ignoreList.contains(ex)){
                continue;
            }

            if (new File(path).exists() == false){
                continue;
            }

            mediaArrayList.add(media);
        }

        completion.onCompleted(galleryType, mediaArrayList);
        cursor.close();
    }

    private void onVideoResult(Cursor cursor) {
        ArrayList<Media> mediaArrayList = new ArrayList<>();

        while (cursor.moveToNext()) {
            Media media = new Media();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(DATA));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(SIZE));
            long dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_ADDED));
            String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MIME_TYPE));

            long duration = cursor.getLong(cursor.getColumnIndexOrThrow(DURATION));

            if (size < 1) continue;
            if (path == null || path.equals("")) continue;

            media.setId(id);
            media.setName(name);
            media.setTime(dateTime);
            media.setMimeType(mimeType);
            media.setSize(size);
            media.setPath(path);
            media.setDuration(duration);

            if (new File(path).exists()){
                mediaArrayList.add(media);
            }
        }

        completion.onCompleted(galleryType, mediaArrayList);
        cursor.close();
    }

    private void onImageResult(Cursor cursor) {
        ArrayList<Media> mediaArrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Media media = new Media();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(DATA));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(SIZE));
            long dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_ADDED));
            String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MIME_TYPE));

            if (size < 1) continue;
            if (path == null || path.equals("")) continue;

            media.setId(id);
            media.setName(name);
            media.setTime(dateTime);
            media.setSize(size);
            media.setMimeType(mimeType);
            media.setPath(path);

            if (new File(path).exists()){
                mediaArrayList.add(media);
            }
        }

        completion.onCompleted(galleryType, mediaArrayList);
        cursor.close();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
