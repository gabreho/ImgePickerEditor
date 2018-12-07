package com.skrumble.picketeditor.enumeration;

import com.skrumble.picketeditor.R;

public enum GalleryType {
    PICTURE(R.string.photos),
    VIDEO(R.string.videos),
    FILE(R.string.file),
    PICTURE_VIDEO(R.string.gallery),
    AUDIO(R.string.audio);

    public int title;

    GalleryType(int title){
        this.title = title;
    }

    public static GalleryType getGalleryTypeFromInt(int intExtra){
        switch (intExtra) {
            case 1:
                return PICTURE;
            case 2:
                return VIDEO;
            case 3:
                return FILE;
            default:
                return PICTURE_VIDEO;
        }
    }
}
