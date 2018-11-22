package com.skrumble.picketeditor.picker.modals;

import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import com.skrumble.picketeditor.picker.utility.Constants;
import com.skrumble.picketeditor.picker.utility.Utility;

import java.io.Serializable;

public class Img implements Serializable {
    private String headerDate;
    private String contentUrl;
    private String url;
    private Boolean isSelected;
    private String scrollerDate;
    private int position;
    private int type;
    private int duration;

    public Img(String headerDate, String contentUrl, String url, String scrollerDate, int type) {
        this.headerDate = headerDate;
        this.contentUrl = contentUrl;
        this.url = url;
        this.isSelected = false;
        this.scrollerDate = scrollerDate;
        setType(type);
    }

    public int getType() {
        return type;
    }

    private void setType(int type) {
        if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
            this.type = Constants.TYPE_IMAGE;
        } else {
            this.type = Constants.TYPE_VIDEO;
        }
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

    public void setHeaderDate(String headerDate) {
        this.headerDate = headerDate;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public void setScrollerDate(String scrollerDate) {
        this.scrollerDate = scrollerDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
