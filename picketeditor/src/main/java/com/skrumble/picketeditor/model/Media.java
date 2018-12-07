package com.skrumble.picketeditor.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Media implements Parcelable {
    public String path;
    public String name;
    public String extension;
    public long time;
    public String mediaType;
    public long size;
    public int id;
    public long duration;

    public Media() {
        id = 0;
        name = "";
        extension = "";

        path = "";

        time = 0;
        mediaType = "";
        size = 0;

        duration = 0;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        try {
            this.extension = name.substring(name.lastIndexOf("."), name.length());
        } catch (Exception e){
            e.printStackTrace();
            this.extension = "";
        }
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

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
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
        dest.writeString(this.mediaType);
        dest.writeLong(this.size);
        dest.writeInt(this.id);
    }

    protected Media(Parcel in) {
        this.path = in.readString();
        this.name = in.readString();
        this.extension = in.readString();
        this.time = in.readLong();
        this.mediaType = in.readString();
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

