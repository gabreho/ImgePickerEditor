package com.skrumble.picketeditor.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Media implements Parcelable {
    private String path;
    private String name;
    private String extension;
    private long time;
    private int mediaType;
    private long size;
    private int id;
    private long duration;
    private String mimeType;
    private boolean selected;

    public Media() {
        id = 0;
        name = "";
        extension = "";

        path = "";

        time = 0;
        mediaType = 0;
        mimeType = "";
        size = 0;

        duration = 0;

        selected = false;
    }

    public String getFileName(){
        return getName() + getExtension();
    }

    public boolean isVideo(){
        return getMimeType().contains("video");
    }

    public boolean isImage(){
        return getMimeType().contains("image");
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;

        try {
            this.extension = this.path.substring(this.path.lastIndexOf("."));
        } catch (Exception e){
            e.printStackTrace();
            this.extension = "";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.name);
        dest.writeString(this.extension);
        dest.writeLong(this.time);
        dest.writeInt(this.mediaType);
        dest.writeLong(this.size);
        dest.writeInt(this.id);
    }

    protected Media(Parcel in) {
        this.path = in.readString();
        this.name = in.readString();
        this.extension = in.readString();
        this.time = in.readLong();
        this.mediaType = in.readInt();
        this.size = in.readLong();
        this.id = in.readInt();
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };
}

