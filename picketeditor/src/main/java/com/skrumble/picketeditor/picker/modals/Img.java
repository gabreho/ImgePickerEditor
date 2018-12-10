package com.skrumble.picketeditor.picker.modals;

import java.io.Serializable;

import com.skrumble.picketeditor.enumeration.GalleryType;

public class Img implements Serializable {
    private String headerDate;
    private String contentUrl;
    private String url;
    private Boolean isSelected;
    private String scrollerDate;
    private int position;
    private int duration;
    private GalleryType galleryType;

    public Img(String headerDate, String contentUrl, String url, String scrollerDate, GalleryType galleryType) {
        this.headerDate = headerDate;
        this.contentUrl = contentUrl;
        this.url = url;
        this.isSelected = false;
        this.scrollerDate = scrollerDate;
        this.galleryType = galleryType;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getHeaderDate() {
        return headerDate;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public String getUrl() {
        return url;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getScrollerDate() {
        return scrollerDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public GalleryType getGalleryType() {
        return galleryType;
    }
}
