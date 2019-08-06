package com.skrumble.picketeditor.utility;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;

public class PickerEditorStyleParams implements Parcelable {
    @ColorInt
    int accentColor;

    @ColorInt
    int titleColor;

    protected PickerEditorStyleParams(Parcel in) {
        accentColor = in.readInt();
        titleColor = in.readInt();
    }

    public PickerEditorStyleParams(@ColorInt int accentColor, @ColorInt int titleColor) {
        this.accentColor = accentColor;
        this.titleColor = titleColor;
    }

    public static final Creator<PickerEditorStyleParams> CREATOR = new Creator<PickerEditorStyleParams>() {
        @Override
        public PickerEditorStyleParams createFromParcel(Parcel source) {
            return new PickerEditorStyleParams(source);
        }

        @Override
        public PickerEditorStyleParams[] newArray(int size) {
            return new PickerEditorStyleParams[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.accentColor);
        dest.writeInt(this.titleColor);
    }

    public int getAccentColor() {
        return accentColor;
    }

    public int getTitleColor() {
        return titleColor;
    }
}
