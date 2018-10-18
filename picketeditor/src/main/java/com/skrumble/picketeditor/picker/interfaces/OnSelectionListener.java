package com.skrumble.picketeditor.picker.interfaces;

import android.view.View;

import com.skrumble.picketeditor.picker.modals.Img;


public interface OnSelectionListener {
    void onClick(Img Img, View view, int position);

    void onLongClick(Img img, View view, int position);
}
