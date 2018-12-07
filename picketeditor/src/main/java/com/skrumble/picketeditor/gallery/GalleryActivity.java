package com.skrumble.picketeditor.gallery;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.skrumble.picketeditor.PickerEditor;
import com.skrumble.picketeditor.R;
import com.skrumble.picketeditor.adapters.MediaGridAdapter;
import com.skrumble.picketeditor.data_loaders.FileFilters;
import com.skrumble.picketeditor.enumeration.GalleryType;
import com.skrumble.picketeditor.model.Media;
import com.skrumble.picketeditor.picker.adapters.MainImageAdapter;
import com.skrumble.picketeditor.picker.interfaces.OnSelectionListener;
import com.skrumble.picketeditor.picker.modals.Img;
import com.skrumble.picketeditor.public_interface.OnClickAction;
import com.skrumble.picketeditor.public_interface.OnCompletion;
import com.skrumble.picketeditor.utility.HeaderItemDecoration;
import com.skrumble.picketeditor.utility.ImageVideoFetcher;
import com.skrumble.picketeditor.utility.Utility;

import java.util.ArrayList;

public class GalleryActivity  extends AppCompatActivity implements OnSelectionListener, OnClickAction<Media> {

    public static GalleryActivity activity;

    public static final String EXTRA_GALLERY_TYPE = "GALLERY_TYPE";
    public static final int GAlLERY_TYPE_PICTURE = 1;
    public static final int GAlLERY_TYPE_VIDEO = 2;
    public static final int GAlLERY_TYPE_PHOTO_AND_VIDEO = 3;

//    private int typeOfGallery;

    private GalleryType galleryType;

    private RecyclerView recyclerView;
    private MediaGridAdapter mediaGridAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        setGalleryType(getIntent());

        activity = this;

        Utility.getScreenSize(this);

        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);

        setupAppBar();

        mediaGridAdapter = new MediaGridAdapter();
        mediaGridAdapter.setOnClickAction(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, MainImageAdapter.SPAN_COUNT);

        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(mediaGridAdapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateImages();
    }

    private void setGalleryType(Intent intent) {

        int intExtra = intent.getIntExtra(EXTRA_GALLERY_TYPE, GAlLERY_TYPE_PHOTO_AND_VIDEO);

        galleryType = GalleryType.getGalleryTypeFromInt(intExtra);

        setTitle(galleryType.title);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Utility.getScreenSize(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.getScreenSize(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utility.getScreenSize(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity = null;
    }

    @SuppressLint("StaticFieldLeak")
    private void updateImages() {
        FileFilters.getData(this, galleryType, new OnCompletion<GalleryType, ArrayList<Media>>() {
            @Override
            public void onCompleted(GalleryType galleryType, ArrayList<Media> media) {
                mediaGridAdapter.setData(media);
            }
        });
    }

    private void setupAppBar() {
        setStatusBarColor(Color.BLACK);

        toolbar = findViewById(R.id.toolbar);

        // Set all of the Toolbar coloring
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.ally_accent_color));

        toolbar.setVisibility(View.VISIBLE);

        // Color buttons inside the Toolbar
        Drawable stateButtonDrawable = ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_and_icn_back);
        if (stateButtonDrawable != null) {
            stateButtonDrawable.mutate();
            stateButtonDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            toolbar.setNavigationIcon(stateButtonDrawable);
        }

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Window window = getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
            }
        }
    }

    @Override
    public void onClick(Img object, View view, int position) {
        switch (object.getGalleryType()) {
            case PICTURE:
                PickerEditor.starEditor(GalleryActivity.this, object.getUrl());
                break;
            case VIDEO:
                PickerEditor.starVideoEditor(this, object.getUrl());
                break;
            default:
                Intent intent = new Intent();
                intent.putExtra(PickerEditor.RESULT_FILE, object.getUrl());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void onLongClick(Img img, View view, int position) {

    }

    @Override
    public void onClick(Media object) {

    }
}
