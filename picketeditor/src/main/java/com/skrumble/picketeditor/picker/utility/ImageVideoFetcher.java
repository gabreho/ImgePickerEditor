package com.skrumble.picketeditor.picker.utility;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.skrumble.picketeditor.gallery.GalleryActivity;
import com.skrumble.picketeditor.picker.modals.Img;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.skrumble.picketeditor.gallery.GalleryActivity.*;
import static com.skrumble.picketeditor.gallery.GalleryActivity.GAlLERY_TYPE_PHOTO_AND_VIDEO;
import static com.skrumble.picketeditor.gallery.GalleryActivity.GAlLERY_TYPE_PICTURE;
import static com.skrumble.picketeditor.gallery.GalleryActivity.GAlLERY_TYPE_VIDEO;

public class ImageVideoFetcher extends AsyncTask<GalleryType, Void, ArrayList<Img>> {
    private Context context;

    public ImageVideoFetcher(Context context) {
        this.context = context;
    }

    public void setDuration(Img image){
        MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.parse(image.getContentUrl()));
        if (mediaPlayer != null){
            int duration = mediaPlayer.getDuration();
            image.setDuration(duration);
        }
    }

    @Override
    protected ArrayList<Img> doInBackground(GalleryType... galleryTypes) {
        GalleryType galleryType = galleryTypes[0];

        Cursor cursor = Utility.getCursor(context, galleryType);
        ArrayList<Img> list = new ArrayList<>();
        String header = "";
        int limit = 100;
        if (cursor.getCount() < 100) {
            limit = cursor.getCount();
        }

        int date;
        int data;
        int contentUrl;
        int type;

        switch (galleryType) {
            case PICTURE:
                date = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
                data = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                contentUrl = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                type = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
                break;
            case VIDEO:
                date = cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN);
                data = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                contentUrl = cursor.getColumnIndex(MediaStore.Video.Media._ID);
                type = MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
                break;
            default:
                date = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
                data = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                contentUrl = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
                type = cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);
                break;
        }
        Calendar calendar;
        for (int i = 0; i < limit; i++) {
            cursor.moveToNext();
            Uri path = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + cursor.getInt(contentUrl));
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(cursor.getLong(date));
            String dateDifference = Utility.getDateDifference(context, calendar);

            if (!header.equalsIgnoreCase("" + dateDifference)) {
                header = "" + dateDifference;
                list.add(new Img("" + dateDifference, "", "", "", GalleryType.PICTURE));
            }

            switch (galleryType) {
                case PICTURE:
                    list.add(new Img("" + header, "" + path, cursor.getString(data), "", GalleryType.PICTURE));
                    break;
                case VIDEO:
                    Img img = new Img("" + header, "" + path, cursor.getString(data), "", GalleryType.VIDEO);
                    setDuration(img);
                    list.add(img);
                    break;
                default:
                    Img img2;
                    if (cursor.getInt(type) == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                        img2 = new Img("" + header, "" + path, cursor.getString(data), "", GalleryType.PICTURE);
                    } else {
                        img2 = new Img("" + header, "" + path, cursor.getString(data), "", GalleryType.VIDEO);
                        setDuration(img2);
                    }

                    list.add(img2);

                    break;
            }

        }
        cursor.close();

        return list;
    }
}
