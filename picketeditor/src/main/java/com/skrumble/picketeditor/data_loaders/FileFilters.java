package com.skrumble.picketeditor.data_loaders;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;

import com.skrumble.picketeditor.enumeration.GalleryType;
import com.skrumble.picketeditor.model.Media;
import com.skrumble.picketeditor.public_interface.OnCompletion;

import java.util.ArrayList;

public class FileFilters {

    public static void getData(FragmentActivity activity, GalleryType galleryType, OnCompletion<GalleryType, ArrayList<Media>> callback) {
        switch (galleryType) {
            case PICTURE:
                getImages(activity, callback);
                break;
            case VIDEO:
                getVideos(activity, callback);
                break;
            case PICTURE_VIDEO:
                getMedia(activity, callback);
                break;
            case AUDIO:
                getAudios(activity, callback);
                break;
            case FILE:
                getFiles(activity, callback);
                break;
        }
    }

    public static void getImages(FragmentActivity activity, OnCompletion<GalleryType, ArrayList<Media>> callback){
        LoaderManager.getInstance(activity).initLoader(0, null, new MediaLoaderCallBack(activity, GalleryType.PICTURE, callback));
    }

    public static void getVideos(FragmentActivity activity, OnCompletion<GalleryType, ArrayList<Media>> callback){
        LoaderManager.getInstance(activity).initLoader(0, null, new MediaLoaderCallBack(activity, GalleryType.VIDEO, callback));
    }

    public static void getAudios(FragmentActivity activity, OnCompletion<GalleryType, ArrayList<Media>> callback){
        LoaderManager.getInstance(activity).initLoader(0, null, new MediaLoaderCallBack(activity, GalleryType.AUDIO, callback));
    }

    public static void getFiles(FragmentActivity activity, OnCompletion<GalleryType, ArrayList<Media>> callback){
        LoaderManager.getInstance(activity).initLoader(0, null, new MediaLoaderCallBack(activity, GalleryType.FILE, callback));
    }

    public static void getMedia(FragmentActivity activity, OnCompletion<GalleryType, ArrayList<Media>> callback){
        LoaderManager.getInstance(activity).initLoader(0, null, new MediaLoaderCallBack(activity, GalleryType.PICTURE_VIDEO, callback));
    }
}